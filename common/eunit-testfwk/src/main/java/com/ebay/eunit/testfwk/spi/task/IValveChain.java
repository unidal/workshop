package com.ebay.eunit.testfwk.spi.task;

import com.ebay.eunit.testfwk.spi.ICaseContext;

public interface IValveChain {
   public void executeNext(ICaseContext ctx) throws Throwable;
}
