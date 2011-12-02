package com.ebay.eunit.testfwk.spi.task;

import com.ebay.eunit.testfwk.spi.ICaseContext;

public interface IValve<T extends ICaseContext> {
   public void execute(T ctx, IValveChain chain) throws Throwable;
}
