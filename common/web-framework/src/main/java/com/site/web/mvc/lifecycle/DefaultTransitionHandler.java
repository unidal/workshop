package com.site.web.mvc.lifecycle;

import static com.site.lookup.util.ReflectUtils.invokeMethod;

import com.dianping.cat.message.MessageProducer;
import com.dianping.cat.message.Transaction;
import com.site.lookup.annotation.Inject;
import com.site.web.mvc.ActionContext;
import com.site.web.mvc.ActionException;
import com.site.web.mvc.model.TransitionModel;

public class DefaultTransitionHandler implements TransitionHandler {
   @Inject
   private MessageProducer m_cat;

   private TransitionModel m_transition;

   public void handle(ActionContext<?> context) throws ActionException {
      Transaction t = m_cat.newTransaction("MVC", "TransitionPhase");

      try {
         invokeMethod(m_transition.getMethod(), m_transition.getModuleInstance(), context);
         t.setStatus(Transaction.SUCCESS);
      } catch (RuntimeException e) {
         String transitionName = m_transition.getTransitionName();

         m_cat.logError(e);
         t.setStatus(e);
         throw new ActionException("Error occured during handling transition(" + transitionName + ")", e);
      } finally {
         t.complete();
      }
   }

   public void initialize(TransitionModel transition) {
      m_transition = transition;
   }
}
