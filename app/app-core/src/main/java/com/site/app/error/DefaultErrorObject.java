package com.site.app.error;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.site.app.FieldId;

public class DefaultErrorObject implements ErrorObject {
   private ErrorId m_errorId;

   private ErrorSeverity m_errorSeverity;

   private FieldId m_fieldId;

   private Throwable m_exception;

   private Map<String, Object> m_attributes = new LinkedHashMap<String, Object>();

   public void addAttribute(String name, Object value) {
      m_attributes.put(name, value);
   }

   public Object getAttribute(String name) {
      return m_attributes.get(name);
   }

   public Set<String> getAttributeNames() {
      return m_attributes.keySet();
   }

   public ErrorId getErrorId() {
      return m_errorId;
   }

   public ErrorSeverity getErrorSeverity() {
      return m_errorSeverity;
   }

   public Throwable getException() {
      return m_exception;
   }

   public FieldId getFieldId() {
      return m_fieldId;
   }

   public void setErrorId(ErrorId errorId) {
      m_errorId = errorId;
   }

   public void setErrorSeverity(ErrorSeverity errorSeverity) {
      m_errorSeverity = errorSeverity;
   }

   public void setException(Throwable exception) {
      m_exception = exception;
   }

   public void setFieldId(FieldId fieldId) {
      m_fieldId = fieldId;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder(256);

      sb.append("ErrorObject[").append(m_errorId);

      if (m_errorSeverity != null) {
         sb.append(',').append(m_errorSeverity);
      }

      if (m_fieldId != null) {
         sb.append(',').append(m_fieldId);
      }

      if (!m_attributes.isEmpty()) {
         sb.append(",attributes[");
         for (Map.Entry<String, Object> entry : m_attributes.entrySet()) {
            sb.append(entry.getKey()).append("=>");
            sb.append(entry.getValue()).append(';');
         }
         sb.append(']');
      }

      if (m_exception != null) {
         sb.append(',').append(m_exception);
      }

      sb.append(']');

      return sb.toString();
   }
}
