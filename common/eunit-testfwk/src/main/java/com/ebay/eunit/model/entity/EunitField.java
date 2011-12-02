package com.ebay.eunit.model.entity;

import static com.ebay.eunit.model.Constants.ATTR_NAME;
import static com.ebay.eunit.model.Constants.ENTITY_EUNIT_FIELD;

import java.util.ArrayList;
import java.util.List;

import com.ebay.eunit.model.BaseEntity;
import com.ebay.eunit.model.IVisitor;

public class EunitField extends BaseEntity<EunitField> {
   private String m_name;

   private Class<?> m_type;

   private java.lang.reflect.Field m_field;

   private EunitClass m_eunitClass;

   private List<java.lang.annotation.Annotation> m_annotations = new ArrayList<java.lang.annotation.Annotation>();

   public EunitField(String name) {
      m_name = name;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitEunitField(this);
   }

   public EunitField addAnnotation(java.lang.annotation.Annotation annotation) {
      m_annotations.add(annotation);
      return this;
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

   public EunitClass getEunitClass() {
      return m_eunitClass;
   }

   public java.lang.reflect.Field getField() {
      return m_field;
   }

   public String getName() {
      return m_name;
   }

   public Class<?> getType() {
      return m_type;
   }

   public boolean isAnnotationPresent(Class<? extends java.lang.annotation.Annotation> clazz) {
      return findAnnotation(clazz) != null;
   }

   @Override
   public void mergeAttributes(EunitField other) {
      assertAttributeEquals(other, ENTITY_EUNIT_FIELD, ATTR_NAME, m_name, other.getName());

      if (other.getType() != null) {
         m_type = other.getType();
      }

      if (other.getField() != null) {
         m_field = other.getField();
      }

      if (other.getEunitClass() != null) {
         m_eunitClass = other.getEunitClass();
      }
   }

   public EunitField setEunitClass(EunitClass eunitClass) {
      m_eunitClass=eunitClass;
      return this;
   }

   public EunitField setField(java.lang.reflect.Field field) {
      m_field=field;
      return this;
   }

   public EunitField setType(Class<?> type) {
      m_type=type;
      return this;
   }

}
