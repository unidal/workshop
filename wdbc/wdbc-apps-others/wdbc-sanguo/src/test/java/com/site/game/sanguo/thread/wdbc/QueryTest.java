package com.site.game.sanguo.thread.wdbc;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.ResourceSource;
import com.site.wdbc.WdbcEngine;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.WdbcSource;
import com.site.wdbc.WdbcSourceType;

public class QueryTest extends ComponentTestCase {
   private String getPath() {
      return getClass().getPackage().getName().toString().replace('.', '/');
   }

   public void testBuildingDetail() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "building_detail");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, getPath() + "/building_detail.html", "utf-8");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(1, result.getRowSize());
   }

   public void testBuildingList() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "building_list");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, getPath() + "/building_list.html", "utf-8");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(22, result.getRowSize());
   }

   public void testCourtList() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "court_list");
      assertNotNull(query);
      
      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, getPath() + "/court_list.html", "utf-8");
      WdbcResult result = engine.execute(query, source);
      
      assertNotNull(result);
      assertEquals(1, result.getRowSize());
   }

   public void testGeneralDetail() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "general_detail");
      assertNotNull(query);
      
      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, getPath() + "/general_detail.html", "utf-8");
      WdbcResult result = engine.execute(query, source);
      
      assertNotNull(result);
      assertEquals(1, result.getRowSize());
   }

   public void testGeneralItems() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "general_items");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, getPath() + "/general_items.html", "utf-8");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(4, result.getRowSize());
   }
   
   public void testGeneralList() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "general_list");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, getPath() + "/general_list.html", "utf-8");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(1, result.getRowSize());
   }

   public void testMapStatus() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "map_status");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, getPath() + "/map_status.html", "utf-8");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(10, result.getRowSize());
   }

   public void testResourceDetail() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "resource_detail");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, getPath() + "/resource_detail.html", "utf-8");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(1, result.getRowSize());
   }

   public void testResourceList() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "resource_list");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, getPath() + "/resource_list.html", "utf-8");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(18, result.getRowSize());
   }
   
   public void testStateList() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "state_list");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, getPath() + "/state_list.html", "utf-8");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(13, result.getRowSize());
   }
   
   public void testStoreItems() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "store_items");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, getPath() + "/store_items.html", "utf-8");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(26, result.getRowSize());
   }

   public void testVillageDetail() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "village_detail");
      assertNotNull(query);
      
      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, getPath() + "/village_detail.html", "utf-8");
      WdbcResult result = engine.execute(query, source);
      
      assertNotNull(result);
      assertEquals(1, result.getRowSize());
   }
}
