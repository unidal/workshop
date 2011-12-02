package com.ebay.eunit.invocation;

import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.spi.ICaseContext;

public interface IMethodInvoker {
   public <T> T invoke(ICaseContext ctx, EunitMethod eunitMethod) throws Throwable;
}