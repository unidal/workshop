package com.ebay.eunit.cmd.handler;

import java.lang.reflect.Method;

import com.ebay.eunit.cmd.annotation.GET;
import com.ebay.eunit.cmd.annotation.POST;
import com.ebay.eunit.cmd.model.entity.CommandEntity;
import com.ebay.eunit.cmd.model.entity.RequestEntity;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;

public enum GETHandler implements IAnnotationHandler<GET, Method> {
   INSTANCE;

   @Override
   public Class<GET> getTargetAnnotation() {
      return GET.class;
   }

   @Override
   public void handle(IClassContext ctx, GET get, Method method) {
      CommandEntity cmd = ctx.forModel().peek();
      RequestEntity request = new RequestEntity();

      if (cmd.getWithRequest() != null) {
         throw new IllegalStateException(String.format("Only one of %s or %s annotations is allowed for method(%s)!",
               GET.class.getName(), POST.class.getName(), method.getName()));
      }

      cmd.setWithRequest(request);
      request.setRequestMethod("GET");
      request.setRequestUrl(get.value());
   }

   @Override
   public boolean isAfter() {
      return false;
   }
}
