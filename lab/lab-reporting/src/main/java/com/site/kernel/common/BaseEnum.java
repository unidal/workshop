package com.site.kernel.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BaseEnum {
   private static EnumManager s_manager = new EnumManager();

   private int m_id;

   private String m_name;

   protected BaseEnum(int id, String name) {
      m_id = id;
      m_name = name;

      s_manager.addEnum(this);
   }

   public static final List getAll(Class clz) {
      return s_manager.getAll(clz);
   }

   public static final BaseEnum getById(Class clz, int id) {
      return s_manager.getById(clz, id);
   }

   public static final BaseEnum getByName(Class clz, String name) {
      return s_manager.getByName(clz, name);
   }

   public final int getId() {
      return m_id;
   }

   public final String getName() {
      return m_name;
   }

   public final int hashCode() {
      return m_id;
   }

   public final String toString() {
      return m_id + "=" + m_name;
   }

   private static final class EnumManager {
      private static final HashMap<Class<? extends BaseEnum>, List<BaseEnum>> s_enumMap = new HashMap<Class<? extends BaseEnum>, List<BaseEnum>>();

      private EnumManager() {
      }

      protected BaseEnum getByName(Class clz, String name) {
         List list = getAll(clz);

         int size = (list == null ? 0 : list.size());
         for (int i = 0; i < size; i++) {
            BaseEnum e = (BaseEnum) list.get(i);

            if (name != null && name.equals(e.getName())) {
               return e;
            }
         }

         return null;
      }

      protected BaseEnum getById(Class clz, int id) {
         List list = getAll(clz);
         int size = (list == null ? 0 : list.size());
         for (int i = 0; i < size; i++) {
            BaseEnum e = (BaseEnum) list.get(i);
            if (e.getId() == id) {
               return e;
            }
         }

         return null;
      }

      protected List getAll(Class clz) {
         return s_enumMap.get(clz);
      }

      public void addEnum(BaseEnum baseEnum) {
         List<BaseEnum> list = s_enumMap.get(baseEnum.getClass());
         if (list == null) {
            list = new ArrayList<BaseEnum>();
            s_enumMap.put(baseEnum.getClass(), list);
         }

         int size = list.size();
         for (int i = 0; i < size; i++) {
            BaseEnum e = list.get(i);
            if (e.getId() == baseEnum.getId()) {
               throw new RuntimeException("ID(" + baseEnum.getId() + ") has already been used by " + e + " in " + baseEnum.getClass());
            }
         }

         list.add(baseEnum);
      }
   }
}
