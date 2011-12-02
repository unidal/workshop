package com.site.converter.collection;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.List;

import com.site.converter.Converter;
import com.site.converter.ConverterException;
import com.site.converter.ConverterManager;
import com.site.converter.TypeUtil;

public class ArrayConverter implements Converter<Object> {
   public boolean canConvert(Type fromType, Type targetType) {
      Class<?> fromClass = TypeUtil.getRawType(fromType);

      if (fromClass.isArray()) {
         return true;
      } else if (List.class.isAssignableFrom(fromClass)) {
         return true;
      }

      return false;
   }

   public Object convert(Object from, Type targetType) throws ConverterException {
      Class<?> clazz = TypeUtil.getRawType(from.getClass());
      Class<?> componentType = ((Class<?>) targetType).getComponentType();
      Object array;

      if (clazz.isArray()) {
         int length = Array.getLength(from);

         array = Array.newInstance(componentType, length);

         for (int i = 0; i < length; i++) {
            Object item = Array.get(from, i);
            Object element = ConverterManager.getInstance().convert(item, componentType);

            Array.set(array, i, element);
         }
      } else if (List.class.isAssignableFrom(clazz)) {
         List<?> fromList = (List<?>) from;
         int length = fromList.size();

         array = Array.newInstance(componentType, length);

         for (int i = 0; i < length; i++) {
            Object item = fromList.get(i);
            Object element = ConverterManager.getInstance().convert(item, componentType);

            Array.set(array, i, element);
         }
      } else {
         throw new ConverterException("Unknown type: " + from.getClass());
      }

      return array;
   }

   public Type getTargetType() {
      return Array.class;
   }
}
