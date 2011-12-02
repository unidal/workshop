package com.site.kernel.dal.db;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.site.kernel.dal.DataObject;
import com.site.kernel.dal.DataObjectField;
import com.site.kernel.dal.ValueType;

/**
 * @author qwu
 */
public abstract class DataRow extends DataObject implements Cloneable {
   // Dirty property will be used by UPDATE query
   private BitSet m_memberDirties;

   protected DataRow() {
      super();

      m_memberDirties = new BitSet();
   }

   protected static void initialize(String logicalName, Class entityClass, DataRowField[] hintFields) {
      Class doClass = CallerIntrospector.getCaller();
      DataRowNaming naming = new DataRowNaming();
      DataRowMetaData metadata = new DataRowMetaData(logicalName, naming, doClass, hintFields);

      try {
         metadata.initialize(entityClass);
      } catch (RuntimeException e) {
         e.printStackTrace();
         throw e;
      }
   }

   /**
    * Called just before the object is saved to the database just prior to
    * binding values for INSERT or UPDATE. Override this if you need to provide
    * any custom data manipulation in the object.
    */
   public void afterLoad() {
      // OVERRIDE IT IN SUB-CLASS
   }

   /**
    * Called just after the object is loaded with values from the database.
    * Override this if you need to provide custom loading behavior
    */
   public void beforeSave() {
      // OVERRIDE IT IN SUB-CLASS
   }

   public boolean checkConstraints() {
      DataRowMetaData metadata = (DataRowMetaData) getMetaData();
      List<DataObjectField> fields = metadata.getFields();

      for (DataObjectField f : fields) {
         DataRowField field = (DataRowField) f;

         if (field.isField()) {
            // no data set for NOT_NULL field
            if (!field.isNullable() && !isFieldUsed(field) && !field.hasInsertExpr() && !field.isAutoIncrement()) {
               return false;
            }
         }
      }

      return true;
   }

   public void clearDirties() {
      m_memberDirties.clear();
   }

   @Override
   public DataRowMetaData getMetaData() {
      return (DataRowMetaData) super.getMetaData();
   }

   public Map<String, Object> getQueryHints() {
      DataRowMetaData metadata = (DataRowMetaData) getMetaData();
      DataRowField[] hintFields = metadata.getHintFields();
      int len = (hintFields == null ? 0 : hintFields.length);
      Map<String, Object> hints = new HashMap<String, Object>(len * 2);

      for (int i = 0; i < len; i++) {
         DataRowField field = hintFields[i];
         Object value = field.getFieldValue(this);

         // Only used member will be set to hints
         if (isFieldUsed(field)) {
            hints.put(field.getName(), value);
         }
      }

      return hints;
   }

   private String getShortClassName() {
      String className = getClass().getName();
      int pos = className.lastIndexOf('.');

      if (pos > 0) {
         return className.substring(pos + 1);
      } else {
         return className;
      }
   }

   public boolean isMemberDirty(DataRowField field) {
      return m_memberDirties.get(field.getIndex());
   }

   protected DataRow loadSubObject(Entity entity, Object key) {
      // TODO Auto-generated method stub
      return null;
   }

   protected List loadSubObjects(Entity entity, Object key) {
      // TODO
      return null;
   }

   public void setFieldUsed(DataRowField field, boolean isUsed) {
      super.setFieldUsed(field, isUsed);

      if (isUsed) {
         m_memberDirties.set(field.getIndex());
      } else {
         m_memberDirties.clear(field.getIndex());
      }
   }

   public String toString() {
      StringBuffer sb = new StringBuffer(256);
      boolean first = true;

      sb.append(getShortClassName());
      sb.append('[');

      DataRowMetaData metadata = (DataRowMetaData) getMetaData();
      List<DataObjectField> fields = metadata.getFields();

      for (DataObjectField f : fields) {
         DataRowField field = (DataRowField) f;

         if (!first) {
            sb.append(',');
         }

         first = false;
         sb.append(field.getName()).append('=');

         if (isFieldUsed(field)) {
            Object value = field.getFieldValue(this);

            if (field.getValueType() == ValueType.STRING && value != null) {
               sb.append('"').append(value).append('"');
            } else {
               sb.append(value);
            }
         }
      }

      sb.append(']');
      return sb.toString();
   }
}
