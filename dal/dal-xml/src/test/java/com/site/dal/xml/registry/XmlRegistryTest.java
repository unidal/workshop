package com.site.dal.xml.registry;

import java.util.Date;
import java.util.List;

import com.site.dal.xml.sanguo.Actions;
import com.site.dal.xml.sanguo.Game;
import com.site.dal.xml.sanguo.Html;
import com.site.dal.xml.sanguo.Resource;
import com.site.dal.xml.sanguo.Soldier;
import com.site.dal.xml.sanguo.Soldiers;
import com.site.dal.xml.sanguo.Village;
import com.site.lookup.ComponentTestCase;

public class XmlRegistryTest extends ComponentTestCase {
   public void testNormalCase() throws Exception {
      XmlRegistry registry = lookup(XmlRegistry.class);

      registry.register(Game.class);

      assertEquals(Game.class, registry.findRootElementType("game"));
      assertNotNull(registry.findElementEntry(Game.class));
      assertNotNull(registry.findElementEntry(Village.class));
      assertNotNull(registry.findElementEntry(Actions.class));
      assertNotNull(registry.findElementEntry(Resource.class));
      assertNotNull(registry.findElementEntry(Html.class));

      ElementEntry gameEntry = registry.findElementEntry(Game.class);

      assertEquals(Village.class, gameEntry.getElement("village").getType());
      assertEquals(String.class, gameEntry.getElement("script").getType());
      assertEquals(Actions.class, gameEntry.getElement("actions").getType());
      assertEquals(Date.class, gameEntry.getElement("time").getType());
      assertEquals(List.class, gameEntry.getElement("resos").getType());
      assertEquals(Resource.class, gameEntry.getElement("resos").getElement("reso").getType());
      assertEquals(Soldiers.class, gameEntry.getElement("soldiers").getType());
      assertEquals(List.class, gameEntry.getElement("htmls").getType());
      assertEquals(Html.class, gameEntry.getElement("htmls").getElement("html").getType());

      assertEquals(Soldier.class, registry.findElementEntry(Soldiers.class).getComponentElement("soldier").getType());
      assertNotNull(registry.findElementEntry(Html.class).getValueEntry());
   }
}
