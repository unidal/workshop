package com.ebay.eunit.cmd.handler;

import java.lang.reflect.Method;

import com.ebay.eunit.cmd.annotation.GET;
import com.ebay.eunit.cmd.annotation.POST;
import com.ebay.eunit.cmd.model.entity.CommandEntity;
import com.ebay.eunit.cmd.model.entity.RequestEntity;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;

public enum POSTHandler implements IAnnotationHandler<POST, Method> {
   INSTANCE;

   @Override
   public Class<POST> getTargetAnnotation() {
      return POST.class;
   }

   @Override
   public void handle(IClassContext ctx, POST post, Method method) {
      CommandEntity cmd = ctx.forModel().peek();
      RequestEntity request = new RequestEntity();

      if (cmd.getWithRequest() != null) {
         throw new IllegalStateException(String.format("Only one of %s or %s annotations is allowed for method(%s)!",
               GET.class.getName(), POST.class.getName(), method.getName()));
      }

      cmd.setWithRequest(request);
      request.setRequestMethod("POST");
      request.setRequestUrl(post.value());

      if (post.formData().length() > 0) {
         request.setFormData(post.formData());
      }
   }

   @Override
   public boolean isAfter() {
      return false;
   }
}
