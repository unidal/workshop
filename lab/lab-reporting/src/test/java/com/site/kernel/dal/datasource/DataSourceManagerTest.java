package com.site.kernel.dal.datasource;

import java.util.Stack;

import com.site.kernel.Module;
import com.site.kernel.common.BaseTestCase;
import com.site.kernel.initialization.BaseModule;

public class DataSourceManagerTest extends BaseTestCase {
   public void testLoad() {
      Module.FULL.doInitialization(null, new Stack<BaseModule>(), true);
   }
}
