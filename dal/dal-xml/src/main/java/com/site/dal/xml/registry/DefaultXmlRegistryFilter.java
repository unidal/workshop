package com.site.dal.xml.registry;

import java.util.HashSet;
import java.util.Set;

public class DefaultXmlRegistryFilter implements XmlRegistryFilter {
   private Set<String> m_packageNames = new HashSet<String>();

   public boolean matches(Class<?> clazz) {
      if (clazz.isPrimitive() || clazz.isArray()) {
         return false;
      }

      String packageName = clazz.getPackage().getName();

      if (m_packageNames.isEmpty()) {
         return !packageName.startsWith("java.") && !packageName.startsWith("javax.");
      } else {
         return m_packageNames.contains(packageName);
      }
   }

   public void setPackages(String[] packageNames) {
      for (String packageName : packageNames) {
         m_packageNames.add(packageName);
      }
   }
}
