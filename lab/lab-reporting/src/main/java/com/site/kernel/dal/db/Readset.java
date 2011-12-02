package com.site.kernel.dal.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Readset {
   private Set<Entity> m_involvedEntities;

   private List<DataRowField> m_fields;

   private List<Class> m_doClasses;

   public Readset(DataRowField[] fields) {
      if (fields == null || fields.length == 0) {
         throw new IllegalArgumentException("At least one field is needed for a readset");
      }

      m_fields = Arrays.asList(fields);

      init();
   }

   public Readset(Readset readset1, Readset readset2) {
      this(new Readset[] { readset1, readset2 });
   }

   public Readset(Readset[] rs) {
      if (rs == null || rs.length == 0) {
         throw new IllegalArgumentException("Invalid readset definition");
      }

      ArrayList<DataRowField> list = new ArrayList<DataRowField>();
      for (int i = 0; i < rs.length; i++) {
         list.addAll(rs[i].getFields());
      }

      m_fields = list;
      init();
   }

   public boolean contains(Entity entity) {
      return m_involvedEntities.contains(entity);
   }

   public List<DataRowField> getFields() {
      return m_fields;
   }

   private void init() {
      List<Class> doClasses = new ArrayList<Class>(3);
      Set<Entity> entities = new HashSet<Entity>(5);
      int size = m_fields.size();

      for (int i = 0; i < size; i++) {
         DataRowField field = m_fields.get(i);
         Entity entity = field.getEntity();
         Class declaringClass = entity.getDoClass();

         if (!doClasses.contains(declaringClass)) {
            doClasses.add(declaringClass);
         }

         if (!entities.contains(entity)) {
            entities.add(entity);
         }
      }

      m_involvedEntities = entities;
      m_doClasses = doClasses;
   }

   public boolean matches(Set<Entity> entities) {
      return m_involvedEntities.equals(entities);
   }

   public boolean validate(Entity entity) {
      List<Class> doClasses = entity.getDoClasses();

      return doClasses.containsAll(m_doClasses);
   }
}
