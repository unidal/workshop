package com.ebay.eunit.handler.testng;

import java.lang.reflect.AnnotatedElement;

import org.testng.annotations.Test;

import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;
import com.ebay.eunit.testfwk.spi.IClassContext.IEunitContext;

public enum TestHandler implements IAnnotationHandler<Test, AnnotatedElement> {
   INSTANCE;

   @Override
   public Class<Test> getTargetAnnotation() {
      return Test.class;
   }

   @Override
   public void handle(IClassContext context, Test meta, AnnotatedElement annotated) {
      IEunitContext ctx = context.forEunit();
      EunitMethod eunitMethod = ctx.peek();

      eunitMethod.setTest(true);

      if (!meta.enabled()) {
         eunitMethod.setIgnored(true);
      }

      if (meta.timeOut() > 0) {
         eunitMethod.setTimeout(meta.timeOut());
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