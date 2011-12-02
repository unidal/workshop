package com.ebay.eunit.cmd.handler;

import java.lang.reflect.Method;

import com.ebay.eunit.cmd.annotation.ExpectedResponseBody;
import com.ebay.eunit.cmd.annotation.ExpectedResponseHeaders;
import com.ebay.eunit.cmd.model.entity.CommandEntity;
import com.ebay.eunit.cmd.model.entity.ResponseEntity;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;

public enum ExpectedResponseBodyHandler implements IAnnotationHandler<ExpectedResponseBody, Method> {
   INSTANCE;

   @Override
   public Class<ExpectedResponseBody> getTargetAnnotation() {
      return ExpectedResponseBody.class;
   }

   @Override
   public void handle(IClassContext ctx, ExpectedResponseBody body, Method method) {
      CommandEntity cmd = ctx.forModel().peek();
      ResponseEntity response = cmd.getExpectedResponse();

      if (response == null) {
         throw new IllegalStateException(String.format("Method(%s) should be annotated by %s!", method.getName(),
               ExpectedResponseHeaders.class.getName()));
      }

      response.setBody(body.value());
   }

   @Override
   public boolean isAfter() {
      return false;
   }
}
