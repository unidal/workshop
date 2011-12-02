package com.ebay.eunit.handler;

import com.ebay.eunit.annotation.Id;
import com.ebay.eunit.model.entity.EunitParameter;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;
import com.ebay.eunit.testfwk.spi.IClassContext.IEunitContext;
import com.ebay.eunit.testfwk.spi.Parameter;

public enum IdHandler implements IAnnotationHandler<Id, Parameter> {
   INSTANCE;

   @Override
   public Class<Id> getTargetAnnotation() {
      return Id.class;
   }

   @Override
   public void handle(IClassContext context, Id meta, Parameter parameter) {
      IEunitContext ctx = context.forEunit();
      EunitParameter eunitParameter = ctx.peek();

      eunitParameter.setId(meta.value());
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
