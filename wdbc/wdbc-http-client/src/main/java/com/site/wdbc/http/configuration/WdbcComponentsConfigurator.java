package com.site.wdbc.http.configuration;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.site.lookup.configuration.Component;
import com.site.lookup.configuration.Configuration;
import com.site.wdbc.http.configuration.AbstractWdbcComponentsConfigurator;
import com.site.wdbc.query.WdbcFilter;

class WdbcComponentsConfigurator extends AbstractWdbcComponentsConfigurator {
   private Class<?> m_wdbcClass;

   public WdbcComponentsConfigurator(Class<?> wdbcClass) {
      m_wdbcClass = wdbcClass;
   }

   @Override
   public List<Component> defineComponents() {
      WdbcMeta meta = m_wdbcClass.getAnnotation(WdbcMeta.class);

      if (meta == null) {
         throw new RuntimeException(m_wdbcClass + " must be annotated by " + WdbcMeta.class);
      }

      String roleHint = meta.name();
      Class<? extends WdbcFilter> filterClass = meta.filter();
      Configuration paths = E("paths");
      Map<String, String> map = getFieldMap();

      for (Map.Entry<String, String> e : map.entrySet()) {
         String name = e.getKey();
         String path = e.getValue();

         paths.add(E("path", "name", name).value(path));
      }

      return RQF(roleHint, filterClass).qconfig(paths).getComponents();
   }

   private Map<String, String> getFieldMap() {
      Map<String, String> map = new LinkedHashMap<String, String>();
      Field[] fields = m_wdbcClass.getDeclaredFields();

      for (Field field : fields) {
         WdbcFieldMeta meta = field.getAnnotation(WdbcFieldMeta.class);

         if (meta != null) {
            String name = getFieldName(field);
            String path = meta.value();

            map.put(name, path);
         }
      }

      return map;
   }

   private String getFieldName(Field field) {
      String name = field.getName();

      if (name.startsWith("m_")) {
         return name.substring(2);
      } else {
         return name;
      }
   }
}
