package com.site.app.error;

import java.util.Set;

import com.site.app.FieldId;

public interface ErrorObject {
   public void addAttribute(String name, Object value);

   public Object getAttribute(String name);

   public Set<String> getAttributeNames();

   public ErrorId getErrorId();

   public ErrorSeverity getErrorSeverity();

   public Throwable getException();

   public FieldId getFieldId();

   public void setErrorId(ErrorId errorId);

   public void setErrorSeverity(ErrorSeverity errorSeverity);

   public void setException(Throwable throwable);

   public void setFieldId(FieldId fieldId);
}
