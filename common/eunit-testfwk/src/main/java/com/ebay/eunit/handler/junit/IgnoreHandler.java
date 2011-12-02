package com.ebay.eunit.handler.junit;

import java.lang.reflect.AnnotatedElement;

import org.junit.Ignore;

import com.ebay.eunit.model.entity.EunitClass;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;
import com.ebay.eunit.testfwk.spi.IClassContext.IEunitContext;

public enum IgnoreHandler implements IAnnotationHandler<Ignore, AnnotatedElement> {
   INSTANCE;

   @Override
   public Class<Ignore> getTargetAnnotation() {
      return Ignore.class;
   }

   @Override
   public void handle(IClassContext context, Ignore meta, AnnotatedElement annotated) {
      IEunitContext ctx = context.forEunit();
      Object parent = ctx.peek();

      if (parent instanceof EunitClass) {
         EunitClass eunitClass = (EunitClass) parent;

         eunitClass.setIgnored(true);
      } else if (parent instanceof EunitMethod) {
         EunitMethod eunitMethod = (EunitMethod) parent;

         eunitMethod.setIgnored(true);
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
