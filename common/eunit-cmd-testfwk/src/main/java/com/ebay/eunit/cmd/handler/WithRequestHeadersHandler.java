package com.ebay.eunit.cmd.handler;

import java.lang.reflect.Method;

import com.ebay.eunit.cmd.annotation.GET;
import com.ebay.eunit.cmd.annotation.POST;
import com.ebay.eunit.cmd.annotation.WithRequestHeaders;
import com.ebay.eunit.cmd.model.entity.CommandEntity;
import com.ebay.eunit.cmd.model.entity.HeaderEntity;
import com.ebay.eunit.cmd.model.entity.RequestEntity;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;

public enum WithRequestHeadersHandler implements IAnnotationHandler<WithRequestHeaders, Method> {
   INSTANCE;

   @Override
   public Class<WithRequestHeaders> getTargetAnnotation() {
      return WithRequestHeaders.class;
   }

   @Override
   public void handle(IClassContext ctx, WithRequestHeaders headers, Method method) {
      CommandEntity cmd = ctx.forModel().peek();
      RequestEntity request = cmd.getWithRequest();

      if (request == null) {
         throw new IllegalStateException(String.format("Method(%s) should be annotated by either %s or %s!", method.getName(),
               GET.class.getName(), POST.class.getName()));
      }

      String[] names = headers.names();
      String[] values = headers.values();

      if (names.length != values.length) {
         throw new RuntimeException(String.format(
               "Length of attribute names and values of %s of method(%s) should be same, but was %s and %s!",
               WithRequestHeaders.class.getName(), method.getName(), names.length, values.length));
      } else {
         int len = names.length;

         for (int i = 0; i < len; i++) {
            String name = names[i];
            String value = values[i];
            HeaderEntity header = request.findHeader(name);

            if (header == null) {
               header = new HeaderEntity(name);
               request.addHeader(header);
            }

            header.addValue(value);
         }
      }
   }

   @Override
   public boolean isAfter() {
      return false;
   }
}
