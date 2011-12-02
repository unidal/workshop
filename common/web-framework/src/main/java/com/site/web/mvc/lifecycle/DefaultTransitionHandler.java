package com.site.web.mvc.lifecycle;

import static com.site.lookup.util.ReflectUtils.invokeMethod;

import com.site.web.mvc.ActionContext;
import com.site.web.mvc.ActionException;
import com.site.web.mvc.model.TransitionModel;

public class DefaultTransitionHandler implements TransitionHandler {
   private TransitionModel m_transition;

   public void handle(ActionContext<?> context) throws ActionException {
      try {
         invokeMethod(m_transition.getMethod(), m_transition.getModuleInstance(), context);
      } catch (RuntimeException e) {
         throw new ActionException(
               "Error occured during handling transition(" + m_transition.getTransitionName() + ")", e);
      }
   }

   public void initialize(TransitionModel transition) {
      m_transition = transition;
   }
}
