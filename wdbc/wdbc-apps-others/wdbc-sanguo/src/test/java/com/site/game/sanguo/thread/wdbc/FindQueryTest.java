package com.site.game.sanguo.thread.wdbc;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.ResourceSource;
import com.site.wdbc.WdbcEngine;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.WdbcSource;
import com.site.wdbc.WdbcSourceType;

public class FindQueryTest extends ComponentTestCase {
   public void testBuildingDetail() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "building_detail");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "building_detail.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(2, result.getRowSize());
   }

   public void testBuildingList() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "building_list");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "building_list.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(13, result.getRowSize());
   }

   public void testCourtList() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "court_list");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "court_list.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(2, result.getRowSize());
   }

   public void testGeneralDetail() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "general_detail");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "general_detail.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(3, result.getRowSize());
   }

   public void testGeneralItems() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "general_items");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "general_items.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(2, result.getRowSize());
   }

   public void testGeneralList() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "general_list");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "general_list.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(2, result.getRowSize());
   }

   public void testMapStatus() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "map_status");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "map_status.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(2, result.getRowSize());
   }

   public void testResourceDetail() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "resource_detail");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "resource_detail.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(3, result.getRowSize());
   }

   public void testResourceList() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "resource_list");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "resource_list.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(11, result.getRowSize());
   }

   public void testStateList() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "state_list");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "state_list.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(13, result.getRowSize());
   }

   public void testStoreItems() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "store_items");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "store_items.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(6, result.getRowSize());
   }

   public void testVillageDetail() throws Exception {
      WdbcQuery query = lookup(WdbcQuery.class, "village_detail");
      assertNotNull(query);

      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcSource source = new ResourceSource(WdbcSourceType.HTML, "village_detail.html");
      WdbcResult result = engine.execute(query, source);

      assertNotNull(result);
      assertEquals(2, result.getRowSize());
   }
}