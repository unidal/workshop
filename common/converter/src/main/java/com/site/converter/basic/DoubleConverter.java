package com.site.converter.basic;

import java.lang.reflect.Type;

import com.site.converter.Converter;
import com.site.converter.ConverterException;
import com.site.converter.TypeUtil;

public class DoubleConverter implements Converter<Double> {

   public boolean canConvert(Type fromType, Type targetType) {
      return TypeUtil.isTypeSupported(fromType, Number.class, Boolean.TYPE, Boolean.class, String.class, Enum.class);
   }

   public Double convert(Object from, Type targetType) throws ConverterException {
      if (from instanceof Number) {
         return ((Number) from).doubleValue();
      } else if (from instanceof Boolean) {
         return ((Boolean) from).booleanValue() ? Double.valueOf(1) : 0;
      } else if (from instanceof Enum) {
         return Double.valueOf(((Enum<?>) from).ordinal());
      } else {
         try {
            return Double.valueOf(from.toString().trim());
         } catch (NumberFormatException e) {
            throw new ConverterException(e);
         }
      }
   }

   public Type getTargetType() {
      return Double.class;
   }
}
