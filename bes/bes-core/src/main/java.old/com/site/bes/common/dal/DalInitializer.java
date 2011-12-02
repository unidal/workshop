package com.site.bes.common.dal;

import com.site.kernel.dal.db.SimpleTableProvider;
import com.site.kernel.dal.db.helpers.TableProviderManager;

public class DalInitializer {
   public static void initialize(String ds) {
      TableProviderManager m = TableProviderManager.getInstance();
   
      m.register(new SimpleTableProvider(ds, EventEntity.EVENT));
      m.register(new SimpleTableProvider(ds, EventBatchLogEntity.EVENT_BATCH_LOG));
      m.register(new SimpleTableProvider(ds, EventDashboardEntity.EVENT_DASHBOARD));
      m.register(new SimpleTableProvider(ds, EventPlusEntity.EVENT_PLUS));
   }
}
