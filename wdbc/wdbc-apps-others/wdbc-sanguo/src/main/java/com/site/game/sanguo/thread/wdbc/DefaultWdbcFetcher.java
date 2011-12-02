package com.site.game.sanguo.thread.wdbc;

import java.io.IOException;
import java.io.StringReader;

import org.apache.http.HttpException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.xml.sax.InputSource;

import com.site.dal.xml.XmlAdapter;
import com.site.dal.xml.XmlException;
import com.site.dal.xml.registry.XmlRegistry;
import com.site.game.sanguo.api.Game;
import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.ThreadException;
import com.site.game.sanguo.thread.ThreadHelper;
import com.site.lookup.ContainerHolder;
import com.site.wdbc.StringSource;
import com.site.wdbc.WdbcEngine;
import com.site.wdbc.WdbcException;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.WdbcSource;
import com.site.wdbc.WdbcSourceType;
import com.site.wdbc.http.Request;

public class DefaultWdbcFetcher extends ContainerHolder implements WdbcFetcher, Initializable {
   private XmlRegistry m_registry;

   private XmlAdapter m_xmlAdapter;

   private Request m_resourceListRequest;

   private Request m_resourceDetailRequest;

   private Request m_buildingListRequest;

   private Request m_buildingDetailRequest;

   private Request m_stateListRequest;

   private Request m_generalListRequest;

   private Request m_generalDetailRequest;

   private Request m_generalItemsRequest;

   private Request m_storeItemsRequest;

   private Request m_mapStatusRequest;

   private Request m_mapDetailRequest;

   private Request m_courtListRequest;

   private Request m_villageDetailRequest;

   private WdbcResult executeQuery(String name, WdbcSource source) throws WdbcException {
      WdbcQuery query = lookup(WdbcQuery.class, name);
      WdbcEngine engine = lookup(WdbcEngine.class);
      WdbcResult result = engine.execute(query, source);

      release(query);
      release(engine);

      return result;
   }

   public WdbcResult getBuildingDetail(ThreadContext ctx, int id) throws HttpException, IOException, XmlException,
         WdbcException {
      ctx.getSession().setProperty("building-id", id);
      return queryWdbcResult(ctx, m_buildingDetailRequest, "building_detail", "floatblockright");
   }

   public WdbcResult getBuildingList(ThreadContext ctx) throws HttpException, IOException, XmlException, WdbcException {
      return queryWdbcResult(ctx, m_buildingListRequest, "building_list", "main");
   }

   public WdbcResult getGeneralItems(ThreadContext ctx, int id) throws HttpException, IOException, XmlException,
         WdbcException {
      ctx.getSession().setProperty("general-id", id);
      return queryWdbcResult(ctx, m_generalItemsRequest, "general_items", "floatblockright");
   }

   public WdbcResult getGeneralList(ThreadContext ctx) throws HttpException, IOException, XmlException, WdbcException {
      return queryWdbcResult(ctx, m_generalListRequest, "general_list", "floatblockleft");
   }

   public WdbcResult getVillageList(ThreadContext ctx, int x, int y) throws HttpException, IOException, XmlException,
         WdbcException {
      ctx.getSession().setProperty("x", x);
      ctx.getSession().setProperty("y", y);
      return queryWdbcResult(ctx, m_mapStatusRequest, "map_status", "main");
   }

   public WdbcResult getVillageDetail(ThreadContext ctx, int villageId) throws HttpException, IOException,
         XmlException, WdbcException {
      ctx.getSession().setProperty("villageId", villageId);
      return queryWdbcResult(ctx, m_villageDetailRequest, "village_detail", "floatblockright");
   }

   public WdbcResult getVillageDetail(ThreadContext ctx, int x, int y) throws HttpException, IOException, XmlException,
         WdbcException {
      ctx.getSession().setProperty("x", x);
      ctx.getSession().setProperty("y", y);
      return queryWdbcResult(ctx, m_mapDetailRequest, "map_detail", "floatblockright");
   }

   public WdbcResult getCourtList(ThreadContext ctx) throws HttpException, IOException, XmlException, WdbcException {
      return queryWdbcResult(ctx, m_courtListRequest, "court_list", "floatblockleft");
   }

   public WdbcResult getResourceDetail(ThreadContext ctx, int id) throws HttpException, IOException, XmlException,
         WdbcException {
      ctx.getSession().setProperty("resource-id", id);
      return queryWdbcResult(ctx, m_resourceDetailRequest, "resource_detail", "floatblockright");
   }

   public WdbcResult getResourceList(ThreadContext ctx) throws HttpException, IOException, XmlException, WdbcException {
      return queryWdbcResult(ctx, m_resourceListRequest, "resource_list", "main");
   }

   public WdbcResult getStateList(ThreadContext ctx) throws HttpException, IOException, XmlException, WdbcException {
      return queryWdbcResult(ctx, m_stateListRequest, "state_list", "floatblockleft");
   }

   public WdbcResult getStoreItems(ThreadContext ctx) throws HttpException, IOException, XmlException, WdbcException {
      return queryWdbcResult(ctx, m_storeItemsRequest, "store_items", "floatblockleft");
   }

   public void initialize() throws InitializationException {
      m_registry.register(Game.class);
   }

   private WdbcResult queryWdbcResult(ThreadContext ctx, Request request, String queryName, String htmlName)
         throws HttpException, IOException, XmlException, WdbcException {
      ThreadHelper.setRandom(ctx);
      String content;

      try {
         content = ThreadHelper.executeRequest(ctx.getSession(), request, false);
      } catch (ThreadException e) {
         throw new RuntimeException("Unexpected exception caught!", e);
      }

      Game game = m_xmlAdapter.unmarshal(new InputSource(new StringReader(content)));

      ctx.setGame(game);

      String html = ThreadHelper.getHtml(game, htmlName);
      WdbcResult result = executeQuery(queryName, new StringSource(WdbcSourceType.HTML, html));
      return result;
   }

   public WdbcResult getGeneralDetail(ThreadContext ctx, int id) throws HttpException, IOException, XmlException,
         WdbcException {
      ctx.getSession().setProperty("general-id", id);
      return queryWdbcResult(ctx, m_generalDetailRequest, "general_detail", "floatblockleft");
   }
}
