package com.ebay.eunit.testfwk.spi;

import com.ebay.eunit.model.entity.EunitMethod;

public interface ICaseContextFactory {
   public ICaseContext createContext(IClassContext ctx, EunitMethod eunitMethod);
}
