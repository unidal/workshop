package com.ebay.eunit.testfwk.spi.task;

import com.ebay.eunit.testfwk.spi.ICaseContext;

public interface ITaskExecutor<T extends ITaskType> {
   public void execute(ICaseContext ctx) throws Throwable;

   public T getTaskType();
}