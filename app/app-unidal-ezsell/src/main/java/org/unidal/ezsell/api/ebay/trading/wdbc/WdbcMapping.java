package org.unidal.ezsell.api.ebay.trading.wdbc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.site.converter.ConverterManager;
import com.site.dal.xml.XmlException;
import com.site.dal.xml.formatter.Formatter;
import com.site.lookup.ContainerHolder;
import com.site.wdbc.WdbcEngine;
import com.site.wdbc.WdbcException;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.WdbcSource;
import com.site.wdbc.http.configuration.WdbcFieldMeta;
import com.site.wdbc.http.configuration.WdbcMeta;

public class WdbcMapping extends ContainerHolder {
   public <T> List<T> apply(Class<T> clazz, WdbcSource source) throws WdbcException {
      WdbcMeta meta = clazz.getAnnotation(WdbcMeta.class);

      if (meta == null) {
         throw new RuntimeException(clazz + " must be annotated by " + WdbcMeta.class);
      }

      List<T> list = new ArrayList<T>();
      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcQuery query = lookup(WdbcQuery.class, meta.name());
      WdbcResult result = engine.execute(query, source);
      int len = result.getRowSize();

      for (int i = 0; i < len; i++) {
         T instance = newInstance(clazz);

         inject(result, i, instance);
         list.add(instance);
      }

      return list;
   }

   private void inject(WdbcResult result, int row, Object instance) {
      Field[] fields = instance.getClass().getDeclaredFields();

      try {
         for (Field field : fields) {
            WdbcFieldMeta meta = field.getAnnotation(WdbcFieldMeta.class);

            if (meta != null) {
               String name = getFieldName(field);
               String format = meta.format();
               String text = result.getString(row, name);
               Object value = prepareValue(field, text, format);

               if (value != null) {
                  field.setAccessible(true);
                  field.set(instance, value);
               }
            }
         }
      } catch (Exception e) {
         throw new RuntimeException("Error when injecting to row(" + row + ").\r\n " + e);
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

   private String getFieldName(Field field) {
      String name = field.getName();

      if (name.startsWith("m_")) {
         return name.substring(2);
      } else {
         return name;
      }
   }

   private <S> S newInstance(Class<S> clazz) {
      try {
         return clazz.newInstance();
      } catch (Exception e) {
         throw new RuntimeException("Error when creating new instance of " + clazz + ".\r\n " + e);
      }
   }
}
