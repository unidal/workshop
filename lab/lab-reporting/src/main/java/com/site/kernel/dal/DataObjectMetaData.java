package com.site.kernel.dal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.site.kernel.SystemRegistry;

public class DataObjectMetaData {
   private Class m_doClass;

   private DataObjectNaming m_naming;

   private Map<String, DataObjectField> m_map;

   private List<DataObjectField> m_fields;

   public DataObjectMetaData(DataObjectNaming naming, Class doClass) {
      m_doClass = doClass;
      m_naming = (naming == null ? new DefaultDataObjectNaming() : naming);
      m_map = new HashMap<String, DataObjectField>();
      m_fields = new ArrayList<DataObjectField>();
   }

   public DataObjectField getField(String fieldName) {
      return m_map.get(fieldName);
   }

   public List<DataObjectField> getFields() {
      return m_fields;
   }

   public void initialize(Class declaringClass) {
      Field[] fields = declaringClass.getFields();
      int index = 0;

      for (int i = 0; i < fields.length; i++) {
         Field field = fields[i];

         // It must be static
         if (!Modifier.isStatic(field.getModifiers())) {
            continue;
         }

         // Defined as DataObjectField or its sub-class type
         if (!DataObjectField.class.isAssignableFrom(field.getType())) {
            continue;
         }

         field.setAccessible(true);

         try {
            DataObjectField f = (DataObjectField) field.get(null);

            f.setIndex(index++);
            f.setGetter(m_naming.getGetterMethod(m_doClass, f));
            f.setSetter(m_naming.getSetterMethod(m_doClass, f));

            m_fields.add(f);
            m_map.put(f.getName(), f);
         } catch (RuntimeException e) {
            throw e;
         } catch (IllegalAccessException e) {
            throw new FieldAccessException("Can't access filed: " + field.getName(), e);
         }
      }

      SystemRegistry.register(DataObjectMetaData.class, m_doClass, this);
   }

   public void setFieldValue(DataObject obj, DataObjectField field, Object fieldValue) {
      setFieldValue(obj, field.getName(), fieldValue);
   }

   public void setFieldValue(DataObject obj, String fieldName, Object fieldValue) {
      DataObjectField field = (DataObjectField) m_map.get(fieldName);

      if (field == null) {
         throw new FieldAccessException("No Field(" + fieldName + ") initialized in " + m_doClass);
      } else {
         field.setFieldValue(obj, new Object[] { fieldValue });
      }
   }
}