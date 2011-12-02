package com.ebay.eunit.invocation;

import com.ebay.eunit.model.entity.EunitParameter;
import com.ebay.eunit.testfwk.spi.ICaseContext;

public interface IParameterResolver<T extends ICaseContext> {
   public boolean matches(T ctx, EunitParameter eunitParameter);

   public Object resolve(T ctx, EunitParameter eunitParameter);
}
