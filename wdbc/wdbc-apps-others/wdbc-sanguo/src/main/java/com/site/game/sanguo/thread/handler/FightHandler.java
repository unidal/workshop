package com.site.game.sanguo.thread.handler;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpException;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.dal.xml.XmlException;
import com.site.game.sanguo.Configuration;
import com.site.game.sanguo.model.Build;
import com.site.game.sanguo.model.Colonia;
import com.site.game.sanguo.model.Farm;
import com.site.game.sanguo.model.Soldier;
import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.ThreadException;
import com.site.game.sanguo.thread.ThreadHandler;
import com.site.game.sanguo.thread.ThreadHelper;
import com.site.game.sanguo.thread.wdbc.WdbcFetcher;
import com.site.wdbc.WdbcException;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.Session;

public class FightHandler implements ThreadHandler, LogEnabled {
   private WdbcFetcher m_wdbcFetcher;

   private Configuration m_configuration;

   private Request m_startWarRequest;

   private Request m_startWarConfirmRequest;

   private Logger m_logger;

   private boolean attackColonia(ThreadContext ctx, Colonia colonia) throws HttpException, IOException, XmlException,
         WdbcException {
      if (colonia == null) {
         return false;
      }

      StringBuilder sb = new StringBuilder(32);
      int maxCount = m_configuration.getFightMaxCount();
      int totalCount = 0;
      int villageId = ctx.getFarm().getVillageId();

      for (Soldier soldier : ctx.getFarm().getSoldiers()) {
         if (soldier.getVillageId() != villageId) {
            continue;
         }

         int typeId = soldier.getTypeId();

         if (isTypeEligible(typeId)) {
            int count = soldier.getCount() < maxCount ? soldier.getCount() : maxCount;

            totalCount += count;
            maxCount -= count;
            soldier.setCount(soldier.getCount() - count);
            sb.append("&soldier[").append(typeId).append("]=").append(count);

            if (maxCount == 0) {
               break;
            }
         }
      }

      // no attacking if less than 10 soldiers
      if (totalCount < m_configuration.getFightMinCount()) {
         return false;
      }

      Session session = ctx.getSession();

      session.setProperty("building-id", getBuildingId(ctx));
      session.setProperty("x", colonia.getX());
      session.setProperty("y", colonia.getY());
      session.setProperty("soldier-data", sb.toString());
      ThreadHelper.setRandom(ctx);

      try {
         ThreadHelper.executeRequest(session, m_startWarRequest, true);
         ThreadHelper.setRandom(ctx);

         ThreadHelper.executeRequest(session, m_startWarConfirmRequest, true);

         if (colonia.getVillageName() == null) {
            fetchVillageName(ctx, colonia);
         }

         m_logger.info("Attacking " + colonia.getVillageName() + "(" + colonia.getX() + "," + colonia.getY() + ")");
         return true;
      } catch (ThreadException e) {
         // ignore it
      }

      return false;
   }

   private void fetchVillageName(ThreadContext ctx, Colonia colonia) throws HttpException, IOException, XmlException,
         WdbcException {
      WdbcResult result = m_wdbcFetcher.getVillageDetail(ctx, colonia.getX(), colonia.getY());

      if (result.getRowSize() > 0) {
         int row = 0;
         colonia.setVillageName((String) result.getCell(row, "village"));
         colonia.setTribe((String) result.getCell(row, "tribe"));
         colonia.setAlliance((String) result.getCell(row, "alliance"));
         colonia.setEmperor((String) result.getCell(row, "emperor"));
         colonia.setPopulation(Integer.parseInt((String) result.getCell(row, "population")));
         colonia.setRank((String) result.getCell(row, "rank"));
      }
   }

   private boolean canProcess(ThreadContext ctx) {
      // no fight for population less than 300
      if (ctx.getFarm().getStocks().get("population").getNow() < 300) {
         return false;
      }

      // must have 中军帐
      if (getBuildingId(ctx) == -1) {
         return false;
      }

      // must be above 10, so recruiting soldier one by one won't be affected
      int count = 0;
      int villageId = ctx.getFarm().getVillageId();

      for (Soldier soldier : ctx.getFarm().getSoldiers()) {
         if (soldier.getVillageId() != villageId) {
            continue;
         }

         int typeId = soldier.getTypeId();

         if (isTypeEligible(typeId)) {
            count += soldier.getCount();
         }
      }

      return count >= m_configuration.getFightMinCount();
   }

   private boolean isTypeEligible(int typeId) {
      return typeId >= 0 && typeId <= 4 || typeId == 10 || typeId == 11;
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   private int getBuildingId(ThreadContext ctx) {
      for (Build build : ctx.getFarm().getBuildings().values()) {
         if (build.getTypeName().equals("中军帐")) {
            return build.getResourceId();
         }
      }

      return -1;
   }

   public void handle(ThreadContext ctx) throws ThreadException {
      for (Farm farm : ctx.getModel().getFarms()) {
         ctx.setFarm(farm);
         ctx.getSession().setProperty("village-id", farm.getVillageId());

         if (canProcess(ctx)) {
            try {
               while (true) {
                  Colonia colonia = selectColonia(ctx);

                  if (!attackColonia(ctx, colonia)) {
                     break;
                  }
               }
            } catch (Exception e) {
               m_logger.warn("Error when handling Fight.", e);
            }
         }
      }
   }

   private int m_lastIndex = 0;

   private Colonia selectColonia(ThreadContext ctx) {
      List<Colonia> colonias = ctx.getFarm().getColonias();
      int index = m_lastIndex;

      if (colonias.size() > 0) {
         m_lastIndex = (m_lastIndex + 1) % colonias.size();

         return colonias.get(index);
      } else {
         return null;
      }
   }
}
