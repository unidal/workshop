package com.site.game.sanguo.thread.handler;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpException;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.dal.xml.XmlException;
import com.site.game.sanguo.model.Farm;
import com.site.game.sanguo.model.Field;
import com.site.game.sanguo.model.Price;
import com.site.game.sanguo.model.Stock;
import com.site.game.sanguo.model.Task;
import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.ThreadException;
import com.site.game.sanguo.thread.ThreadHandler;
import com.site.game.sanguo.thread.ThreadHelper;
import com.site.game.sanguo.thread.wdbc.WdbcFetcher;
import com.site.wdbc.WdbcException;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.Session;

public class ResourceHandler implements ThreadHandler, LogEnabled {
   private WdbcFetcher m_wdbcFetcher;

   private Request m_upgradeRequest;

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
         return !hasField;
      } else {
         return !hasField & !hasBuilding;
      }
   }

   private Field chooseField(ThreadContext ctx, String typeCode) {
      Field selected = null;

      for (Field field : ctx.getFarm().getFields()) {
         if (field.getTypeCode().equals(typeCode)) {
            if (selected == null) {
               selected = field;
            } else if (field.getLevel() < selected.getLevel()) {
               selected = field;
            }
         }
      }

      return selected;
   }

   private String determinField(ThreadContext ctx) {
      Map<String, Stock> stocks = ctx.getFarm().getStocks();
      Stock population = stocks.get("population");

      if (population.getNow() + 1 > population.getMax()) {
         // has reach the max population, upgrade field
         return "crop";
      }

      String typeCode = null;
      double minValue = Double.MAX_VALUE;

      for (Stock e : stocks.values()) {
         if (e.getId().equals("population")) {
            continue;
         }

         // calculate for next 12 hours
         double value = e.getIncrease() * 12 + e.getNow();

         if (value < minValue && !isAllTopLevel(ctx, e.getId(), ctx.getFarm().isMain())) {
            minValue = value;
            typeCode = e.getId();
         }
      }

      return typeCode;
   }

   private boolean isAllTopLevel(ThreadContext ctx, String id, boolean isMain) {
      for (Field field : ctx.getFarm().getFields()) {
         if (field.getTypeCode().equals(id)) {
            if (isMain && field.getLevel() < 20) {
               return false;
            } else if (!isMain && field.getLevel() < 10) {
               return false;
            }
         }
      }

      return true;
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   private Price getUpgradePrice(ThreadContext ctx, Field field) throws HttpException, IOException, XmlException,
         WdbcException {
      Map<String, Price> priceMap = ctx.getModel().getPriceMap();
      String key = field.getTypeName() + ":" + field.getLevel();
      Price price = priceMap.get(key);

      if (price == null) {
         price = getUpgradePrice(ctx, field.getResourceId());
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
      WdbcResult detail = m_wdbcFetcher.getResourceDetail(ctx, resourceId);

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

      return null;
   }

   public void handle(ThreadContext ctx) throws ThreadException {
      for (Farm farm : ctx.getModel().getFarms()) {
         ctx.setFarm(farm);
         ctx.getSession().setProperty("village-id", farm.getVillageId());

         if (canProceed(ctx)) {
            try {
               String fieldTypeCode = determinField(ctx);
               Field field = chooseField(ctx, fieldTypeCode);
               Price price = field == null ? null : getUpgradePrice(ctx, field);

               if (price != null) {
                  upgradeField(ctx, field, price);
               }
            } catch (Exception e) {
               m_logger.warn("Error when handling Resouce.", e);
            }
         }
      }
   }

   private void upgradeField(ThreadContext ctx, Field field, Price price) throws HttpException, IOException {
      Session session = ctx.getSession();

      session.setProperty("resource-id", field.getResourceId());
      ThreadHelper.setRandom(ctx);

      try {
         ThreadHelper.executeRequest(session, m_upgradeRequest, true);

         Price stock = ctx.getFarm().getStock();

         stock.setCrop(stock.getCrop() - price.getCrop());
         stock.setLumber(stock.getLumber() - price.getLumber());
         stock.setClay(stock.getClay() - price.getClay());
         stock.setIron(stock.getIron() - price.getIron());
         stock.setAvailablePopulation(stock.getAvailablePopulation() - price.getPopulation());

         field.setLevel(field.getLevel() + 1);
         m_logger.info("Upgrading field (" + field.getTypeName() + " Lv " + field.getLevel() + ")");
      } catch (ThreadException e) {
         // ignore it
      }
   }
}
