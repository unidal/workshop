package com.site.web.mvc.lifecycle;

import com.site.web.mvc.model.ErrorModel;
import com.site.web.mvc.model.InboundActionModel;
import com.site.web.mvc.model.ModuleModel;
import com.site.web.mvc.model.OutboundActionModel;
import com.site.web.mvc.model.TransitionModel;

public interface ActionHandlerManager {
   public ErrorHandler getErrorHandler(ModuleModel module, ErrorModel error);

   public InboundActionHandler getInboundActionHandler(ModuleModel module, InboundActionModel inboundAction);

   public OutboundActionHandler getOutboundActionHandler(ModuleModel module, OutboundActionModel outboundAction);

   public TransitionHandler getTransitionHandler(ModuleModel module, TransitionModel transition);
}
