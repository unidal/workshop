package com.site.converter.basic;

import java.lang.reflect.Type;

import com.site.converter.Converter;
import com.site.converter.ConverterException;
import com.site.converter.TypeUtil;

public class IntegerConverter implements Converter<Integer> {

   public boolean canConvert(Type fromType, Type targetType) {
      return TypeUtil.isTypeSupported(fromType, Number.class, Boolean.TYPE, Boolean.class, String.class, Enum.class);
   }

   public Integer convert(Object from, Type targetType) throws ConverterException {
      if (from instanceof Number) {
         return ((Number) from).intValue();
      } else if (from instanceof Boolean) {
         return ((Boolean) from).booleanValue() ? Integer.valueOf(1) : 0;
      } else if (from instanceof Enum) {
         return Integer.valueOf(((Enum<?>) from).ordinal());
      } else {
         try {
            return Integer.valueOf(from.toString().trim());
         } catch (NumberFormatException e) {
            throw new ConverterException(e);
         }
      }
   }

   public Type getTargetType() {
      return Integer.class;
   }
}
