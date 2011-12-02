package com.site.kernel.dal;

import java.lang.reflect.Method;

public class DataObjectField {
   private int m_index;

   private String m_name;

   private ValueType m_valueType;

   private Method m_getter; // Get Method, getXyz() or isXyz() or hasXyz()

   private Method m_setter; // Set Method, setXyz(...)

   public DataObjectField(String name, ValueType valueType) {
      m_name = name;
      m_valueType = valueType;
   }

   public Object getFieldValue(Object obj) {
      if (m_getter == null) {
         throw new FieldAccessException("No GET method defined for '" + m_name + "' in " + obj.getClass());
      }

      try {
         return m_getter.invoke(obj, (Object[]) null);
      } catch (Exception e) {
         throw new FieldAccessException("Error invoking method: " + m_getter.getName() + "() of " + obj.getClass(), e);
      }
   }

   public int getIndex() {
      return m_index;
   }

   public String getName() {
      return m_name;
   }

   public ValueType getValueType() {
      return m_valueType;
   }

   public void setFieldValue(Object obj, Object[] args) {
      if (m_setter == null) {
         throw new FieldAccessException("No SET method defined for '" + m_name + "' in " + obj.getClass());
      }

      try {
         m_setter.invoke(obj, args);
      } catch (Exception e) {
         throw new FieldAccessException("Error invoking method: " + m_setter.getName() + "(" + args[0] + ") of " + obj.getClass(), e);
      }
   }

   public void setIndex(int index) {
      m_index = index;
   }

   public void setGetter(Method getter) {
      m_getter = getter;
   }

   public void setSetter(Method setter) {
      m_setter = setter;
   }
}