package com.site.kernel.dal.model;

import java.lang.reflect.Method;

import com.site.kernel.dal.Cardinality;
import com.site.kernel.dal.DataObjectField;
import com.site.kernel.dal.DefaultDataObjectNaming;
import com.site.kernel.dal.FieldAccessException;
import com.site.kernel.dal.ValueType;

public class XmlModelNaming extends DefaultDataObjectNaming {
   public Method getGetterMethod(Class doClass, DataObjectField field) {
      XmlModelField xmlField = (XmlModelField) field;
      Cardinality cardinality = xmlField.getCardinality();
      ValueType valueType = field.getValueType();

      if (cardinality == null) {// ATTRIBUTE, ELEMENT
         String name = normalize(null, field.getName(), null);
         Method getter = getMethod(doClass, "get" + name, null);

         if (getter == null && valueType == ValueType.BOOLEAN) {
            getter = getMethod(doClass, "is" + name, null);
         }

         if (getter != null) {
            return getter;
         } else {
            throw new FieldAccessException("GET method (get" + name + ") expected for " + doClass);
         }
      } else if (cardinality.isSingle()) {
         String name = normalize("get", field.getName(), "Do");
         Method setter = getMethod(doClass, name, null);

         if (setter != null) {
            return setter;
         } else {
            throw new FieldAccessException("GET method (" + name + ") expected for " + doClass);
         }
      } else {
         String name = normalize("get", field.getName(), "Dos");
         Method setter = getMethod(doClass, name, null);

         if (setter != null) {
            return setter;
         } else {
            throw new FieldAccessException("GET method (" + name + ") expected for " + doClass);
         }
      }
   }

   public Method getSetterMethod(Class doClass, DataObjectField field) {
      XmlModelField xmlField = (XmlModelField) field;
      Cardinality modelType = xmlField.getCardinality();

      if (modelType == null) { // ATTRIBUTE, ELEMENT
         Class[] parameterTypes = { xmlField.getValueType().getDefinedClass() };
         String name = normalize("set", field.getName(), null);
         Method setter = getMethod(doClass, name, parameterTypes);

         if (setter != null) {
            return setter;
         } else {
            throw new FieldAccessException("SET method (" + name + ") expected for " + doClass);
         }
      } else if (modelType.isSingle()) { // MODEL single object
         Class[] parameterTypes = { xmlField.getModelClass() };
         String name = normalize("set", field.getName(), "Do");
         Method setter = getMethod(doClass, name, parameterTypes);

         if (setter != null) {
            return setter;
         } else {
            throw new FieldAccessException("SET method (" + name + ") expected for " + doClass);
         }
      } else { // MODEL multiple object
         // Nothing for plural models?
         return null;
      }
   }
}
