package com.site.kernel.dal;

import java.lang.reflect.Method;

public class DefaultDataObjectNaming implements DataObjectNaming {
   public Method getGetterMethod(Class doClass, DataObjectField field) {
      String name = normalize(null, field.getName(), null);
      Method getter = getMethod(doClass, "get" + name, null);

      if (getter == null && field.getValueType() == ValueType.BOOLEAN) {
         getter = getMethod(doClass, "is" + name, null);

         if (getter == null) {
            getter = getMethod(doClass, "has" + name, null);
         }
      }

      if (getter != null) {
         return getter;
      } else {
         throw new FieldAccessException("GET method (get" + name + ") expected for " + doClass);
      }
   }

   protected Method getMethod(Class doClass, String methodName, Class[] parameterTypes) {
      Class base = doClass;

      while (true) {
         try {
            Method method = base.getDeclaredMethod(methodName, parameterTypes);

            method.setAccessible(true);
            return method;
         } catch (NoSuchMethodException e) {
            if (base.getSuperclass() != Object.class) {
               base = base.getSuperclass();
            } else {
               return null;
            }
         }
      }
   }

   public Method getSetterMethod(Class doClass, DataObjectField field) {
      return getSetterMethod(doClass, field, null, field.getValueType().getDefinedClass());
   }

   protected Method getSetterMethod(Class doClass, DataObjectField field, String suffix, Class parameter) {
      Class[] parameterTypes = { parameter };
      String name = normalize("set", field.getName(), suffix);
      Method setter = getMethod(doClass, name, parameterTypes);

      if (setter != null) {
         return setter;
      } else {
         throw new FieldAccessException("SET method (" + name + ") expected for " + doClass);
      }
   }

   protected String normalize(String prefix, String rawFieldName, String suffix) {
      int len = (rawFieldName == null ? 0 : rawFieldName.length());
      StringBuffer sb = new StringBuffer(len);
      boolean firstChar = true;
      boolean hyphen = false;

      synchronized (sb) {
         if (prefix != null) {
            sb.append(prefix);
         }

         for (int i = 0; i < len; i++) {
            char ch = rawFieldName.charAt(i);

            if (ch == '-' || ch == '_')
               hyphen = true;
            else if (firstChar) {
               firstChar = false;
               sb.append(Character.toUpperCase(ch));
            } else if (hyphen) {
               hyphen = false;
               sb.append(Character.toUpperCase(ch));
            } else
               sb.append(ch);
         }

         if (suffix != null) {
            sb.append(suffix);
         }
      }

      return sb.toString();
   }
}
