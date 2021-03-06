package com.ebay.eunit.handler.testng;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.testng.annotations.BeforeMethod;

import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;
import com.ebay.eunit.testfwk.spi.IClassContext.IEunitContext;

public enum BeforeMethodHandler implements IAnnotationHandler<BeforeMethod, Method> {
   INSTANCE;
   
   @Override
   public Class<BeforeMethod> getTargetAnnotation() {
      return BeforeMethod.class;
   }

   @Override
   public void handle(IClassContext context, BeforeMethod meta, Method method) {
      int modifier = method.getModifiers();

      if (Modifier.isPublic(modifier)) {
         IEunitContext ctx = context.forEunit();
         EunitMethod eunitMethod = ctx.peek();

         eunitMethod.setBeforeAfter(Boolean.TRUE);
      } else {
         throw new RuntimeException(String.format("Method %s() should be public.", method.getName()));
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
