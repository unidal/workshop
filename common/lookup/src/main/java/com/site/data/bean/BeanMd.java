package com.site.data.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.site.data.IManagedData;
import com.site.data.IManagedDataMeta;

public class BeanMd<T> extends BaseMd<String, BeanMd.BeanMeta> {
	private T m_bean;

	private BeanAccessor m_accessor;

	public BeanMd(T bean) {
		super(BeanMetaBuilder.INSTANCE.buildMeta(bean.getClass()));

		m_bean = bean;
		m_accessor = new BeanAccessor(getMeta());
	}

	public T bean() {
		return m_bean;
	}

	public IManagedData<?> getData(String key) {
		return wrapChild(m_accessor.getData(m_bean, key));
	}

	public boolean hasData(String key) {
		return m_accessor.hasData(m_bean, key);
	}

	public void setData(String key, IManagedData<?> value) {
		m_accessor.setData(m_bean, key, unwrapChild(value));
	}

	public void unsetData(String key) {
		m_accessor.unsetData(m_bean, key);
	}

	protected static class BeanAccessor {
		private BeanMeta m_meta;

		public BeanAccessor(BeanMeta meta) {
			m_meta = meta;
		}

		private String buildMessage(Method method, Object... args) {
			StringBuilder sb = new StringBuilder(32);

			if (args.length > 0) {
				boolean first = true;

				sb.append(" with (");

				for (Object arg : args) {
					if (first) {
						first = false;
					} else {
						sb.append(", ");
					}

					sb.append(arg);
				}

				sb.append(')');
			}

			String message = String.format("Error when invoking %s%s.", method.toGenericString(), sb);
			return message;
		}

		private Object getJavaPrimitiveDefaultValue(Class<?> type) {
			Object defaultValue;

			if (type == Integer.TYPE) {
				defaultValue = (int) 0;
			} else if (type == Boolean.TYPE) {
				defaultValue = Boolean.FALSE;
			} else if (type == Long.TYPE) {
				defaultValue = (long) 0;
			} else if (type == Double.TYPE) {
				defaultValue = (double) 0;
			} else if (type == Float.TYPE) {
				defaultValue = (float) 0;
			} else if (type == Short.TYPE) {
				defaultValue = (short) 0;
			} else if (type == Byte.TYPE) {
				defaultValue = (byte) 0;
			} else if (type == Character.TYPE) {
				defaultValue = (char) 0;
			} else {
				defaultValue = null;
			}

			return defaultValue;
		}

		public Object getData(Object bean, String key) {
			Method getter = m_meta.getGetter(key);
			Object value = null;

			if (getter != null) {
				value = invokeMethod(bean, getter);
			} else {
				throw new IllegalArgumentException(String.format("No getter(%s) of class(%s) defined.", key, bean.getClass()
						.getName()));
			}

			return value;
		}

		public boolean hasData(Object bean, String key) {
			Object value = getData(bean, key);

			return value != null;
		}

		private Object invokeMethod(Object instance, Method method, Object... args) {
			Object value = null;

			try {
				if (!method.isAccessible()) {
					method.setAccessible(true);
				}

				value = method.invoke(instance, args);
			} catch (IllegalArgumentException e) {
				String message = buildMessage(method, args);

				throw new RuntimeException(message, e);
			} catch (IllegalAccessException e) {
				String message = buildMessage(method, args);

				throw new RuntimeException(message, e);
			} catch (InvocationTargetException e) {
				Throwable target = e.getTargetException();

				if (target instanceof RuntimeException) {
					throw (RuntimeException) target;
				} else {
					String message = buildMessage(method, args);

					throw new RuntimeException(message, target);
				}
			}

			return value;
		}

		public Object setData(Object bean, String key, Object newValue) {
			Method setter = m_meta.getSetter(key);
			Object oldValue = getData(bean, key);

			if (setter != null) {
				invokeMethod(bean, setter, newValue);
			} else {
				throw new IllegalArgumentException(String.format("No setter(%s) of class(%s) defined.", key, bean.getClass()
						.getName()));
			}

			return oldValue;
		}

		public Object unsetData(Object bean, String key) {
			Method getter = m_meta.getGetter(key);
			Object newValue = null;
			Object oldValue = null;

			if (getter != null && getter.getReturnType().isPrimitive()) {
				newValue = getJavaPrimitiveDefaultValue(getter.getReturnType());
				oldValue = invokeMethod(bean, getter);
			}

			setData(bean, key, newValue);
			return oldValue;
		}
	}

	public static class BeanMeta implements IManagedDataMeta<String> {
		private Set<String> m_keys;

		private Map<String, Method> m_getters;

		private Map<String, Method> m_setters;

		protected BeanMeta(Set<String> keys, Map<String, Method> getters, Map<String, Method> setters) {
			m_keys = keys;
			m_getters = getters;
			m_setters = setters;
		}

		public Method getGetter(String key) {
			return m_getters.get(key);
		}

		public Set<String> getKeys() {
			return m_keys;
		}

		public Method getSetter(String key) {
			return m_setters.get(key);
		}

		public boolean isNullable(String key) {
			Method getter = m_getters.get(key);

			return getter != null && !getter.getReturnType().isPrimitive();
		}
	}

	static enum BeanMetaBuilder {
		INSTANCE;

		public BeanMeta buildMeta(Class<?> beanClass) {
			Set<String> properties = new HashSet<String>();
			Map<String, Method> getters = new HashMap<String, Method>();
			Map<String, Method> setters = new HashMap<String, Method>();
			Method[] methods = beanClass.getMethods();

			for (Method method : methods) {
				int modifiers = method.getModifiers();

				if (!Modifier.isStatic(modifiers) && !Modifier.isAbstract(modifiers)) {
					Class<?>[] types = method.getParameterTypes();
					String name = method.getName();

					if (types.length == 0) { // getter
						String property = null;

						if (name.startsWith("get") && !name.equals("getClass")) {
							property = getDecapitalName(name, 3);
						} else if (name.startsWith("is")) {
							property = getDecapitalName(name, 2);
						}

						if (property != null) {
							properties.add(property);
							getters.put(property, method);
						}
					} else if (types.length == 1 && name.startsWith("set")) { // setter
						String property = getDecapitalName(name, 3);

						properties.add(property);
						setters.put(property, method);
					}
				}
			}

			return new BeanMeta(properties, getters, setters);
		}

		private String getDecapitalName(String str, int offset) {
			int len = str.length();
			StringBuilder sb = new StringBuilder(len - offset);
			boolean first = true;

			for (int i = offset; i < len; i++) {
				char ch = str.charAt(i);

				if (first) {
					first = false;
					sb.append(Character.toLowerCase(ch));
				} else {
					sb.append(ch);
				}
			}

			if (sb.length() == 0) {
				return null;
			} else {
				return sb.toString();
			}
		}
	}

	@Override
	protected Object unwrapChild(IManagedData<?> value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IManagedData<?> wrapChild(Object value) {
		// TODO Auto-generated method stub
		return null;
	}
}
