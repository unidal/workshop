package com.ebay.eunit.handler.junit;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.BeforeClass;

import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;
import com.ebay.eunit.testfwk.spi.IClassContext.IEunitContext;

public enum BeforeClassHandler implements IAnnotationHandler<BeforeClass, Method> {
   INSTANCE;
   
   @Override
   public Class<BeforeClass> getTargetAnnotation() {
      return BeforeClass.class;
   }

   @Override
   public void handle(IClassContext context, BeforeClass meta, Method method) {
      int modifier = method.getModifiers();

      if (Modifier.isPublic(modifier) && Modifier.isStatic(modifier)) {
         IEunitContext ctx = context.forEunit();
         EunitMethod eunitMethod = ctx.peek();

         eunitMethod.setBeforeAfter(Boolean.TRUE);
         eunitMethod.setStatic(true);
      } else {
         throw new RuntimeException(String.format("Method %s() should be public static.", method.getName()));
      }
   }

   @Override
   public boolean isAfter() {
      return false;
   }

   @Override
   public String toString() {
      return getClass().getSimpleName();
   }
}
