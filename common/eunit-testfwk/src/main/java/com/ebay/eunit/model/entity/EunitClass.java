package com.ebay.eunit.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.ebay.eunit.model.BaseEntity;
import com.ebay.eunit.model.IVisitor;

public class EunitClass extends BaseEntity<EunitClass> {
   private Class<?> m_type;

   private Boolean m_ignored;

   private List<java.lang.annotation.Annotation> m_annotations = new ArrayList<java.lang.annotation.Annotation>();

   private List<String> m_groups = new ArrayList<String>();

   private List<EunitField> m_fields = new ArrayList<EunitField>();

   private List<EunitMethod> m_methods = new ArrayList<EunitMethod>();

   public EunitClass() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitEunitClass(this);
   }

   public EunitClass addAnnotation(java.lang.annotation.Annotation annotation) {
      m_annotations.add(annotation);
      return this;
   }

   public EunitClass addField(EunitField field) {
      m_fields.add(field);
      return this;
   }

   public EunitClass addMethod(EunitMethod method) {
      m_methods.add(method);
      return this;
   }

   public EunitClass addGroup(String group) {
      m_groups.add(group);
      return this;
   }

   public EunitField findField(String name) {
      for (EunitField field : m_fields) {
         if (!field.getName().equals(name)) {
            continue;
         }

         return field;
      }

      return null;
   }

   public EunitMethod findMethod(String name) {
      for (EunitMethod method : m_methods) {
         if (!method.getName().equals(name)) {
            continue;
         }

         return method;
      }

      return null;
   }

   @SuppressWarnings("unchecked")
   public <T extends java.lang.annotation.Annotation> T findAnnotation(Class<T> clazz) {
      for (java.lang.annotation.Annotation annotation : m_annotations) {
         if (annotation.annotationType() == clazz) {
            return (T) annotation;
         }
      }

      return null;
   }

   public List<java.lang.annotation.Annotation> getAnnotations() {
      return m_annotations;
   }

   public List<EunitField> getFields() {
      return m_fields;
   }

   public List<EunitMethod> getMethods() {
      return m_methods;
   }

   public List<String> getGroups() {
      return m_groups;
   }

   public Boolean getIgnored() {
      return m_ignored;
   }

   public Class<?> getType() {
      return m_type;
   }

   public boolean isAnnotationPresent(Class<? extends java.lang.annotation.Annotation> clazz) {
      return findAnnotation(clazz) != null;
   }

   public boolean isIgnored() {
      return m_ignored != null && m_ignored.booleanValue();
   }

   @Override
   public void mergeAttributes(EunitClass other) {
      if (other.getType() != null) {
         m_type = other.getType();
      }

      if (other.getIgnored() != null) {
         m_ignored = other.getIgnored();
      }
   }

   public boolean removeField(String name) {
      int len = m_fields.size();

      for (int i = 0; i < len; i++) {
         EunitField field = m_fields.get(i);

         if (!field.getName().equals(name)) {
            continue;
         }

         m_fields.remove(i);
         return true;
      }

      return false;
   }

   public boolean removeMethod(String name) {
      int len = m_methods.size();

      for (int i = 0; i < len; i++) {
         EunitMethod method = m_methods.get(i);

         if (!method.getName().equals(name)) {
            continue;
         }

         m_methods.remove(i);
         return true;
      }

      return false;
   }

   public EunitClass setIgnored(Boolean ignored) {
      m_ignored=ignored;
      return this;
   }

   public EunitClass setType(Class<?> type) {
      m_type=type;
      return this;
   }

}
