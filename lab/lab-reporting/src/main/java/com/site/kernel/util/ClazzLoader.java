package com.site.kernel.util;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

public class ClazzLoader {
   public static URL getResource(String resource) {
      ClassLoader classLoader = null;
      URL url = null;

      try {
         classLoader = getTCL();
         if (classLoader != null) {
            url = classLoader.getResource(resource);
            if (url != null) {
               return url;
            }
         }

         // We could not find resource. Ler us now try with the
         // classloader that loaded this class.
         classLoader = ClazzLoader.class.getClassLoader();
         if (classLoader != null) {
            url = classLoader.getResource(resource);
            if (url != null) {
               return url;
            }
         }
      } catch (Throwable t) {
      }

      // Last ditch attempt: get the resource from the class path. It
      // may be the case that clazz was loaded by the Extentsion class
      // loader which the parent of the system class loader. Hence the
      // code below.
      return ClassLoader.getSystemResource(resource);
   }

   public static InputStream getResourceAsStream(String resource) {
      ClassLoader classLoader = null;
      InputStream is = null;

      try {
         classLoader = getTCL();
         if (classLoader != null) {
            is = classLoader.getResourceAsStream(resource);
            if (is != null) {
               return is;
            }
         }

         // We could not find resource. Ler us now try with the
         // classloader that loaded this class.
         classLoader = ClazzLoader.class.getClassLoader();
         if (classLoader != null) {
            is = classLoader.getResourceAsStream(resource);
            if (is != null) {
               return is;
            }
         }
      } catch (Throwable t) {
      }

      // Last ditch attempt: get the resource from the class path. It
      // may be the case that clazz was loaded by the Extentsion class
      // loader which the parent of the system class loader. Hence the
      // code below.
      return ClassLoader.getSystemResourceAsStream(resource);
   }

   private static ClassLoader getTCL() throws IllegalAccessException, InvocationTargetException {
      // Are we running on a JDK 1.2 or later system?
      Method method = null;
      try {
         method = Thread.class.getMethod("getContextClassLoader", (Class[]) null);
      } catch (NoSuchMethodException e) {
         // We are running on JDK 1.1
         return null;
      }

      return (ClassLoader) method.invoke(Thread.currentThread(), (Object[]) null);
   }

   public static Class loadClass(String clazz) throws ClassNotFoundException {
      try {
         return Class.forName(clazz);
      } catch (ClassNotFoundException e) {
      }

      try {
         return getTCL().loadClass(clazz);
      } catch (Throwable e) {
         // we reached here because tcl was null or because of a
         // security exception, or because clazz could not be loaded...
         // In any case we now try one more time
         return Class.forName(clazz);
      }
   }
}