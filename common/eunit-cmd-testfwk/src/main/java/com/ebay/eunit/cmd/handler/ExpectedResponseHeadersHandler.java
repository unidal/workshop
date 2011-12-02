package com.ebay.eunit.cmd.handler;

import java.lang.reflect.Method;

import com.ebay.eunit.cmd.annotation.ExpectedResponseHeaders;
import com.ebay.eunit.cmd.model.entity.CommandEntity;
import com.ebay.eunit.cmd.model.entity.HeaderEntity;
import com.ebay.eunit.cmd.model.entity.ResponseEntity;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;

public enum ExpectedResponseHeadersHandler implements IAnnotationHandler<ExpectedResponseHeaders, Method> {
   INSTANCE;

   @Override
   public Class<ExpectedResponseHeaders> getTargetAnnotation() {
      return ExpectedResponseHeaders.class;
   }

   @Override
   public void handle(IClassContext ctx, ExpectedResponseHeaders headers, Method method) {
      CommandEntity cmd = ctx.forModel().peek();
      ResponseEntity response = cmd.getExpectedResponse();

      if (response == null) {
         throw new IllegalStateException(String.format("Method(%s) should be annotated by %s!", method.getName(),
               ExpectedResponseHeaders.class.getName()));
      }

      String[] names = headers.names();
      String[] values = headers.values();

      if (names.length != values.length) {
         throw new RuntimeException(String.format(
               "Length of attribute names and values of annotation(%s) for method(%s) should be same, but was %s and %s!",
               ExpectedResponseHeaders.class.getName(), method.getName(), names.length, values.length));
      } else {
         int len = names.length;

         for (int i = 0; i < len; i++) {
            String name = names[i];
            String value = values[i];
            HeaderEntity header = response.findHeader(name);

            if (header == null) {
               header = new HeaderEntity(name);
               response.addHeader(header);
            }

            if (value.length() > 0) {
               header.addValue(value);
            }
         }
      }
   }

   @Override
   public boolean isAfter() {
      return false;
   }
}
