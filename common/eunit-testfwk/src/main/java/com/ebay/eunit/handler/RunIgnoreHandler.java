package com.ebay.eunit.handler;

import com.ebay.eunit.annotation.RunIgnore;
import com.ebay.eunit.model.entity.EunitClass;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.spi.IAnnotationHandler;
import com.ebay.eunit.testfwk.spi.IClassContext;
import com.ebay.eunit.testfwk.spi.filter.RunOption;

public enum RunIgnoreHandler implements IAnnotationHandler<RunIgnore, Class<?>> {
   INSTANCE;

   @Override
   public Class<RunIgnore> getTargetAnnotation() {
      return RunIgnore.class;
   }

   @Override
   public void handle(IClassContext context, RunIgnore meta, Class<?> clazz) {
      EunitClass eunitClass = context.forEunit().peek();
      RunOption option = meta.runAll() ? RunOption.ALL_CASES : RunOption.IGNORED_CASES_ONLY;

      switch (option) {
      case IGNORED_CASES_ONLY:
      case ALL_CASES:
         if (eunitClass.isIgnored()) {
            eunitClass.setIgnored(false);
         }

         for (EunitMethod eunitMethod : eunitClass.getMethods()) {
            if (eunitMethod.isTest()) {
               if (eunitMethod.isIgnored()) {
                  eunitMethod.setIgnored(false);
               } else if (option == RunOption.IGNORED_CASES_ONLY) {
                  eunitMethod.setIgnored(true);
               }
            }
         }

         break;
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
