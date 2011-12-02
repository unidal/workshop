package com.site.web.mvc.lifecycle;

import com.site.web.lifecycle.ActionResolver;
import com.site.web.lifecycle.DefaultUrlMapping;
import com.site.web.lifecycle.UrlMapping;
import com.site.web.mvc.model.ErrorModel;
import com.site.web.mvc.model.InboundActionModel;
import com.site.web.mvc.model.ModuleModel;
import com.site.web.mvc.model.OutboundActionModel;
import com.site.web.mvc.model.TransitionModel;
import com.site.web.mvc.payload.ParameterProvider;

public class RequestContext {
   private ParameterProvider m_parameterProvider;

   private UrlMapping m_urlMapping;

   private ModuleModel m_module;

   private InboundActionModel m_inboundAction;

   private OutboundActionModel m_outboundAction;

   private TransitionModel m_transition;

   private ErrorModel m_error;

   private ActionResolver m_actionResolver;

   public String getAction() {
      return m_urlMapping.getAction();
   }

   public ActionResolver getActionResovler() {
      return m_actionResolver;
   }

   public String getActionUri() {
      return m_actionResolver.buildUrl(m_parameterProvider, m_urlMapping);
   }

   public String getActionUri(String action) {
      return getActionUri(action, null);
   }

   public String getActionUri(String action, String pathInfo) {
      DefaultUrlMapping urlMapping = new DefaultUrlMapping(m_urlMapping);

      urlMapping.setAction(action);
      urlMapping.setPathInfo(pathInfo);
      urlMapping.setQueryString(null);
      return m_actionResolver.buildUrl(m_parameterProvider, urlMapping);
   }

   public ErrorModel getError() {
      return m_error;
   }

   public InboundActionModel getInboundAction() {
      return m_inboundAction;
   }

   public ModuleModel getModule() {
      return m_module;
   }

   public OutboundActionModel getOutboundAction() {
      return m_outboundAction;
   }

   public ParameterProvider getParameterProvider() {
      return m_parameterProvider;
   }

   public TransitionModel getTransition() {
      return m_transition;
   }

   public UrlMapping getUrlMapping() {
      return m_urlMapping;
   }

   public void setActionResolver(ActionResolver actionResolver) {
      m_actionResolver = actionResolver;
   }

   public void setError(ErrorModel error) {
      m_error = error;
   }

   public void setInboundAction(InboundActionModel inboundAction) {
      m_inboundAction = inboundAction;
   }

   public void setModule(ModuleModel module) {
      m_module = module;
   }

   public void setOutboundAction(OutboundActionModel outboundAction) {
      m_outboundAction = outboundAction;
   }

   public void setParameterProvider(ParameterProvider parameterProvider) {
      m_parameterProvider = parameterProvider;
   }

   public void setTransition(TransitionModel transition) {
      m_transition = transition;
   }

   public void setUrlMapping(UrlMapping urlMapping) {
      m_urlMapping = urlMapping;
   }
}
