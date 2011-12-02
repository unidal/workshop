package com.site.app.uitl;

import java.util.ArrayList;
import java.util.List;

public class Lists {
   public static <T> List<T> intersection(List<T> list1, List<T> list2, Factor<T> factor) {
      List<T> result = new ArrayList<T>();
      List<Object> ids = new ArrayList<Object>(list2.size());

      for (T e : list2) {
         ids.add(factor.getId(e));
      }

      for (int i = 0; i < list1.size(); i++) {
         T item = list1.get(i);
         Object id = factor.getId(item);
         int index = ids.indexOf(id);

         if (index >= 0) {
            result.add(factor.merge(item, list2.get(index)));
         }
      }

      return result;
   }

   public static <T> void segregate(List<T> newList, List<T> oldList, List<T> insert, List<T> update, List<T> delete,
         Factor<T> factor) {
      List<Object> oldIds = new ArrayList<Object>(oldList.size());

      for (T e : oldList) {
         oldIds.add(factor.getId(e));
      }

      delete.addAll(oldList);

      for (T newItem : newList) {
         Object id = factor.getId(newItem);
         int index = oldIds.indexOf(id);

         if (index < 0) {
            insert.add(newItem);
         } else {
            T oldItem = delete.remove(index);

            oldIds.remove(index);
            update.add(factor.merge(newItem, oldItem));
         }
      }
   }

   public static interface Factor<T> {
      public Object getId(T object);

      public T merge(T newItem, T oldItem);
   }
}
