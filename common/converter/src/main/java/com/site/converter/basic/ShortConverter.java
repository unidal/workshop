package com.site.converter.basic;

import java.lang.reflect.Type;

import com.site.converter.Converter;
import com.site.converter.ConverterException;
import com.site.converter.TypeUtil;

public class ShortConverter implements Converter<Short> {

   public boolean canConvert(Type fromType, Type targetType) {
      return TypeUtil.isTypeSupported(fromType, Number.class, Boolean.TYPE, Boolean.class, String.class, Enum.class);
   }

   public Short convert(Object from, Type targetType) throws ConverterException {
      if (from instanceof Number) {
         return ((Number) from).shortValue();
      } else if (from instanceof Boolean) {
         return ((Boolean) from).booleanValue() ? (short) 1 : 0;
      } else if (from instanceof Enum) {
         return (short) ((Enum<?>) from).ordinal();
      } else {
         try {
            return Short.valueOf(from.toString());
         } catch (NumberFormatException e) {
            throw new ConverterException(e);
         }
      }
   }

   public Type getTargetType() {
      return Short.class;
   }
}
