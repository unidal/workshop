package com.site.converter.basic;

import java.lang.reflect.Type;

import com.site.converter.Converter;
import com.site.converter.ConverterException;

public class StringConverter implements Converter<String> {
   public boolean canConvert(Type fromType, Type targetType) {
      return true;
   }

   public String convert(Object from, Type targetType) throws ConverterException {
      return from.toString();
   }

   public Type getTargetType() {
      return String.class;
   }
}
