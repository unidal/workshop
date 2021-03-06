package com.site.app.tag.function;

import java.lang.reflect.Method;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;

public class Calculator {
   private static Method getGetter(String fieldName, Class<?> clazz) {
      try {
         return clazz.getMethod("get" + StringUtils.capitalizeFirstLetter(fieldName));
      } catch (Exception e) {
         // ignore it
      }

      try {
         return clazz.getMethod("is" + StringUtils.capitalizeFirstLetter(fieldName));
      } catch (Exception e) {
         // ignore it
      }

      throw new IllegalArgumentException("No getter method for " + fieldName + " in " + clazz);
   }

   public static int max(List<?> list, String fieldName) {
      int max = Integer.MIN_VALUE;

      if (list != null && list.size() > 0) {
         Method method = getGetter(fieldName, list.get(0).getClass());

         if (method.getReturnType().isPrimitive() || Number.class.isAssignableFrom(method.getReturnType())) {
            for (Object item : list) {
               try {
                  Object value = method.invoke(item, new Object[0]);
                  int val = 0;

                  if (value instanceof Boolean) {
                     val = ((Boolean) value).booleanValue() ? 1 : 0;
                  } else if (value instanceof Number) {
                     val = ((Number) value).intValue();
                  }

                  if (val > max) {
                     max = val;
                  }
               } catch (Exception e) {
                  // ignore it
               }
            }
         }
      }

      if (max == Integer.MIN_VALUE) {
         return 0;
      } else {
         return max;
      }
   }

   public static int min(List<?> list, String fieldName) {
      int min = Integer.MAX_VALUE;

      if (list != null && list.size() > 0) {
         Method method = getGetter(fieldName, list.get(0).getClass());

         if (method.getReturnType().isPrimitive() || Number.class.isAssignableFrom(method.getReturnType())) {
            for (Object item : list) {
               try {
                  Object value = method.invoke(item, new Object[0]);
                  int val = 0;

                  if (value instanceof Boolean) {
                     val = ((Boolean) value).booleanValue() ? 1 : 0;
                  } else if (value instanceof Number) {
                     val = ((Number) value).intValue();
                  }

                  if (val < min) {
                     min = val;
                  }
               } catch (Exception e) {
                  // ignore it
               }
            }
         }
      }

      if (min == Integer.MAX_VALUE) {
         return 0;
      } else {
         return min;
      }
   }

   public static int count(List<?> list) {
      return list == null ? 0 : list.size();
   }

   public static double sum(List<?> list, String fieldName) {
      double sum = 0;

      if (list != null && list.size() > 0) {
         Method method = getGetter(fieldName, list.get(0).getClass());

         if (method.getReturnType().isPrimitive() || Number.class.isAssignableFrom(method.getReturnType())) {
            for (Object item : list) {
               try {
                  Object value = method.invoke(item, new Object[0]);

                  if (value instanceof Boolean) {
                     sum += ((Boolean) value).booleanValue() ? 1 : 0;
                  } else if (value instanceof Number) {
                     sum += ((Number) value).doubleValue();
                  }
               } catch (Exception e) {
                  // ignore it
               }
            }
         }
      }

      return sum;
   }
}
