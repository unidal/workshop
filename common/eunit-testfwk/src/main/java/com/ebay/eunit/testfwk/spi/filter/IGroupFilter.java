package com.ebay.eunit.testfwk.spi.filter;

import com.ebay.eunit.model.entity.EunitMethod;

public interface IGroupFilter {
   public boolean matches(EunitMethod eunitMethod);
}