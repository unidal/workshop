package com.site.kernel.dal;

import java.lang.reflect.Array;
import java.util.List;

import com.site.kernel.common.BaseEnum;

public class ValueType extends BaseEnum {
	public static final ValueType STRING = new ValueType(1, "String", String.class);
	public static final ValueType INT = new ValueType(2, "int", Integer.TYPE);
	public static final ValueType LONG = new ValueType(3, "long", Long.TYPE);
	public static final ValueType SHORT = new ValueType(4, "short", Short.TYPE);
	public static final ValueType CHAR = new ValueType(5, "char", Character.TYPE);
	public static final ValueType DOUBLE = new ValueType(6, "double", Double.TYPE);
	public static final ValueType FLOAT = new ValueType(7, "float", Float.TYPE);
	public static final ValueType BOOLEAN = new ValueType(8, "boolean", Boolean.TYPE);
	public static final ValueType DATE = new ValueType(11, "Date", java.util.Date.class);
	public static final ValueType TIME = new ValueType(12, "Time", Long.TYPE);

	public static final ValueType ARRAY = new ValueType(13, "Array", Array.class);
	public static final ValueType LIST = new ValueType(14, "List", List.class);
	public static final ValueType SUBOBJECT = new ValueType(21, "SubObject", Void.TYPE);

	private Class m_definedClass;

	protected ValueType(int id, String name, Class definedClass) {
		super(id, name);

		m_definedClass = definedClass;
	}

	public Class getDefinedClass() {
		return m_definedClass;
	}

	public Class getWrapperClass() {
		if (m_definedClass == Integer.TYPE) {
			return Integer.class;
		} else if (m_definedClass == Double.TYPE) {
			return Double.class;
		} else if (m_definedClass == Float.TYPE) {
			return Float.class;
		} else if (m_definedClass == Long.TYPE) {
			return Long.class;
		} else if (m_definedClass == Boolean.TYPE) {
			return Boolean.class;
		} else {
			return m_definedClass;
		}
	}

	public static ValueType getByDefinedClass(Class clazz) {
		List enums = getAll(ValueType.class);
		int size = (enums == null ? 0 : enums.size());

		for (int i = 0; i < size; i++) {
			ValueType vt = (ValueType) enums.get(i);

			if (vt.getDefinedClass() == clazz || vt.getWrapperClass() == clazz) {
				return vt;
			}
		}

		return null;
	}
}
