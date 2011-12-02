package com.ebay.eunit.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.ebay.eunit.model.BaseEntity;
import com.ebay.eunit.model.IVisitor;

public class EunitParameter extends BaseEntity<EunitParameter> {
   private Class<?> m_type;

   private String m_id;

   private Integer m_index;

   private EunitMethod m_eunitMethod;

   private List<java.lang.annotation.Annotation> m_annotations = new ArrayList<java.lang.annotation.Annotation>();

   public EunitParameter() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitEunitParameter(this);
   }

   public EunitParameter addAnnotation(java.lang.annotation.Annotation annotation) {
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

   public EunitMethod getEunitMethod() {
      return m_eunitMethod;
   }

   public String getId() {
      return m_id;
   }

   public Integer getIndex() {
      return m_index;
   }

   public Class<?> getType() {
      return m_type;
   }

   public boolean isAnnotationPresent(Class<? extends java.lang.annotation.Annotation> clazz) {
      return findAnnotation(clazz) != null;
   }

   @Override
   public void mergeAttributes(EunitParameter other) {
      if (other.getType() != null) {
         m_type = other.getType();
      }

      if (other.getId() != null) {
         m_id = other.getId();
      }

      if (other.getIndex() != null) {
         m_index = other.getIndex();
      }

      if (other.getEunitMethod() != null) {
         m_eunitMethod = other.getEunitMethod();
      }
   }

   public EunitParameter setEunitMethod(EunitMethod eunitMethod) {
      m_eunitMethod=eunitMethod;
      return this;
   }

   public EunitParameter setId(String id) {
      m_id=id;
      return this;
   }

   public EunitParameter setIndex(Integer index) {
      m_index=index;
      return this;
   }

   public EunitParameter setType(Class<?> type) {
      m_type=type;
      return this;
   }

}
