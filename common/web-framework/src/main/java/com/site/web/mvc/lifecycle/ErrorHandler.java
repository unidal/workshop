package com.site.web.mvc.lifecycle;

import com.site.web.mvc.ActionContext;
import com.site.web.mvc.model.ErrorModel;

public interface ErrorHandler {
   public void handle(ActionContext<?> context, Throwable cause);

   public void initialize(ErrorModel error);
}
