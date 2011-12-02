package com.site.wdbc.query.path;

import java.util.Map;

public interface WdbcTagTree {
   public WdbcTagTree addNode(String name);

   public int getCount();

   public String getName();

   public Map<String, ? extends WdbcTagTree> getChildren();

   public void increaseCount();

   public WdbcTagTree getParent();

   public WdbcTagTree getChild(String tagName);

   public void resetCount();

   public void resetChildrenCount();

   public boolean isAutoCreate();
}
