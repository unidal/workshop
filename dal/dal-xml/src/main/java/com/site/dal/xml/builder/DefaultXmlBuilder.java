package com.site.dal.xml.builder;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.site.dal.xml.XmlException;
import com.site.dal.xml.dynamic.DynamicAttributes;
import com.site.dal.xml.formatter.Formatter;
import com.site.dal.xml.parser.XmlParserPolicy;
import com.site.dal.xml.registry.AttributeEntry;
import com.site.dal.xml.registry.ElementEntry;
import com.site.dal.xml.registry.XmlRegistry;
import com.site.lookup.ContainerHolder;

public class DefaultXmlBuilder extends ContainerHolder implements XmlBuilder, LogEnabled {
	private XmlRegistry m_registry;

	private XmlParserPolicy m_policy;

	private Logger m_logger;

	public void build(Result result, Object object) throws XmlException {
		if (object == null) {
			return;
		}

		try {
			XmlWriter handler = lookup(XmlWriter.class, result.getClass().getName());
			ElementEntry entry = m_registry.findElementEntry(object.getClass());

			if (entry == null) {
				handleError("No element registered for " + object.getClass(), null);
			}

			handler.setResult(result);
			handler.startDocument();
			processElement(handler, entry, object);
			handler.endDocument();
		} catch (XmlException e) {
			throw e;
		} catch (Exception e) {
			handleError("Error when processing " + object, e);
		}
	}

	@SuppressWarnings("unchecked")
	private String convertList(Object object) throws XmlException {
		StringBuilder sb = new StringBuilder(256);

		if (object instanceof List) {
			for (Object item : (List<Object>) object) {
				sb.append(' ').append(item);
			}
		} else if (object.getClass().isArray()) {
			int len = Array.getLength(object);

			for (int i = 0; i < len; i++) {
				Object item = Array.get(object, i);

				sb.append(' ').append(item);
			}
		} else {
			handleError("Unknown type: " + object.getClass(), null);
		}

		if (sb.length() > 0) {
			return sb.substring(1);
		} else {
			return sb.toString();
		}
	}

	public void enableLogging(Logger logger) {
		m_logger = logger;
	}

	@SuppressWarnings("unchecked")
	private Object getValue(Object object, AnnotatedElement annotated, Class<?> type, String format, boolean isList)
			throws XmlException {
		Object value = null;

		try {
			if (annotated instanceof Field) {
				Field field = (Field) annotated;

				field.setAccessible(true);

				value = field.get(object);
			} else if (annotated instanceof Method) {
				value = ((Method) annotated).invoke(object, new Object[0]);
			}
		} catch (Exception e) {
			handleError("Error when accessing data of " + annotated, e);
		}

		if (format != null && format.length() > 0) {
			Formatter<Object> formatter = lookup(Formatter.class, type.getName());

			value = formatter.format(format, value);
		} else if (isList) {
			value = convertList(value);
		}

		return value;
	}

	private void handleError(String message, Exception cause) throws XmlException {
		switch (m_policy.forUnknownElement()) {
		case IGNORE:
			// ignore it
			break;
		case WARNING:
			m_logger.warn(message, cause);
			break;
		case ERROR:
			throw new XmlException(message, cause);
		}
	}

	@SuppressWarnings("rawtypes")
	private void handleWrapperElement(XmlWriter handler, ElementEntry e, Object object) throws SAXException, XmlException {
		if (object instanceof List) {
			for (Object item : (List) object) {
				processElement(handler, e, item);
			}
		} else if (object instanceof Map) {
			for (Object item : ((Map) object).values()) {
				processElement(handler, e, item);
			}
		} else if (object.getClass().isArray()) {
			int len = Array.getLength(object);

			for (int i = 0; i < len; i++) {
				Object item = Array.get(object, i);

				processElement(handler, e, item);
			}
		} else {
			handleError("Unknown type: " + object.getClass(), null);
		}
	}

	private void processElement(XmlWriter handler, ElementEntry entry, Object object) throws SAXException, XmlException {
		AttributesImpl attrs = new AttributesImpl();
		ElementEntry ee = m_registry.findElementEntry(entry.getType());

		if (ee != null) {
			entry = ee;
		}

		for (AttributeEntry a : entry.getAttributes()) {
			Object fieldValue = getValue(object, a.getAnnotatedElement(), a.getType(), a.getFormat(), a.isList());

			if (fieldValue != null) {
				attrs.addAttribute(null, null, a.getName(), "CDATA", String.valueOf(fieldValue));
			}
		}

		if (object instanceof DynamicAttributes) {
			Map<String, String> dynamicAttributes = ((DynamicAttributes) object).getDynamicAttributes();

			for (Map.Entry<String, String> e : dynamicAttributes.entrySet()) {
				attrs.addAttribute(null, null, e.getKey(), "CDATA", e.getValue());
			}
		}

		if (entry.getName() != null) {
			handler.startElement(null, null, entry.getName(), attrs);
		}

		for (ElementEntry e : entry.getElements()) {
			if (e.getAnnotatedElement() == null) {
				handleWrapperElement(handler, e, object);
			} else {
				Object fieldValue = getValue(object, e.getAnnotatedElement(), e.getType(), e.getFormat(), e.isList());

				if (fieldValue == null) {
					continue;
				}

				if (e.isValue()) {
					char[] chars = String.valueOf(fieldValue).toCharArray();

					handler.characters(chars, 0, chars.length);
					break;
				} else {
					processElement(handler, e, fieldValue);
				}
			}
		}

		for (ElementEntry e : entry.getComponents()) {
			Object fieldValue = getValue(object, e.getAnnotatedElement(), e.getType(), e.getFormat(), e.isList());

			if (fieldValue == null) {
				continue;
			}

			if (e.isValue()) {
				String text = String.valueOf(fieldValue);
				char[] chars = text.toCharArray();

				handler.characters(chars, 0, chars.length);
			} else {
				processElement(handler, e, fieldValue);
			}
		}

		if (ee == null && entry.getElements().isEmpty()) {
			char[] chars = String.valueOf(object).toCharArray();

			handler.characters(chars, 0, chars.length);
		}

		if (entry.getName() != null) {
			handler.endElement(null, null, entry.getName());
		}
	}
}
