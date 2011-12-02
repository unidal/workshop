package com.site.lookup.resource;

import java.io.IOException;
import java.net.URL;

public class ClasspathResourceResolver implements ResourceResolver {
   private Class<?> m_baseClass;

   public ClasspathResourceResolver() {
      m_baseClass = getClass();
   }

   public ClasspathResourceResolver(Class<?> baseClass) {
      m_baseClass = baseClass;
   }

   public URL resolve(String relativePath) throws IOException {
      return m_baseClass.getResource(relativePath);
   }
}
