package com.ebay.eunit.invocation;

import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.CaseContext;
import com.ebay.eunit.testfwk.spi.ICaseContext;
import com.ebay.eunit.testfwk.spi.ICaseContextFactory;
import com.ebay.eunit.testfwk.spi.IClassContext;

public class EunitCaseContextFactory implements ICaseContextFactory {
   @Override
   public ICaseContext createContext(IClassContext ctx, EunitMethod eunitMethod) {
      return new CaseContext(ctx, eunitMethod);
   }
}
