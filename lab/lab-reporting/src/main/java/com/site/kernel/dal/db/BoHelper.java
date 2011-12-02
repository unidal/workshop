package com.site.kernel.dal.db;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class BoHelper {
   public static <S extends DataRow, T extends BaseBo> List<T> wrap(List<S> rows, Class<T> boClass) {
      List<T> bos = new ArrayList<T>(rows.size());

      for (S row : rows) {
         bos.add(wrap(row, boClass));
      }

      return bos;
   }

   public static <S extends DataRow, T extends BaseBo> T wrap(S row, Class<T> boClass) {
      try {
         Constructor<T> c = getBoConstructor(boClass);

         return c.newInstance(new Object[] { row });
      } catch (Exception e) {
         throw new RuntimeException("Can't create a BO instance from " + boClass, e);
      }
   }

   @SuppressWarnings("unchecked")
   private static <T extends BaseBo> Constructor<T> getBoConstructor(Class<T> boClass) {
      try {
         Constructor<T>[] cs = (Constructor<T>[]) boClass.getConstructors();

         for (int i = 0; i < cs.length; i++) {
            Class[] paramTypes = cs[i].getParameterTypes();

            if (paramTypes.length == 1 && DataRow.class.isAssignableFrom(paramTypes[0])) {
               return cs[i];
            }
         }
      } catch (Exception e) {
         // ignore it
      }

      throw new RuntimeException("Can't find a constructor for creating a BO instance from " + boClass);
   }

   @SuppressWarnings("unchecked")
   public static <S extends DataRow, T extends BaseBo> S unwrap(T bo, Class<S> doClass) {
      return (S) bo.getDo();
   }

   public static <S extends DataRow, T extends BaseBo> List<S> unwrap(List<T> bos, Class<S> doClass) {
      List<S> dos = new ArrayList<S>(bos.size());

      for (T bo : bos) {
         dos.add(unwrap(bo, doClass));
      }

      return dos;
   }
}
