package com.site.game.sanguo.thread.wdbc;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.site.game.sanguo.api.Game;
import com.site.game.sanguo.api.Village;
import com.site.game.sanguo.model.Colonia;
import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.ThreadHandler;
import com.site.game.sanguo.thread.handler.fighting.ColoniaManager;
import com.site.lookup.ComponentTestCase;

public class DefaultWdbcFetcherTest extends ComponentTestCase {
   private ThreadContext m_ctx = new ThreadContext();

   @Override
   public void setUp() throws Exception {
      super.setUp();

      lookup(ThreadHandler.class, "signin").handle(m_ctx);
      assertTrue(m_ctx.isSignedIn());

      lookup(ThreadHandler.class, "landing-page").handle(m_ctx);
      assertNotNull(m_ctx.getMainVillageId());
   }

   @Test
   @Ignore
   public void testRanged() throws Exception {
      ColoniaManager manager = lookup(ColoniaManager.class, "ranged");
      Game game = new Game();
      Village village = new Village();

      village.setX(476);
      village.setY(293);
      game.setVillage(village);
      m_ctx.setGame(game);
      System.setProperty("fight.diameter", "7");

      List<Colonia> colonias = manager.getColonias(m_ctx);
      assertEquals(7, colonias.size());
   }

   @Test
   @Ignore
   public void testTrader() throws Exception {
      lookup(ThreadHandler.class, "refresh").handle(m_ctx);
      lookup(ThreadHandler.class, "building").handle(m_ctx);
   }

   @Test
   @Ignore
   public void testAddressed() throws Exception {
      ColoniaManager manager = lookup(ColoniaManager.class, "addressed");
      System.setProperty("fight.villages", "476:293,477:299,476:294");

      List<Colonia> colonias = manager.getColonias(m_ctx);
      assertEquals(1, colonias.size());
      assertNotNull(colonias.get(0).getAlliance());
   }

   @Test
   @Ignore
   public void testComposited() throws Exception {
      ColoniaManager manager = lookup(ColoniaManager.class, "ranged");
      Game game = new Game();
      Village village = new Village();

      village.setX(476);
      village.setY(293);
      game.setVillage(village);
      m_ctx.setGame(game);
      System.setProperty("fight.diameter", "7");

      List<Colonia> colonias = manager.getColonias(m_ctx);
      assertEquals(7, colonias.size());

      manager = lookup(ColoniaManager.class, "addressed");
      System.setProperty("fight.villages", "476:293,477:299,476:294");

      colonias = manager.getColonias(m_ctx);
      assertEquals(1, colonias.size());
      assertNotNull(colonias.get(0).getAlliance());

      manager = lookup(ColoniaManager.class, "composited");

      colonias = manager.getColonias(m_ctx);
      System.out.println(colonias);
   }
}
