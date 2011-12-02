package com.site.converter;

import java.lang.reflect.Type;

import com.site.converter.advanced.ConstructorConverter;
import com.site.converter.basic.BooleanConverter;
import com.site.converter.basic.ByteConverter;
import com.site.converter.basic.CharConverter;
import com.site.converter.basic.DoubleConverter;
import com.site.converter.basic.EnumConverter;
import com.site.converter.basic.FloatConverter;
import com.site.converter.basic.IntegerConverter;
import com.site.converter.basic.LongConverter;
import com.site.converter.basic.ObjectConverter;
import com.site.converter.basic.ShortConverter;
import com.site.converter.basic.StringConverter;
import com.site.converter.collection.ArrayConverter;
import com.site.converter.collection.ListConverter;
import com.site.converter.dom.NodeArrayConverter;
import com.site.converter.dom.NodeConverter;
import com.site.converter.dom.NodeListConverter;
import com.site.converter.dom.NodeValueConverter;

public class ConverterManager {
	private static final ConverterManager s_instance = new ConverterManager();

	private ConverterRegistry m_registry = new ConverterRegistry();

	private ConverterManager() {
		registerConverters();
	}

	@SuppressWarnings("rawtypes")
	private void registerConverters() {
		m_registry.registerConverter(new BooleanConverter());
		m_registry.registerConverter(new ByteConverter());
		m_registry.registerConverter(new CharConverter());
		m_registry.registerConverter(new DoubleConverter());
		m_registry.registerConverter(new EnumConverter());
		m_registry.registerConverter(new FloatConverter());
		m_registry.registerConverter(new IntegerConverter());
		m_registry.registerConverter(new LongConverter());
		m_registry.registerConverter(new ObjectConverter(), ConverterPriority.VERY_LOW.getValue());
		m_registry.registerConverter(new StringConverter(), ConverterPriority.LOW.getValue());
		m_registry.registerConverter(new ShortConverter());

		m_registry.registerConverter(new ArrayConverter());
		m_registry.registerConverter(new ListConverter<Object>());

		m_registry.registerConverter(new ConstructorConverter(), ConverterPriority.HIGH.getValue());

		m_registry.registerConverter(new NodeConverter());
		m_registry.registerConverter(new NodeArrayConverter(), ConverterPriority.HIGH.getValue());
		m_registry.registerConverter(new NodeListConverter(), ConverterPriority.HIGH.getValue());
		m_registry.registerConverter(new NodeValueConverter(), ConverterPriority.HIGH.getValue());
	}

	public static final ConverterManager getInstance() {
		return s_instance;
	}

	public Object convert(Object from, Type targetType) {
		Class<?> rawType = TypeUtil.getRawType(targetType);

		if (rawType.isAssignableFrom(from.getClass())) {
			// No need to convert
			return from;
		} else {
			Converter<?> converter = m_registry.findConverter(from.getClass(), targetType);

			return converter.convert(from, targetType);
		}
	}

	public ConverterRegistry getRegistry() {
		return m_registry;
	}
}
