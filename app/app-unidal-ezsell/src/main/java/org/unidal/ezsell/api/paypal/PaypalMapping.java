package org.unidal.ezsell.api.paypal;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.site.converter.ConverterManager;
import com.site.converter.TypeUtil;
import com.site.dal.xml.XmlException;
import com.site.dal.xml.formatter.Formatter;
import com.site.lookup.ContainerHolder;

public class PaypalMapping extends ContainerHolder {
   public <T> T apply(Class<T> clazz, String content) {
      PaypalApiResult result = new PaypalApiResult(content);
      T instance = newInstance(clazz);

      inject(result, -1, instance);
      return instance;
   }

   private void inject(PaypalApiResult result, int row, Object instance) {
      Field[] fields = instance.getClass().getDeclaredFields();

      try {
         for (Field field : fields) {
            PaypalFieldMeta meta = field.getAnnotation(PaypalFieldMeta.class);

            if (meta != null) {
               Object value = null;

               if (meta.property()) {
                  Class<?> fieldType = field.getType();
                  boolean primitive = !fieldType.isAnnotationPresent(PaypalMeta.class);

                  if (primitive) {
                     String name = meta.value();
                     String format = meta.format();

                     try {
                        String text = (row < 0 ? result.getProperty(name) : result.getString(row, name));

                        value = prepareValue(field, text, format);
                     } catch (RuntimeException e) {
                        if (meta.required()) {
                           throw e;
                        }
                     }
                  } else {
                     Object fieldValue = newInstance(fieldType);

                     inject(result, row, fieldValue);
                     value = fieldValue;
                  }
               } else {
                  int size = result.getRowSize();
                  List<Object> components = new ArrayList<Object>(size);

                  for (int i = 0; i < size; i++) {
                     Type genericType = field.getGenericType();
                     Type componentType = TypeUtil.getComponentType(genericType);
                     Class<?> componentClass = TypeUtil.getRawType(componentType);
                     Object component = newInstance(componentClass);

                     inject(result, i, component);
                     components.add(component);
                  }

                  value = components;
               }

               if (value != null) {
                  field.setAccessible(true);
                  field.set(instance, value);
               }
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
         throw new RuntimeException("Error when injecting to " + instance + ".\r\n " + e);
      }
   }

   private Object prepareValue(Field field, String text, String format) {
      Object value = null;

      if (text != null) {
         if (format.length() > 0) {
            Formatter<?> formatter = lookup(Formatter.class, field.getType().getName());

            try {
               value = formatter.parse(format, text);
            } catch (XmlException e) {
               throw new RuntimeException("Error when parsing format(" + format + ") for: " + text + ".\r\n " + e);
            } finally {
               release(formatter);
            }
         } else {
            ConverterManager converter = ConverterManager.getInstance();

            value = converter.convert(text, field.getType());
         }
      }

      return value;
   }

   private <S> S newInstance(Class<S> clazz) {
      try {
         return clazz.newInstance();
      } catch (Exception e) {
         throw new RuntimeException("Error when creating new instance of " + clazz + ".\r\n " + e);
      }
   }
}
