package com.site.web.mvc.payload;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.converter.ConverterManager;
import com.site.converter.TypeUtil;
import com.site.dal.xml.XmlException;
import com.site.dal.xml.formatter.Formatter;
import com.site.lookup.ContainerHolder;
import com.site.lookup.util.ReflectUtils;
import com.site.web.lifecycle.UrlMapping;
import com.site.web.mvc.Action;
import com.site.web.mvc.ActionPayload;
import com.site.web.mvc.ErrorObject;
import com.site.web.mvc.Page;
import com.site.web.mvc.PayloadProvider;
import com.site.web.mvc.model.PayloadFieldModel;
import com.site.web.mvc.model.PayloadModel;
import com.site.web.mvc.model.PayloadPathModel;
import com.site.web.mvc.payload.annotation.FieldMeta;
import com.site.web.mvc.payload.annotation.PathMeta;

public class DefaultPayloadProvider extends ContainerHolder implements PayloadProvider<Page, Action>, LogEnabled {
	private Map<Class<?>, PayloadModel> m_payloadModels = new HashMap<Class<?>, PayloadModel>();

	private Logger m_logger;

	private Object convertValue(Type type, Object value, String format) {
		if (format == null) {
			return ConverterManager.getInstance().convert(value, type);
		} else {
			Class<?> clazz = TypeUtil.getRawType(type);
			Formatter<?> formatter = lookup(Formatter.class, clazz.getName());
			String text = value instanceof String ? (String) value : ((String[]) value)[0];

			try {
				return formatter.parse(format, text);
			} catch (XmlException e) {
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				release(formatter);
			}
		}
	}

	public void enableLogging(Logger logger) {
		m_logger = logger;
	}

	private List<Field> getDeclaredFields(Class<?> payloadClass) {
		List<Field> list = new ArrayList<Field>();
		Class<?> clazz = payloadClass;

		while (clazz != Object.class) {
			Field[] fields = clazz.getDeclaredFields();

			for (Field field : fields) {
				FieldMeta fieldMeta = field.getAnnotation(FieldMeta.class);
				PathMeta pathMeta = field.getAnnotation(PathMeta.class);

				if (fieldMeta != null || pathMeta != null) {
					list.add(field);
				}
			}

			clazz = clazz.getSuperclass();
		}

		return list;
	}

	private Method getSetMethod(Class<?> clazz, String name, Field field) {
		String[] names = new String[2];
		String fieldName = field.getName();

		if (fieldName.startsWith("m_") && fieldName.length() >= 3) {
			names[0] = "set" + Character.toUpperCase(fieldName.charAt(2)) + fieldName.substring(3);
		} else {
			names[0] = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
		}

		names[1] = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);

		for (String methodName : names) {
			if (methodName == null) {
				continue;
			}

			for (Method method : clazz.getMethods()) {
				if (method.getName().equals(methodName)) {
					if (!Modifier.isStatic(method.getModifiers()) && method.getParameterTypes().length == 1) {
						return method;
					}
				}
			}
		}

		return null;
	}

	private void injectFieldValue(ActionPayload<?, ?> payload, PayloadFieldModel fieldModel, Object value) {
		Method method = fieldModel.getMethod();

		if (method != null) {
			Type parameterType = method.getGenericParameterTypes()[0];
			Object convertedValue = convertValue(parameterType, value, fieldModel.getFormat());

			ReflectUtils.invokeMethod(method, payload, convertedValue);
		} else {
			Field field = fieldModel.getField();
			Type parameterType = field.getGenericType();
			Object convertedValue = convertValue(parameterType, value, fieldModel.getFormat());

			ReflectUtils.setField(field, payload, convertedValue);
		}
	}

	private void injectPathValue(ActionPayload<?, ?> payload, PayloadPathModel pathModel, String[] parts) {
		Method method = pathModel.getMethod();

		if (method != null) {
			Type parameterType = method.getGenericParameterTypes()[0];
			Object convertedValue = convertValue(parameterType, parts, null);

			ReflectUtils.invokeMethod(method, payload, convertedValue);
		} else {
			Field field = pathModel.getField();
			Type parameterType = field.getGenericType();
			Object convertedValue = convertValue(parameterType, parts, null);

			ReflectUtils.setField(field, payload, convertedValue);
		}
	}

	private boolean isMultipleValues(Field field, Method method) {
		Class<?> valueType = (method != null ? method.getParameterTypes()[0] : field.getType());

		if (valueType.isArray()) {
			return true;
		} else if (Collection.class.isAssignableFrom(valueType)) {
			return true;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public List<ErrorObject> process(UrlMapping mapping, ParameterProvider provider, ActionPayload<Page, Action> payload) {
		Class<?> payloadClass = payload.getClass();
		PayloadModel payloadModel = m_payloadModels.get(payloadClass);

		if (payloadModel == null) {
			m_logger.warn("Register " + payloadClass + " on demand");
			register((Class<? extends ActionPayload<? extends Page, ? extends Action>>) payloadClass);
			payloadModel = m_payloadModels.get(payloadClass);
		}

		List<ErrorObject> errors = new ArrayList<ErrorObject>();

		for (PayloadFieldModel field : payloadModel.getFields()) {
			try {
				processField(provider, payload, field);
			} catch (Exception e) {
				String name = field.getName();
				ErrorObject error = new ErrorObject("payload.field", e);

				error.setArguments(name, provider.getParameter(name));
				errors.add(error);
			}
		}

		for (PayloadPathModel path : payloadModel.getPaths()) {
			try {
				processPath(mapping, payload, path);
			} catch (Exception e) {
				String name = path.getName();
				ErrorObject error = new ErrorObject("payload.path");

				error.setArguments(name, mapping.getPathInfo());
				errors.add(error);
			}
		}

		return errors;
	}

	private void processField(ParameterProvider provider, ActionPayload<?, ?> payload, PayloadFieldModel fieldModel)
	      throws IOException {
		String name = fieldModel.getName();

		if (fieldModel.isMultipleValues()) {
			String[] values = provider.getParameterValues(name);

			if (values == null && fieldModel.getDefaultValue() != null) {
				values = fieldModel.getDefaultValue().split(",");
			}

			if (values == null) {
				values = new String[0];
			}

			if (values != null) {
				injectFieldValue(payload, fieldModel, values);
			}
		} else if (fieldModel.isFile()) {
			InputStream value = provider.getFile(name);

			if (value != null) {
				injectFieldValue(payload, fieldModel, value);
			}
		} else {
			String value = provider.getParameter(name);

			if (value == null) {
				value = fieldModel.getDefaultValue();
			}

			if (value != null) {
				injectFieldValue(payload, fieldModel, value);
			}
		}
	}

	private void processPath(UrlMapping mapping, ActionPayload<?, ?> payload, PayloadPathModel pathModel)
	      throws IOException {
		String pathInfo = mapping.getPathInfo(); // not starting with "/"
		String[] parts;

		if (pathInfo == null || pathInfo.length() == 0) {
			parts = new String[0];
		} else {
			parts = pathInfo.split(Pattern.quote("/"));
		}

		injectPathValue(payload, pathModel, parts);
	}

	public void register(Class<?> payloadClass) {
		PayloadModel payloadModel = new PayloadModel();

		for (Field field : getDeclaredFields(payloadClass)) {
			FieldMeta fieldMeta = field.getAnnotation(FieldMeta.class);
			PathMeta pathMeta = field.getAnnotation(PathMeta.class);

			if (fieldMeta != null && pathMeta != null) {
				throw new RuntimeException(String.format("Field %s in %s can't be annotated by both %s and %s!",
				      field.getName(), payloadClass, FieldMeta.class.getSimpleName(), PathMeta.class.getSimpleName()));
			}

			if (fieldMeta != null) {
				registerField(payloadClass, payloadModel, field, fieldMeta);
			} else if (pathMeta != null) {
				registerPath(payloadClass, payloadModel, field, pathMeta);
			}
		}

		payloadModel.setPayloadClass(payloadClass);
		m_payloadModels.put(payloadClass, payloadModel);
	}

	private void registerField(Class<?> payloadClass, PayloadModel payloadModel, Field field, FieldMeta fieldMeta) {
		PayloadFieldModel payloadFieldModel = new PayloadFieldModel();
		String name = fieldMeta.value();

		if (!fieldMeta.defaultValue().equals(FieldMeta.NOT_SPECIFIED)) {
			payloadFieldModel.setDefaultValue(fieldMeta.defaultValue());
		}

		payloadFieldModel.setName(name);
		payloadFieldModel.setFormat(fieldMeta.format().length() == 0 ? null : fieldMeta.format());
		payloadFieldModel.setFile(fieldMeta.file());
		payloadFieldModel.setField(field);
		payloadFieldModel.setMethod(getSetMethod(payloadClass, name, field));
		payloadFieldModel.setMultipleValues(isMultipleValues(field, payloadFieldModel.getMethod()));

		if (payloadFieldModel.isFile()) {
			if (payloadFieldModel.isMultipleValues()) {
				throw new RuntimeException("Can't use file() and multiple values together!");
			} else {
				Method m = payloadFieldModel.getMethod();
				Field f = payloadFieldModel.getField();

				if (m != null && m.getParameterTypes()[0] != InputStream.class) {
					throw new RuntimeException("Only InputStream is allowed as parameter type of " + m);
				} else if (f != null && f.getType() != InputStream.class) {
					throw new RuntimeException("Only InputStream is allowed as type of " + f);
				}
			}
		}

		payloadModel.addField(payloadFieldModel);
	}

	private void registerPath(Class<?> payloadClass, PayloadModel payloadModel, Field field, PathMeta pathMeta) {
		PayloadPathModel payloadPathModel = new PayloadPathModel();
		String name = pathMeta.value();

		payloadPathModel.setName(name);
		payloadPathModel.setField(field);
		payloadPathModel.setMethod(getSetMethod(payloadClass, name, field));

		payloadModel.addPath(payloadPathModel);
	}
}
