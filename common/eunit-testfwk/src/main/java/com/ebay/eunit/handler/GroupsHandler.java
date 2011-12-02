package com.ebay.eunit.handler;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import com.ebay.eunit.annotation.Groups;
import com.ebay.eunit.model.entity.EunitClass;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;

public enum GroupsHandler implements IAnnotationHandler<Groups, AnnotatedElement> {
   INSTANCE;

   @Override
   public Class<Groups> getTargetAnnotation() {
      return Groups.class;
   }

   @Override
   public void handle(IClassContext ctx, Groups meta, AnnotatedElement target) {
      if (target instanceof Class<?>) {
         EunitClass eunitClass = ctx.forEunit().peek();

         for (String group : meta.value()) {
            eunitClass.addGroup(group);
         }

         for (EunitMethod eunitMethod : eunitClass.getMethods()) {
            if (eunitMethod.isTest()) {
               for (String group : meta.value()) {
                  eunitMethod.addGroup(group);
               }
            }
         }
      } else if (target instanceof Method) {
         EunitMethod eunitMethod = ctx.forEunit().peek();

         for (String group : meta.value()) {
            eunitMethod.addGroup(group);
         }
      } else {
         throw new RuntimeException(String.format("Unsupported annotation(%s) on %s!", Groups.class.getName(), target));
      }
   }

   @Override
   public boolean isAfter() {
      return true;
   }

   @Override
   public String toString() {
      return String.format("%s.%s", getClass().getSimpleName(), name());
   }
}
