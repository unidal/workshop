package com.site.kernel.dal;

import java.lang.reflect.Method;

public class FieldInfo {
   private Class m_declaringClass;
   private Method m_getter; // Get Method, getXyz() or isXyz()
   private int m_index;
   private String m_name;
   private Method m_setter; // Set Method, setXyz(...)
   private ValueType m_valueType;

   public FieldInfo(String name, ValueType valueType) {
      m_name = name;
      m_valueType = valueType;
   }

   public Class getDeclaringClass() {
      return m_declaringClass;
   }
   
   public Object getFieldValue(Object obj) {
      if (m_getter == null) {
         throw new FieldAccessException("No GET method defined for '" + m_name
               + "' in " + obj.getClass());
      }

      try {
         return m_getter.invoke(obj, (Object[])null);
      } catch (Exception e) {
         throw new FieldAccessException("Error invoking method: "
               + m_getter.getName() + "() of " + obj.getClass(), e);
      }
   }

   public int getIndex() {
      return m_index;
   }

   private Method getMethod(Class clazz, String methodName,
         Class[] parameterTypes) {
      Class base = clazz;

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

   protected Method getMethod(Class declaringClass, String prefix,
         String suffix, Class[] parameterTypes) {
      StringBuffer sb = new StringBuffer(32);

      sb.append(prefix).append(normalize(m_name)).append(suffix);

      return getMethod(declaringClass, sb.toString(), parameterTypes);
   }

   public String getName() {
      return m_name;
   }

   public ValueType getValueType() {
      return m_valueType;
   }

   public void init(Class declaringClass) {
      m_declaringClass = declaringClass;
      
      initGetMethod(declaringClass);
      initSetMethod(declaringClass);
   }

   protected void initSetMethod(Class declaringClass) {
      // setXyz() method
      if (m_valueType != null) {
         Class[] parameterTypes = { m_valueType.getDefinedClass() };

         m_setter = getMethod(declaringClass, "set", "", parameterTypes);
      }
   }

   protected void initGetMethod(Class declaringClass) {
      // getXyz() method
      m_getter = getMethod(declaringClass, "get", "", null);

      // try isXyz() method if value type is boolean
      if (m_getter == null && m_valueType == ValueType.BOOLEAN) {
         m_getter = getMethod(declaringClass, "is", "", null);
      }
   }

   private String normalize(String rawName) {
      int len = (rawName == null ? 0 : rawName.length());
      StringBuffer sb = new StringBuffer(len);
      boolean firstChar = true;
      boolean hyphen = false;

      synchronized (sb) {
         for (int i = 0; i < len; i++) {
            char ch = rawName.charAt(i);

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
      }

      return sb.toString();
   }

   public void setFieldValue(Object obj, Object[] args) {
      if (m_setter == null) {
         throw new FieldAccessException("No SET method defined for '" + m_name
               + "' in " + obj.getClass());
      }

      try {
         m_setter.invoke(obj, args);
      } catch (Exception e) {
         throw new FieldAccessException("Error invoking method: "
               + m_setter.getName() + "(" + args[0] + ") of " + obj.getClass(),
               e);
      }
   }

   public void setIndex(int index) {
      m_index = index;
   }

   protected void setGetter(Method getter) {
      m_getter = getter;
   }

   protected void setSetter(Method setter) {
      m_setter = setter;
   }
}