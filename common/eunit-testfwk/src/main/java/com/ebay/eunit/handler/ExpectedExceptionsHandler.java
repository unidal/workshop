package com.ebay.eunit.handler;

import java.lang.reflect.Method;

import org.junit.Test;

import com.ebay.eunit.annotation.ExpectedException;
import com.ebay.eunit.annotation.ExpectedExceptions;
import com.ebay.eunit.model.entity.EunitException;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;
import com.ebay.eunit.testfwk.spi.IClassContext.IEunitContext;

public enum ExpectedExceptionsHandler implements IAnnotationHandler<ExpectedExceptions, Method> {
   INSTANCE;

   @Override
   public Class<ExpectedExceptions> getTargetAnnotation() {
      return ExpectedExceptions.class;
   }

   @Override
   public void handle(IClassContext context, ExpectedExceptions meta, Method method) {
      IEunitContext ctx = context.forEunit();
      EunitMethod eunitMethod = ctx.peek();

      if (eunitMethod.getExpectedExceptions().isEmpty()) {
         for (ExpectedException e : meta.value()) {
            EunitException exception = new EunitException();

            exception.setType(e.type());
            exception.setMessage(e.message());
            exception.setPattern(e.pattern());
            eunitMethod.getExpectedExceptions().add(exception);
         }
      } else {
         throw new RuntimeException(String.format("Method(%s) of class(%s) can't be annotated with both @%s, @%s and @%s!",
               method.getName(), context.getTestClass().getName(), Test.class.getName(), ExpectedException.class.getName(),
               ExpectedExceptions.class.getName()));
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
