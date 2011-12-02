package com.site.lab.performance;

import java.lang.reflect.Method;

import com.site.lab.performance.model.Case;

public class ExecutionContext {
   private int m_warmups;

   private int m_executions;

   private int m_currentIndex;

   private Method m_method;

   private Object m_currentInstance;

   private Case m_case;

   public Case getCase() {
      return m_case;
   }

   public int getCurrentIndex() {
      return m_currentIndex;
   }

   public Object getCurrentInstance() {
      return m_currentInstance;
   }

   public int getExecutions() {
      return m_executions;
   }

   public Method getMethod() {
      return m_method;
   }

   public int getWarmups() {
      return m_warmups;
   }

   public void reset(Method method) {
      m_method = method;
   }

   public void setCase(Case case1) {
      m_case = case1;
   }

   public void setCurrentIndex(int currentIndex) {
      m_currentIndex = currentIndex;
   }

   public void setCurrentInstance(Object currentInstance) {
      m_currentInstance = currentInstance;
   }

   public void setExecutions(int executions) {
      m_executions = executions;
   }

   public void setWarmups(int warmups) {
      m_warmups = warmups;
   }
}
