package com.site.web.mvc.lifecycle;

import com.site.web.mvc.ActionContext;
import com.site.web.mvc.ActionException;
import com.site.web.mvc.model.OutboundActionModel;

public interface OutboundActionHandler {
   public void handle(ActionContext<?> context) throws ActionException;

   public void initialize(OutboundActionModel outboundAction);
}
