package com.site.dal.xml.registry;

public interface XmlRegistry {
   public ElementEntry findElementEntry(Class<?> clazz);

   public Class<?> findRootElementType(String name);

   public void register(Class<?> clazz);
}
