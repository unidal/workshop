package com.site.kernel;

import com.site.kernel.common.BaseTestCase;
import com.site.kernel.dal.db.DatabaseDataSourceManager;

public class ComponentManagerTest extends BaseTestCase {
	public void testLookupByComponent() {
		DatabaseDataSourceManager ds = (DatabaseDataSourceManager) ComponentManager.lookup(DatabaseDataSourceManager.NAME);

		out("Found DataSourceManager: " + ds.getClass().getName());
	}
}
