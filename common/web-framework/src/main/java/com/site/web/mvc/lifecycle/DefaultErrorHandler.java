package com.site.web.mvc.lifecycle;

import static com.site.lookup.util.ReflectUtils.invokeMethod;

import com.site.web.mvc.ActionContext;
import com.site.web.mvc.model.ErrorModel;

public class DefaultErrorHandler implements ErrorHandler {
   private ErrorModel m_error;

   public void handle(ActionContext<?> context, Throwable cause) {
      context.setException(cause);
      invokeMethod(m_error.getMethod(), m_error.getModuleInstance(), context);
      context.setException(null);
   }

   public void initialize(ErrorModel error) {
      m_error = error;
   }
}
