package com.ebay.eunit.handler.junit;

import java.lang.reflect.Method;

import org.junit.Test;

import com.ebay.eunit.annotation.ExpectedException;
import com.ebay.eunit.model.entity.EunitException;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;
import com.ebay.eunit.testfwk.spi.IClassContext.IEunitContext;

public enum TestHandler implements IAnnotationHandler<Test, Method> {
   INSTANCE;
   
   @Override
   public Class<Test> getTargetAnnotation() {
      return Test.class;
   }

   @Override
   public void handle(IClassContext context, Test meta, Method method) {
      IEunitContext ctx = context.forEunit();
      EunitMethod eunitMethod = ctx.peek();

      eunitMethod.setTest(true);

      if (meta.expected() != Test.None.class) {
         if (eunitMethod.getExpectedExceptions().isEmpty()) {
            EunitException exception = new EunitException();

            exception.setType(meta.expected());
            eunitMethod.getExpectedExceptions().add(exception);
         } else {
            throw new RuntimeException(String.format("Method(%s) of class(%s) can't be annotated with both @%s and @%s!",
                  method.getName(), context.getTestClass().getName(), Test.class.getName(), ExpectedException.class.getName()));
         }
      }

      if (meta.timeout() > 0) {
         eunitMethod.setTimeout(meta.timeout());
      }
   }

   @Override
   public boolean isAfter() {
      return false;
   }

   @Override
   public String toString() {
      return String.format("%s.%s", getClass().getSimpleName(), name());
   }
}