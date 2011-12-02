package com.site.web.mvc.lifecycle;

import com.site.web.mvc.ActionContext;
import com.site.web.mvc.ActionException;
import com.site.web.mvc.model.TransitionModel;

public interface TransitionHandler {
   public void handle(ActionContext<?> context) throws ActionException;

   public void initialize(TransitionModel transition);
}
