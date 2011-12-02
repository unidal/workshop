package com.site.kernel.dal.model;

import java.util.HashMap;
import java.util.Map;

import com.site.kernel.util.ClazzLoader;

public class ModelRegistry {
   private static final Map<Class, Class> s_modelMap = new HashMap<Class, Class>();

   @SuppressWarnings("unchecked")
   public static void register(Class modelClass, Class bizClass) {
      try {
         ClazzLoader.loadClass(bizClass.getName());
      } catch (ClassNotFoundException e) {
         throw new RuntimeException(e);
      }

      if (modelClass == bizClass) {
         // same class, no need to register
         return;
      }

      if (modelClass.isAssignableFrom(bizClass)) {
         s_modelMap.put(modelClass, bizClass);
      } else {
         throw new IllegalArgumentException(bizClass + " should be subclass of " + modelClass);
      }
   }

   public static Object newInstance(Class modelClass) {
      Class bizClass = s_modelMap.get(modelClass);

      if (bizClass == null) {
         bizClass = modelClass;
      }

      try {
         return bizClass.newInstance();
      } catch (RuntimeException e) {
         throw e;
      } catch (Exception e) {
         throw new RuntimeException(bizClass + " should have an empty argument constructor");
      }
   }
}
