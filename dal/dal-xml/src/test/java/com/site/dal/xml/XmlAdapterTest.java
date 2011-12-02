package com.site.dal.xml;

import java.io.File;
import java.text.SimpleDateFormat;

import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.site.dal.xml.registry.XmlRegistry;
import com.site.dal.xml.sanguo.Game;
import com.site.lookup.ComponentTestCase;

public class XmlAdapterTest extends ComponentTestCase {
   private XmlRegistry m_registry;

   @Override
   public void setUp() throws Exception {
      super.setUp();

      m_registry = lookup(XmlRegistry.class);
      m_registry.register(Game.class);
   }

   public void testMarshalToStream() throws Exception {
      XmlAdapter adapter = lookup(XmlAdapter.class);
      Game game = adapter.unmarshal(new InputSource(getClass().getResourceAsStream("sanguo.xml")));
      File file = new File(getClass().getResource("").getFile() + "/sanguo2.xml");
      StreamResult result = new StreamResult(file);

      adapter.marshal(result, game);
   }

   public void testMarshalToDom() throws Exception {
      XmlAdapter adapter = lookup(XmlAdapter.class);
      Game game = adapter.unmarshal(new InputSource(getClass().getResourceAsStream("sanguo.xml")));
      DOMResult result = new DOMResult();

      adapter.marshal(result, game);

      Node root = result.getNode();
      assertEquals(1, root.getChildNodes().getLength());
      assertEquals("game", root.getFirstChild().getNodeName());
      assertEquals("script", root.getFirstChild().getFirstChild().getNodeName());
      assertEquals("script", root.getFirstChild().getFirstChild().getNodeName());
   }

   public void testUnmarshal() throws Exception {
      XmlAdapter adapter = lookup(XmlAdapter.class);
      Game game = adapter.unmarshal(new InputSource(getClass().getResourceAsStream("sanguo.xml")));
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

      assertNotNull(game);
      assertNotNull(game.getScript());
      assertEquals(dateFormat.parse("2008-09-06 12:06:49"), game.getTime());
      assertEquals("resources.status", game.getActions().getAct());
      assertEquals(435, game.getVillage().getX());
      assertEquals(269, game.getVillage().getY());
      assertEquals(5, game.getResources().size());
      assertEquals(1, game.getVillages().size());
      assertEquals(7970, game.getVillages().get(0).getId());
      assertEquals(6, game.getSoldiers().getZjzbid());
      assertEquals(2, game.getSoldiers().getSoldiers().length);
      assertEquals(1, game.getTasks().getTasks().length);
      assertEquals(dateFormat.parse("2008-09-06 11:16:33"), game.getTasks().getTasks()[0].getBeginTime());
      assertEquals(8, game.getHtmls().size());
      assertNotNull(game.getHtmls().get(0).getValue());
   }
}
