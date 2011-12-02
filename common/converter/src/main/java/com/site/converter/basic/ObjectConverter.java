package com.site.converter.basic;

import java.lang.reflect.Type;

import com.site.converter.Converter;
import com.site.converter.ConverterException;

public class ObjectConverter implements Converter<Object> {
   public boolean canConvert(Type fromType, Type targetType) {
      return true;
   }

   public Object convert(Object from, Type targetType) throws ConverterException {
      return from;
   }

   public Type getTargetType() {
      return Object.class;
   }
}
