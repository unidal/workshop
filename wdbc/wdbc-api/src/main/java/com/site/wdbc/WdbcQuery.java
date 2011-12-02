package com.site.wdbc;

import com.site.wdbc.query.WdbcContext;
import com.site.wdbc.query.WdbcEventType;
import com.site.wdbc.query.path.WdbcTagTree;

public interface WdbcQuery {
   void handleEvent(WdbcContext context, WdbcResult result,
         WdbcEventType eventType);

   public String getName();

   public void setName(String name);

   public WdbcTagTree buildTagTree();
}
