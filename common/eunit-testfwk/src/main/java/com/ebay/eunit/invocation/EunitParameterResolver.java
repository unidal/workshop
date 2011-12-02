package com.ebay.eunit.invocation;

import com.ebay.eunit.model.entity.EunitClass;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.model.entity.EunitParameter;
import com.ebay.eunit.testfwk.CaseContext;
import com.ebay.eunit.testfwk.spi.ICaseContext;
import com.ebay.eunit.testfwk.spi.IClassContext;

public enum EunitParameterResolver implements IParameterResolver<CaseContext> {
   INSTANCE;
   
   @Override
   public boolean matches(final CaseContext ctx, final EunitParameter parameter) {
      final Class<?> type = parameter.getType();

      return type == EunitClass.class || type == EunitMethod.class || type == ICaseContext.class || type == IClassContext.class;
   }

   @Override
   public Object resolve(final CaseContext ctx, final EunitParameter parameter) {
      final Class<?> type = parameter.getType();

      if (type == EunitClass.class) {
         return ctx.getEunitClass();
      } else if (type == EunitMethod.class) {
         return ctx.getEunitMethod();
      } else if (type == ICaseContext.class) {
         return ctx;
      } else if (type == IClassContext.class) {
         return ctx.getClassContext();
      }

      return null;
   }
}
