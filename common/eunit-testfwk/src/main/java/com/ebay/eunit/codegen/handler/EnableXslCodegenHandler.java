package com.ebay.eunit.codegen.handler;

import com.ebay.eunit.codegen.EnableXslCodegen;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;
import com.ebay.eunit.testfwk.spi.Registry;

public enum EnableXslCodegenHandler implements IAnnotationHandler<EnableXslCodegen, Class<?>> {
   INSTANCE;

   @Override
   public Class<EnableXslCodegen> getTargetAnnotation() {
      return EnableXslCodegen.class;
   }

   @Override
   public void handle(IClassContext ctx, EnableXslCodegen annonation, Class<?> target) {
      Registry registry = ctx.getRegistry();

      registry.registerAnnotationHandler(XslCodegenHandler.INSTANCE);
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
