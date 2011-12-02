package com.site.kernel.util;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class ReflectUtils {
   /**
    * Map an argument value from a list string value
    * 
    * Current support parameter type are:
    * <ul>
    * <li> Primitive type (byte,char,short,int,long,float,doulbe,boolean) and
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
   public static Object getArgument(Class type, List<Object> values) throws ArrayIndexOutOfBoundsException, MalformedURLException {
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
         if (Collection.class.isAssignableFrom(type)) { // super class of List
            List<Object> list = new ArrayList<Object>(values.size());

            list.addAll(values);
            arg = list;
         } else if (Map.class.isAssignableFrom(type)) {
            arg = (Map) values.get(0);
         } else if (Set.class.isAssignableFrom(type)) {
            Set<Object> set = new HashSet<Object>(values.size() * 3 / 2);

            set.addAll(values);
            arg = set;
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
   public static Object getArgument(Class type, String value) throws MalformedURLException {
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
      } else if (type == Short.class || type == Byte.TYPE) {
         arg = new Short(value);
      } else if (type == Byte.class || type == Integer.TYPE) {
         arg = new Byte(value);
      } else if (type == Character.class || type == Character.TYPE) {
         arg = new Character(value.charAt(0));
      } else if (type == File.class) { // Extended support
         arg = new File(value);
      } else if (type == URL.class) { // Extended support
         arg = new URL(value);
      } else if (type == TimeZone.class) { // Extended support
         arg = TimeZone.getTimeZone(value);
      } else {
         throw new IllegalArgumentException("Unrecognized parameter type(" + type + ")");
      }

      return arg;
   }

   public static final Object getProperty(Object obj, String property) {
      Class clazz = obj.getClass();
      String methodName = normalize("get", property, null);
      try {
         Method method = clazz.getMethod(methodName, (Class[]) null);

         return method.invoke(obj, (Object[]) null);
      } catch (Exception e) {
         throw new RuntimeException("Error getting field: " + methodName + " of " + clazz, e);
      }
   }

   private static String normalize(String prefix, String rawFieldName, String suffix) {
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

   public static final Class loadClass(String className) throws ClassNotFoundException {
      Class clazz = Class.forName(className);

      return clazz;
   }

   public static final Object newInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
      Class clazz = loadClass(className);

      return clazz.newInstance();
   }
}
