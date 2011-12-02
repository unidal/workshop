package com.site.kernel;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class SystemInjection {
   // KEY Class => ELEMENT Map (KEY methodName => ELEMENT Method)
   private static final Map<Class<? extends Object>, Map<String, Method>> s_methodMap = new HashMap<Class<? extends Object>, Map<String, Method>>();

   public static void injectProperties(Object obj, List names, List values) {
      if (obj == null) {
         throw new NullPointerException("object is null");
      }

      Class<? extends Object> clazz = obj.getClass();
      int size = (names == null ? 0 : names.size());

      for (int i = 0; i < size; i++) {
         String name = (String) names.get(i);
         Method method = getSetMethod(clazz, name);
         Class[] parameterTypes = method.getParameterTypes();

         if (parameterTypes.length != 1) {
            throw new RuntimeException("Method " + method.getName() + "() can only have one argument");
         }

         Object value = values.get(i);
         Object argument;

         if (value instanceof String) {
            argument = getArgument(parameterTypes[0], (String) value);
         } else if (value instanceof List) {
            argument = getArgument(parameterTypes[0], (List<?>) value);
         } else {
            throw new RuntimeException("Name values must be String of List of String");
         }

         try {
            method.invoke(obj, new Object[] { argument });
         } catch (Exception e) {
            throw new RuntimeException("Error when invoking method " + method.getName() + " of " + obj, e);
         }
      }
   }

   /**
    * Map an argument value from a list string value
    * 
    * Current support parameter type are:
    * <ul>
    * <li> Primitive type (byte,char,short,int,long,float,double,boolean) and
    * its class</li>
    * <li> java.lang.String </li>
    * <li> java.io.File </li>
    * <li> java.net.URL</li>
    * <li> java.util.TimeZone</li>
    * <li> Array of above</li>
    * <li> Collection of java.lang.String</li>
    * <li> Set of java.lang.String</li>
    * <li> Iterator of java.lang.String</li>
    * </ul>
    */
   private static Object getArgument(Class type, List<?> values) {
      int size = values.size();
      Object arg;

      if (type.isArray()) {
         Class elementType = type.getComponentType();
         Object array = Array.newInstance(elementType, size);

         for (int i = 0; i < size; i++) {
            String value = (String) values.get(i);

            Array.set(array, i, getArgument(elementType, value));
         }

         arg = array;
      } else if (type.isPrimitive()) {
         String value = (String) values.get(0);

         arg = getArgument(type, value);
      } else if (type.isInterface()) {
         if (Set.class.isAssignableFrom(type)) {
            Set<Object> set = new HashSet<Object>(values.size() * 3 / 2);

            set.addAll(values);
            arg = set;
         } else if (Collection.class.isAssignableFrom(type)) {
            List<Object> list = new ArrayList<Object>(values.size());

            list.addAll(values);
            arg = list;
         } else if (Map.class.isAssignableFrom(type)) {
            arg = (Map) values.get(0);
         } else if (Iterator.class.isAssignableFrom(type)) {
            List<Object> list = new ArrayList<Object>(values.size());

            list.addAll(values);
            arg = list.iterator();
         } else {
            throw new IllegalArgumentException("Unrecognized parameter type(" + type + ")");
         }
      } else {
         String value = (String) values.get(0);

         arg = getArgument(type, value);
      }

      return arg;
   }

   /**
    * Map an argument value from a string value
    * 
    * Current support parameter type are:
    * <ul>
    * <li> Primitive type (byte,char,short,int,long,float,doulbe,boolean) and
    * its class</li>
    * <li> java.lang.String </li>
    * <li> java.io.File </li>
    * <li> java.net.URL</li>
    * <li> java.util.TimeZone</li>
    * </ul>
    */
   private static Object getArgument(Class type, String value) {
      Object arg;

      if (type == String.class) {
         arg = value;
      } else if (type == Integer.class || type == Integer.TYPE) {
         arg = new Integer(value);
      } else if (type == Long.class || type == Long.TYPE) {
         arg = new Long(value);
      } else if (type == Double.class || type == Double.TYPE) {
         arg = new Double(value);
      } else if (type == Float.class || type == Float.TYPE) {
         arg = new Float(value);
      } else if (type == Boolean.class || type == Boolean.TYPE) {
         arg = Boolean.valueOf(value);
      } else if (type == Short.class || type == Short.TYPE) {
         arg = new Short(value);
      } else if (type == Byte.class || type == Byte.TYPE) {
         arg = new Byte(value);
      } else if (type == Character.class || type == Character.TYPE) {
         arg = new Character(value.charAt(0));
      } else if (type == File.class) { // Extended support
         arg = new File(value);
      } else if (type == URL.class) { // Extended support
         try {
            arg = new URL(value);
         } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
         }
      } else if (type == TimeZone.class) { // Extended support
         arg = TimeZone.getTimeZone(value);
      } else {
         throw new IllegalArgumentException("Unrecognized parameter type(" + type + ")");
      }

      return arg;
   }

   private static Method getSetMethod(Class<? extends Object> clazz, String propertyName) {
      String methodName = getSetMethodName(propertyName);
      Map<String, Method> map = s_methodMap.get(clazz);

      if (map == null) {
         map = new HashMap<String, Method>();
         s_methodMap.put(clazz, map);
      }

      Method method = map.get(methodName);

      if (method == null) {
         method = getSetMethod0(clazz, methodName);
         map.put(methodName, method);
      }

      return method;
   }

   /**
    * void setXYZ(ABC abc);
    */
   private static Method getSetMethod0(Class<? extends Object> clazz, String methodName) {
      Method[] methods = clazz.getMethods();
      int size = (methods == null ? 0 : methods.length);

      for (int i = 0; i < size; i++) {
         Method method = methods[i];

         if (method.getReturnType() == Void.TYPE) {
            if (method.getName().equals(methodName) && method.getParameterTypes().length == 1) {
               return method;
            }
         }
      }

      throw new RuntimeException("Public set method " + methodName + " is expected in " + clazz);
   }

   private static String getSetMethodName(String name) {
      StringBuffer sb = new StringBuffer(32);
      int len = name.length();
      boolean capital = true;

      synchronized (sb) {
         sb.append("set");

         for (int i = 0; i < len; i++) {
            char ch = name.charAt(i);

            if (capital) {
               sb.append(Character.toUpperCase(ch));
               capital = false;
            } else if (ch == '-' || ch == '_') {
               capital = true;
            } else {
               sb.append(ch);
            }
         }
      }

      return sb.toString();
   }

}
