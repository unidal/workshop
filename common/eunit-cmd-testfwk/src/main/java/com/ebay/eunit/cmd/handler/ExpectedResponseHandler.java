package com.ebay.eunit.cmd.handler;

import java.lang.reflect.Method;

import com.ebay.eunit.cmd.annotation.ExpectedResponse;
import com.ebay.eunit.cmd.model.entity.CommandEntity;
import com.ebay.eunit.cmd.model.entity.ResponseEntity;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;

public enum ExpectedResponseHandler implements IAnnotationHandler<ExpectedResponse, Method> {
   INSTANCE;

   @Override
   public Class<ExpectedResponse> getTargetAnnotation() {
      return ExpectedResponse.class;
   }

   @Override
   public void handle(IClassContext ctx, ExpectedResponse res, Method method) {
      CommandEntity cmd = ctx.forModel().peek();
      ResponseEntity response = new ResponseEntity();

      cmd.setExpectedResponse(response);

      response.setCode(res.code());
      response.setContentType(res.contentType());
      response.setContentLength(res.contentLength());
   }

   @Override
   public boolean isAfter() {
      return false;
   }
}
