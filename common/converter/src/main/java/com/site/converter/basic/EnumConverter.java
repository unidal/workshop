package com.site.converter.basic;

import java.lang.reflect.Type;

import com.site.converter.Converter;
import com.site.converter.ConverterException;
import com.site.converter.TypeUtil;

public class EnumConverter<T extends Enum<T>> implements Converter<T> {
   public boolean canConvert(Type fromType, Type targetType) {
      Class<?> fromClass = TypeUtil.getRawType(fromType);

      return String.class.isAssignableFrom(fromClass);
   }

   @SuppressWarnings("unchecked")
   public T convert(Object from, Type targetType) throws ConverterException {
      String name = (String) from;
      Class<T> targetClass = (Class<T>) TypeUtil.getRawType(targetType);

      return Enum.valueOf(targetClass, name);
   }

   public Type getTargetType() {
      return Enum.class;
   }
}
