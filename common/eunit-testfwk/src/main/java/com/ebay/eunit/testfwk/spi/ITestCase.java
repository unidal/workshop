package com.ebay.eunit.testfwk.spi;

import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.spi.task.ValveMap;

public interface ITestCase<T extends ITestCallback> {
   public EunitMethod getEunitMethod();

   public ValveMap getValveMap();
}
