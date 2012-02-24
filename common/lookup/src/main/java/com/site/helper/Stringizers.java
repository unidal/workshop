package com.site.helper;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.site.helper.Reflects.IMemberFilter;

public class Stringizers {
   public static JsonStringizer forJson() {
      return JsonStringizer.DEFAULT;
   }

   public static enum JsonStringizer {
      DEFAULT(false),

      COMPACT(true);

      private boolean m_compact;

      private JsonStringizer(boolean compact) {
         m_compact = compact;
      }

      public JsonStringizer compact() {
         return COMPACT;
      }

      public String from(Object obj) {
         StringBuilder sb = new StringBuilder(1024);

         fromObject(sb, obj);
         return sb.toString();
      }

      private void fromArray(StringBuilder sb, Object obj) {
         int len = Array.getLength(obj);

         sb.append('[');

         for (int i = 0; i < len; i++) {
            if (i > 0) {
               sb.append(',');

               if (!m_compact) {
                  sb.append(' ');
               }
            }

            Object element = Array.get(obj, i);

            fromObject(sb, element);
         }

         sb.append(']');
      }

      @SuppressWarnings("unchecked")
      private void fromCollection(StringBuilder sb, Object obj) {
         boolean first = true;

         sb.append('[');

         for (Object item : ((Collection<Object>) obj)) {
            if (first) {
               first = false;
            } else {
               sb.append(',');

               if (!m_compact) {
                  sb.append(' ');
               }
            }

            fromObject(sb, item);
         }

         sb.append(']');
      }

      @SuppressWarnings("unchecked")
      private void fromMap(StringBuilder sb, Object obj) {
         boolean first = true;

         sb.append('{');

         for (Map.Entry<Object, Object> e : ((Map<Object, Object>) obj).entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();

            if (value == null) {
               continue;
            }

            if (first) {
               first = false;
            } else {
               sb.append(',');

               if (!m_compact) {
                  sb.append(' ');
               }
            }

            sb.append('"').append(key).append("\":");

            if (!m_compact) {
               sb.append(' ');
            }

            fromObject(sb, value);
         }

         sb.append('}');
      }

      private void fromObject(StringBuilder sb, Object obj) {
         if (obj == null) {
            return;
         }

         Class<?> type = obj.getClass();

         if (type == String.class) {
            sb.append('"').append(obj.toString()).append('"');
         } else if (type.isPrimitive() || Number.class.isAssignableFrom(type)) {
            sb.append(obj.toString());
         } else if (type == Boolean.class) {
            sb.append(obj.toString());
         } else if (type == Date.class) {
            sb.append('"').append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(obj)).append('"');
         } else if (type == Class.class) {
            sb.append('"').append(obj).append('"');
         } else if (type.isArray()) {
            fromArray(sb, obj);
         } else if (Collection.class.isAssignableFrom(type)) {
            fromCollection(sb, obj);
         } else if (Map.class.isAssignableFrom(type)) {
            fromMap(sb, obj);
         } else {
            fromPojo(sb, obj);
         }
      }

      private void fromPojo(StringBuilder sb, Object obj) {
         Class<? extends Object> type = obj.getClass();
         List<Method> getters = Reflects.forMethod().getMethods(type, new IMemberFilter<Method>() {
            @Override
            public boolean filter(Method method) {
               return Reflects.forMethod().isGetter(method);
            }
         });

         Collections.sort(getters, new Comparator<Method>() {
            @Override
            public int compare(Method m1, Method m2) {
               return m1.getName().compareTo(m2.getName());
            }
         });

         if (getters.isEmpty()) {
            // use java toString() since we can't handle it
            sb.append(obj.toString());
         } else {
            boolean first = true;

            sb.append('{');

            for (Method getter : getters) {
               String key = Reflects.forMethod().getGetterName(getter);
               Object value;

               try {
                  value = getter.invoke(obj);
               } catch (Exception e) {
                  // ignore it
                  value = null;
               }

               if (value == null) {
                  continue;
               }

               if (first) {
                  first = false;
               } else {
                  sb.append(',');

                  if (!m_compact) {
                     sb.append(' ');
                  }
               }

               sb.append('"').append(key).append("\":");

               if (!m_compact) {
                  sb.append(' ');
               }

               fromObject(sb, value);
            }

            sb.append('}');
         }
      }
   }
}
