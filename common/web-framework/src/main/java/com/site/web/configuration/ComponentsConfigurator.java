package com.site.web.configuration;

import java.util.ArrayList;
import java.util.List;

import com.dianping.cat.message.MessageProducer;
import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;
import com.site.web.lifecycle.ActionResolver;
import com.site.web.lifecycle.DefaultActionResolver;
import com.site.web.lifecycle.RequestLifecycle;
import com.site.web.mvc.lifecycle.ActionHandlerManager;
import com.site.web.mvc.lifecycle.DefaultActionHandlerManager;
import com.site.web.mvc.lifecycle.DefaultErrorHandler;
import com.site.web.mvc.lifecycle.DefaultInboundActionHandler;
import com.site.web.mvc.lifecycle.DefaultOutboundActionHandler;
import com.site.web.mvc.lifecycle.DefaultRequestLifecycle;
import com.site.web.mvc.lifecycle.DefaultTransitionHandler;
import com.site.web.mvc.lifecycle.ErrorHandler;
import com.site.web.mvc.lifecycle.InboundActionHandler;
import com.site.web.mvc.lifecycle.OutboundActionHandler;
import com.site.web.mvc.lifecycle.TransitionHandler;
import com.site.web.mvc.model.AnnotationMatrix;
import com.site.web.mvc.model.ModelManager;
import com.site.web.mvc.model.ModuleRegistry;
import com.site.web.mvc.payload.DefaultPayloadProvider;
import com.site.web.mvc.payload.MultipartParameterProvider;
import com.site.web.mvc.payload.ParameterProvider;
import com.site.web.mvc.payload.UrlEncodedParameterProvider;

class ComponentsConfigurator extends AbstractResourceConfigurator {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(AnnotationMatrix.class).is(PER_LOOKUP));
      all.add(C(ModelManager.class).req(ModuleRegistry.class, AnnotationMatrix.class));
      all.add(C(ActionResolver.class, DefaultActionResolver.class));
      all.add(C(InboundActionHandler.class, DefaultInboundActionHandler.class).is(PER_LOOKUP) //
            .req(MessageProducer.class));
      all.add(C(OutboundActionHandler.class, DefaultOutboundActionHandler.class).is(PER_LOOKUP) //
            .req(MessageProducer.class));
      all.add(C(TransitionHandler.class, DefaultTransitionHandler.class).is(PER_LOOKUP) //
            .req(MessageProducer.class));
      all.add(C(ErrorHandler.class, DefaultErrorHandler.class));
      all.add(C(DefaultPayloadProvider.class));
      all.add(C(ActionHandlerManager.class, DefaultActionHandlerManager.class));
      all.add(C(RequestLifecycle.class, "mvc", DefaultRequestLifecycle.class) //
            .req(ModelManager.class, ActionHandlerManager.class, MessageProducer.class));

      all.add(C(ParameterProvider.class, "application/x-www-form-urlencoded", UrlEncodedParameterProvider.class) //
            .is(PER_LOOKUP));
      all.add(C(ParameterProvider.class, "multipart/form-data", MultipartParameterProvider.class) //
            .is(PER_LOOKUP));

      return all;
   }

   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new ComponentsConfigurator());
   }
}
