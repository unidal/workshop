package com.ebay.eunit.testfwk;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.ebay.eunit.EunitJUnit4Runner;
import com.ebay.eunit.model.entity.EunitClass;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.spi.ICaseContext;
import com.ebay.eunit.testfwk.spi.IClassContext;

@RunWith(EunitJUnit4Runner.class)
public class InjectionTest {
   @Test
   public void testEunitParameterResolver(ICaseContext ctx, IClassContext classContext, EunitClass eunitClass,
         EunitMethod eunitMethod) {
      Assert.assertNotNull(ctx);
      Assert.assertNotNull(classContext);
      Assert.assertNotNull(eunitClass);
      Assert.assertNotNull(eunitMethod);
   }
}
