package com.site.kernel.dal.db;

import java.lang.reflect.Method;
import java.util.List;

import com.site.kernel.dal.DataObjectField;
import com.site.kernel.dal.DefaultDataObjectNaming;
import com.site.kernel.dal.FieldAccessException;

public class DataRowNaming extends DefaultDataObjectNaming {
   public Method getGetterMethod(Class doClass, DataObjectField field) {
      DataRowField f = (DataRowField) field;

      if (f.isSubObject()) {
         if (f.getCardinality().isMany()) {
            String name = normalize("get", f.getName(), "List");
            Method getter = getMethod(doClass, name, null);

            if (getter != null) {
               return getter;
            } else {
               throw new FieldAccessException("GET method (" + name + ") expected for " + doClass);
            }
         } else {
            return super.getGetterMethod(doClass, field);
         }
      } else {
         return super.getGetterMethod(doClass, field);
      }
   }

   public Method getSetterMethod(Class doClass, DataObjectField field) {
      DataRowField f = (DataRowField) field;

      if (f.isSubObject()) {
         if (f.getCardinality().isMany()) {
            Class[] parameterTypes = { List.class };
            String name = normalize("set", f.getName(), "List");
            Method setter = getMethod(doClass, name, parameterTypes);

            if (setter != null) {
               return setter;
            } else {
               throw new FieldAccessException("SET method (" + name + ") expected for " + doClass);
            }
         } else {
            Class[] parameterTypes = { f.getEntity().getDoClass() };
            String name = normalize("set", f.getName(), null);
            Method setter = getMethod(doClass, name, parameterTypes);

            if (setter != null) {
               return setter;
            } else {
               throw new FieldAccessException("SET method (" + name + ") expected for " + doClass);
            }
         }
      } else {
         return super.getSetterMethod(doClass, field);
      }
   }
}
