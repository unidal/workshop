package com.ebay.eunit.codegen.handler;

import java.lang.reflect.Method;

import com.ebay.eunit.codegen.XslCodegen;
import com.ebay.eunit.codegen.xsl.XslCodegenValve;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;
import com.ebay.eunit.testfwk.spi.task.Priority;

enum XslCodegenHandler implements IAnnotationHandler<XslCodegen, Method> {
   INSTANCE;

   @Override
   public Class<XslCodegen> getTargetAnnotation() {
      return XslCodegen.class;
   }

   @Override
   public void handle(IClassContext ctx, XslCodegen meta, Method method) {
      ctx.getRegistry().registerCaseValve(Priority.MIDDLE, new XslCodegenValve(meta));
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
