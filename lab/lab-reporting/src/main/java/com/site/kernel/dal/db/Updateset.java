package com.site.kernel.dal.db;

public class Updateset {
   private DataRowField[] m_fields;

   private Class m_doClass;

   public Updateset(DataRowField[] fields) {
      m_fields = fields;

      if (fields == null || fields.length == 0) {
         throw new IllegalArgumentException("At least one field is needed for an updateset");
      }

      initDoClass();
   }

   public DataRowField[] getFields() {
      return m_fields;
   }

   private void initDoClass() {
      Class doClass = null;

      for (int i = 0; i < m_fields.length; i++) {
         Class declaringClass = m_fields[i].getEntity().getDoClass();

         if (doClass == null) {
            doClass = declaringClass;
         } else if (doClass != declaringClass) {
            throw new IllegalArgumentException("All fields of updateset must be in same entity");
         }
      }

      m_doClass = doClass;
   }

   public boolean validate(Entity entity) {
      return m_doClass == entity.getDoClass();
   }
}
