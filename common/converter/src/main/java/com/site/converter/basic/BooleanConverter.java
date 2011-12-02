package com.site.converter.basic;

import java.lang.reflect.Type;

import com.site.converter.Converter;
import com.site.converter.ConverterException;

public class BooleanConverter implements Converter<Object> {
   public boolean canConvert(Type type, Type targetType) {
      return true;
   }

   public Type getTargetType() {
      return Boolean.class;
   }

   public Boolean convert(Object from, Type targetType) throws ConverterException {
      if (from instanceof Boolean) {
         return (Boolean) from;
      } else if (from instanceof Number) {
         return ((Number) from).intValue() > 0;
      } else {
         String text = from.toString();

         try {
            Double value = Double.valueOf(text);

            return value.intValue() > 0;
         } catch (NumberFormatException e) {
            // ignore it
         }

         if (text == null) {
            return false;
         } else if ("false".equals(text) || "0".equals(text) || "no".equals(text) || "F".equals(text)) {
            return false;
         } else {
            return true;
         }
      }
   }
}
