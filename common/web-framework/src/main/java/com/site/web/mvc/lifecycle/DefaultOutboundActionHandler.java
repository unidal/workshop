package com.site.web.mvc.lifecycle;

import static com.site.lookup.util.ReflectUtils.invokeMethod;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.web.mvc.ActionContext;
import com.site.web.mvc.ActionException;
import com.site.web.mvc.model.OutboundActionModel;

public class DefaultOutboundActionHandler implements OutboundActionHandler, LogEnabled {
   private OutboundActionModel m_outboundAction;

   private Logger m_logger;

   public void handle(ActionContext<?> context) throws ActionException {

      try {
         invokeMethod(m_outboundAction.getMethod(), m_outboundAction.getModuleInstance(), context);
      } catch (RuntimeException e) {
         throw new ActionException("Error occured during handling outbound action(" + m_outboundAction.getActionName()
               + ")", e);
      }
   }

   public void initialize(OutboundActionModel outboundAction) {
      m_outboundAction = outboundAction;
      m_logger.debug(getClass().getSimpleName() + " initialized for  " + outboundAction.getActionName());
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }
}
