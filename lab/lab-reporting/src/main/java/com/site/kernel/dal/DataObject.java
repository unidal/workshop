package com.site.kernel.dal;

import java.util.BitSet;

import com.site.kernel.SystemRegistry;

public abstract class DataObject {
   private BitSet m_fieldUsages;

   protected DataObject() {
      m_fieldUsages = new BitSet();
   }

   public void clearAllFieldUsed() {
      m_fieldUsages.clear();
   }

   public Object getFieldValue(DataObjectField field) {
      return field.getFieldValue(this);
   }

   public DataObjectMetaData getMetaData() {
      return (DataObjectMetaData) SystemRegistry.lookup(DataObjectMetaData.class, getClass());
   }

   public boolean isFieldUsed(DataObjectField field) {
      return m_fieldUsages.get(field.getIndex());
   }

   protected void setFieldUsed(DataObjectField field, boolean isUsed) {
      if (isUsed) {
         m_fieldUsages.set(field.getIndex());
      } else {
         m_fieldUsages.clear(field.getIndex());
      }
   }

   public void setFieldValue(DataObjectField field, Object value) {
      Object[] args = new Object[1];

      if (value instanceof String) {
         ValueType valueType = field.getValueType();
         String val = (String) value;

         if (valueType == ValueType.STRING) {
            args[0] = val;
         } else if (valueType == ValueType.INT) {
            args[0] = new Integer(val);
         } else if (valueType == ValueType.LONG) {
            args[0] = new Long(val);
         } else if (valueType == ValueType.DOUBLE) {
            args[0] = new Double(val);
         } else if (valueType == ValueType.FLOAT) {
            args[0] = new Float(val);
         } else if (valueType == ValueType.BOOLEAN) {
            if (val.equalsIgnoreCase("true") || val.equalsIgnoreCase("on") || val.equalsIgnoreCase("yes") || val.equalsIgnoreCase("T") || val.equalsIgnoreCase("Y")
                  || val.equalsIgnoreCase("1")) {
               args[0] = Boolean.TRUE;
            } else {
               args[0] = Boolean.FALSE;
            }
         }
      } else {
         args[0] = value;
      }

      field.setFieldValue(this, args);
   }

   protected static final class CallerIntrospector extends SecurityManager {
      private static CallerIntrospector instance = new CallerIntrospector();

      @SuppressWarnings("unchecked")
      public static Class getCaller() {
         Class[] clazzes = instance.getClassContext();
         Class clazz = clazzes[2];

         for (int i = 3; i < clazzes.length; i++) {
            if (clazz.isAssignableFrom(clazzes[i])) {
               clazz = clazzes[i];
            } else {
               break;
            }
         }

         return clazz;
      }
   }
}