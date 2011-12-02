package com.site.wdbc.query.path;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.SelectQuery;
import com.site.wdbc.WdbcQuery;

public class TagTreeTest extends ComponentTestCase {
   public void testBuildTree() throws Exception {
      SelectQuery query = (SelectQuery) lookup(WdbcQuery.class, "simple");
      WdbcTagTree tree = TagTree.buildTree(query.getPatterns(), false);
      
      assertEquals(1, tree.getChildren().size());
      assertEquals(3, tree.getChild("table").getChildren().size());
      assertEquals(2, tree.getChild("table").getChild("tr").getChildren().size());
      assertEquals(1, tree.getChild("table").getChild("ts").getChildren().size());
      assertEquals(1, tree.getChild("table").getChild("tt").getChildren().size());
   }
}
