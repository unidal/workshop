package com.ebay.eunit.model.entity;

import static com.ebay.eunit.model.Constants.ATTR_NAME;
import static com.ebay.eunit.model.Constants.ENTITY_EUNIT_METHOD;

import java.util.ArrayList;
import java.util.List;

import com.ebay.eunit.model.BaseEntity;
import com.ebay.eunit.model.IVisitor;

public class EunitMethod extends BaseEntity<EunitMethod> {
   private String m_name;

   private Boolean m_test;

   private Long m_timeout;

   private Boolean m_ignored;

   private Boolean m_beforeAfter;

   private Boolean m_static;

   private Class<?> m_returnType;

   private java.lang.reflect.Method m_method;

   private EunitClass m_eunitClass;

   private List<java.lang.annotation.Annotation> m_annotations = new ArrayList<java.lang.annotation.Annotation>();

   private List<String> m_groups = new ArrayList<String>();

   private List<EunitParameter> m_parameters = new ArrayList<EunitParameter>();

   private List<EunitException> m_expectedExceptions = new ArrayList<EunitException>();

   public EunitMethod(String name) {
      m_name = name;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitEunitMethod(this);
   }

   public EunitMethod addAnnotation(java.lang.annotation.Annotation annotation) {
      m_annotations.add(annotation);
      return this;
   }

   public EunitMethod addExpectedException(EunitException expectedException) {
      m_expectedExceptions.add(expectedException);
      return this;
   }

   public EunitMethod addParameter(EunitParameter parameter) {
      m_parameters.add(parameter);
      return this;
   }

   public EunitMethod addGroup(String group) {
      m_groups.add(group);
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

   public Boolean getBeforeAfter() {
      return m_beforeAfter;
   }

   public EunitClass getEunitClass() {
      return m_eunitClass;
   }

   public List<EunitException> getExpectedExceptions() {
      return m_expectedExceptions;
   }

   public List<EunitParameter> getParameters() {
      return m_parameters;
   }

   public List<String> getGroups() {
      return m_groups;
   }

   public Boolean getIgnored() {
      return m_ignored;
   }

   public java.lang.reflect.Method getMethod() {
      return m_method;
   }

   public String getName() {
      return m_name;
   }

   public Class<?> getReturnType() {
      return m_returnType;
   }

   public Boolean getStatic() {
      return m_static;
   }

   public Boolean getTest() {
      return m_test;
   }

   public Long getTimeout() {
      return m_timeout;
   }

   public boolean isAnnotationPresent(Class<? extends java.lang.annotation.Annotation> clazz) {
      return findAnnotation(clazz) != null;
   }

   public boolean isBeforeAfter() {
      return m_beforeAfter != null && m_beforeAfter.booleanValue();
   }

   public boolean isIgnored() {
      return m_ignored != null && m_ignored.booleanValue();
   }

   public boolean isStatic() {
      return m_static != null && m_static.booleanValue();
   }

   public boolean isTest() {
      return m_test != null && m_test.booleanValue();
   }

   @Override
   public void mergeAttributes(EunitMethod other) {
      assertAttributeEquals(other, ENTITY_EUNIT_METHOD, ATTR_NAME, m_name, other.getName());

      if (other.getTest() != null) {
         m_test = other.getTest();
      }

      if (other.getTimeout() != null) {
         m_timeout = other.getTimeout();
      }

      if (other.getIgnored() != null) {
         m_ignored = other.getIgnored();
      }

      if (other.getBeforeAfter() != null) {
         m_beforeAfter = other.getBeforeAfter();
      }

      if (other.getStatic() != null) {
         m_static = other.getStatic();
      }

      if (other.getReturnType() != null) {
         m_returnType = other.getReturnType();
      }

      if (other.getMethod() != null) {
         m_method = other.getMethod();
      }

      if (other.getEunitClass() != null) {
         m_eunitClass = other.getEunitClass();
      }
   }

   public EunitMethod setBeforeAfter(Boolean beforeAfter) {
      m_beforeAfter=beforeAfter;
      return this;
   }

   public EunitMethod setEunitClass(EunitClass eunitClass) {
      m_eunitClass=eunitClass;
      return this;
   }

   public EunitMethod setIgnored(Boolean ignored) {
      m_ignored=ignored;
      return this;
   }

   public EunitMethod setMethod(java.lang.reflect.Method method) {
      m_method=method;
      return this;
   }

   public EunitMethod setReturnType(Class<?> returnType) {
      m_returnType=returnType;
      return this;
   }

   public EunitMethod setStatic(Boolean _static) {
      m_static=_static;
      return this;
   }

   public EunitMethod setTest(Boolean test) {
      m_test=test;
      return this;
   }

   public EunitMethod setTimeout(Long timeout) {
      m_timeout=timeout;
      return this;
   }

}
