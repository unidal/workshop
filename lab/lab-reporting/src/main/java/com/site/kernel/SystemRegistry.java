package com.site.kernel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.site.kernel.logging.Log;

public class SystemRegistry {
   private static final Log s_log = Log.getLog(SystemRegistry.class);

   // KEY type => ELEMENT Map (KEY entry => object)
   private static Map<Class, Map<Object, Object>> s_typeMap = new HashMap<Class, Map<Object, Object>>();

   public static synchronized void clear(Class type) {
      if (s_typeMap.containsKey(type)) {
         s_typeMap.remove(type);
         s_log.warn("Clear all registered info for " + type.getName());
      }
   }

   public static CompositeKey key(Object o1, Object o2) {
      return new CompositeKey(o1, o2);
   }

   public static CompositeKey key(Object o1, Object o2, Object o3) {
      return new CompositeKey(o1, o2, o3);
   }

   public static CompositeKey key(Object o1, Object o2, Object o3, Object o4) {
      return new CompositeKey(o1, o2, o3, o4);
   }

   public static Object lookup(Class type, Object entry) {
      return lookup(type, entry, false);
   }

   public static Object lookup(Class type, Object entry, boolean nullForNotFound) {
      if (type == null) {
         throw new NullPointerException("type is null");
      } else if (entry == null) {
         throw new NullPointerException("entry is null");
      }

      Map map = s_typeMap.get(type);
      Object object = (map == null ? null : map.get(entry));

      if (object == null && !nullForNotFound) {
         throw new RuntimeException(type.getName() + "(" + entry + ") has not been registered in SystemRegistry yet");
      }

      return object;
   }

   public static Object newInstance(Class type, Object entry) {
      Object object = lookup(type, entry);

      if (object.getClass() != Class.class) {
         throw new RuntimeException(type.getName() + "(" + entry + ") must be registered as Class in SystemRegistry");
      }

      try {
         return ((Class) object).newInstance();
      } catch (Exception e) {
         throw new RuntimeException("Error creating new instance of " + object, e);
      }
   }

   public static synchronized void register(Class<?> type, Object entry, Object object) {
      if (type == null) {
         throw new NullPointerException("type is null");
      } else if (entry == null) {
         throw new NullPointerException("entry is null");
      } else if (object != null) {
         if (object.getClass() == Class.class && !type.isAssignableFrom((Class) object)) {
            throw new IllegalArgumentException(object + " must be subclass of " + type);
         } else if (object.getClass() != Class.class && !type.isInstance(object)) {
            throw new IllegalArgumentException(object + " must be an instance of " + type);
         }
      }

      Map<Object, Object> map = s_typeMap.get(type);
      if (map == null) {
         map = new HashMap<Object, Object>();
         s_typeMap.put(type, map);
      }

      if (object == null) {
         // Unregister it if object is null
         map.remove(entry);
         s_log.warn("Unregister " + type.getName() + "(" + entry + ")");
      } else {
         Object original = map.get(entry);

         if (original == null) {
            map.put(entry, object);
//            s_log.debug("Register " + type.getName() + "(" + entry + ")");
         } else if (original.equals(object)) {
            s_log.warn(type.getName() + "(" + entry + ") is unregistered in SystemRegistry");
         } else {
            s_log.warn(type.getName() + "(" + entry + ") is unregistered with different object in SystemRegistry. original: " + original + ", current: " + object);
         }
      }
   }

   private static final class CompositeKey {
      private Object[] m_elements;

      public CompositeKey(Object o1, Object o2) {
         this(new Object[] { o1, o2 });
      }

      public CompositeKey(Object o1, Object o2, Object o3) {
         this(new Object[] { o1, o2, o3 });
      }

      public CompositeKey(Object o1, Object o2, Object o3, Object o4) {
         this(new Object[] { o1, o2, o3, o4 });
      }

      private CompositeKey(Object[] objs) {
         m_elements = objs;
      }

      public int hashCode() {
         return Arrays.hashCode(m_elements);
      }

      public boolean equals(Object another) {
         if (another instanceof Object[]) {
            return Arrays.equals(m_elements, (Object[]) another);
         } else {
            return false;
         }
      }
   }
}
