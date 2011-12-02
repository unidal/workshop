package com.site.game.sanguo.thread.handler;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpException;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.dal.xml.XmlException;
import com.site.game.sanguo.model.Build;
import com.site.game.sanguo.model.Farm;
import com.site.game.sanguo.model.Price;
import com.site.game.sanguo.model.Task;
import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.ThreadException;
import com.site.game.sanguo.thread.ThreadHandler;
import com.site.game.sanguo.thread.ThreadHelper;
import com.site.game.sanguo.thread.handler.building.BuildingPlan;
import com.site.game.sanguo.thread.wdbc.WdbcFetcher;
import com.site.wdbc.WdbcException;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.Session;

public class BuildingHandler implements ThreadHandler, LogEnabled {
   private WdbcFetcher m_wdbcFetcher;

   private BuildingPlan m_buildingPlan;

   private Request m_upgradeRequest;

   private Request m_constructionRequest;

   private Logger m_logger;

   private boolean canProceed(ThreadContext ctx) {
      boolean hasField = false;
      boolean hasBuilding = false;

      for (Task task : ctx.getFarm().getQueuedTasks()) {
         // field and build
         if (task.getTypeId() == 8) {
            String intro = task.getIntro();

            if (intro.contains("农田") || intro.contains("采石场") || intro.contains("林场") || intro.contains("矿山")) {
               hasField = true;
            } else {
               hasBuilding = true;
            }
         }
      }

      String tribe = ctx.getSession().getProperties().get("tribe");

      if ("wei".equals(tribe)) {
         return !hasBuilding;
      } else {
         return !hasField & !hasBuilding;
      }
   }

   private void constructBuilding(ThreadContext ctx, Build building, Price price) throws HttpException, IOException {
      Session session = ctx.getSession();

      session.setProperty("building-id", building.getResourceId());
      session.setProperty("building-type-id", building.getTypeId());
      ThreadHelper.setRandom(ctx);

      try {
         ThreadHelper.executeRequest(session, m_constructionRequest, true);

         Price stock = ctx.getFarm().getStock();

         stock.setCrop(stock.getCrop() - price.getCrop());
         stock.setLumber(stock.getLumber() - price.getLumber());
         stock.setClay(stock.getClay() - price.getClay());
         stock.setIron(stock.getIron() - price.getIron());
         stock.setAvailablePopulation(stock.getAvailablePopulation() - price.getPopulation());

         m_logger.info("Constructing building(" + building.getTypeName() + " Lv " + building.getLevel() + ") at "
               + ctx.getFarm().getVillageName());
      } catch (ThreadException e) {
         // ignore it
      }
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   private Price getUpgradePrice(ThreadContext ctx, Build building) throws HttpException, IOException, XmlException,
         WdbcException {
      Map<String, Price> priceMap = ctx.getModel().getPriceMap();
      String key = building.getTypeName() + ":" + building.getLevel();
      Price price = priceMap.get(key);

      if (price == null) {
         price = getUpgradePrice(ctx, building.getResourceId());
         priceMap.put(key, price);
      }

      if (price != null) {
         Price stock = ctx.getFarm().getStock();

         if (stock.getCrop() >= price.getCrop() && stock.getLumber() >= price.getLumber()
               && stock.getClay() >= price.getClay() && stock.getIron() >= price.getIron()
               && stock.getAvailablePopulation() > price.getPopulation()) {
            return price;
         }
      }

      return null;
   }

   private Price getUpgradePrice(ThreadContext ctx, int resourceId) throws HttpException, IOException, XmlException,
         WdbcException {
      WdbcResult detail = m_wdbcFetcher.getBuildingDetail(ctx, resourceId);

      try {
         if (detail.getRowSize() > 0) {
            Price price = new Price();

            price.setCrop(Integer.parseInt((String) detail.getCell(0, "crop")));
            price.setLumber(Integer.parseInt((String) detail.getCell(0, "lumber")));
            price.setClay(Integer.parseInt((String) detail.getCell(0, "clay")));
            price.setIron(Integer.parseInt((String) detail.getCell(0, "iron")));
            price.setPopulation(Integer.parseInt((String) detail.getCell(0, "population")));

            return price;
         }
      } catch (Exception e) {
         // ignore it
      }

      // for some buildings
      Price price = new Price();

      price.setCrop(500);
      price.setLumber(500);
      price.setClay(500);
      price.setIron(500);

      return price;
   }

   public void handle(ThreadContext ctx) throws ThreadException {
      for (Farm farm : ctx.getModel().getFarms()) {
         ctx.setFarm(farm);
         ctx.getSession().setProperty("village-id", farm.getVillageId());

         if (canProceed(ctx)) {
            try {
               Build building = m_buildingPlan.determineBuilding(ctx);
               Price price = building == null ? null : getUpgradePrice(ctx, building);

               if (price != null) {
                  if (building.getLevel() <= 1) {
                     constructBuilding(ctx, building, price);
                  } else {
                     upgradeBuilding(ctx, building, price);
                  }
               }
            } catch (Exception e) {
               m_logger.warn("Error when handling Building.", e);
            }
         }
      }
   }

   private void upgradeBuilding(ThreadContext ctx, Build building, Price price) throws HttpException, IOException {
      Session session = ctx.getSession();

      session.setProperty("building-id", building.getResourceId());
      ThreadHelper.setRandom(ctx);

      try {
         ThreadHelper.executeRequest(session, m_upgradeRequest, true);

         Price stock = ctx.getFarm().getStock();

         stock.setCrop(stock.getCrop() - price.getCrop());
         stock.setLumber(stock.getLumber() - price.getLumber());
         stock.setClay(stock.getClay() - price.getClay());
         stock.setIron(stock.getIron() - price.getIron());
         stock.setPopulation(stock.getAvailablePopulation() - price.getPopulation());

         building.setLevel(building.getLevel() + 1);
         m_logger.info("Upgrading building(" + building.getTypeName() + " Lv " + building.getLevel() + ") at "
               + ctx.getFarm().getVillageName());
      } catch (ThreadException e) {
         // ignore it
      }
   }
}
