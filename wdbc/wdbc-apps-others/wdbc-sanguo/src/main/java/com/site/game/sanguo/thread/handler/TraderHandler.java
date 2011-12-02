package com.site.game.sanguo.thread.handler;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpException;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.game.sanguo.model.Price;
import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.ThreadException;
import com.site.game.sanguo.thread.ThreadHandler;
import com.site.game.sanguo.thread.ThreadHelper;
import com.site.game.sanguo.thread.handler.trader.TraderPlan;
import com.site.game.sanguo.thread.handler.trader.TraderTask;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.Session;

public class TraderHandler implements ThreadHandler, LogEnabled {
   private TraderPlan m_plan;

   private Request m_tradeRequest;

   private Logger m_logger;

   private void adjustStock(ThreadContext ctx, TraderTask task) {
      Price from = task.getFromFarm().getStock();
      Price to = task.getToFarm().getStock();

      from.setLumber(from.getLumber() - task.getLumber());
      from.setClay(from.getClay() - task.getClay());
      from.setIron(from.getIron() - task.getIron());
      from.setCrop(from.getCrop() - task.getCrop());
      to.setLumber(to.getLumber() + task.getLumber());
      to.setClay(to.getClay() + task.getClay());
      to.setIron(to.getIron() + task.getIron());
      to.setCrop(to.getCrop() + task.getCrop());
   }

   private void checkBound(ThreadContext ctx, TraderTask task) {
      double total = task.getLumber() + task.getClay() + task.getIron() + task.getCrop();
      double capacity = 20000;

      if (total > capacity) {
         double ratio = total / capacity;

         task.setLumber((int) (task.getLumber() * ratio));
         task.setClay((int) (task.getClay() * ratio));
         task.setIron((int) (task.getIron() * ratio));
         task.setCrop((int) (task.getCrop() * ratio));
      }
   }

   private boolean doTrade(ThreadContext ctx, TraderTask task) throws HttpException, IOException {
      Session session = ctx.getSession();

      ThreadHelper.setRandom(ctx);
      session.setProperty("village-id", task.getFromFarm().getVillageId());
      session.setProperty("villageName", task.getToFarm().getVillageName());
      session.setProperty("x", String.valueOf(task.getToFarm().getX()));
      session.setProperty("y", String.valueOf(task.getToFarm().getY()));
      session.setProperty("lumber", String.valueOf(task.getLumber()));
      session.setProperty("clay", String.valueOf(task.getClay()));
      session.setProperty("iron", String.valueOf(task.getIron()));
      session.setProperty("crop", String.valueOf(task.getCrop()));

      try {
         String result = ThreadHelper.executeRequest(session, m_tradeRequest, true);

         if (result.indexOf("资源开始运输") > 0) {
            m_logger.info("Shipping resource(" + task.getLumber() + "," + task.getClay() + "," + task.getIron() + ","
                  + task.getCrop() + ") from " + task.getFromFarm().getVillageName() + "(" + task.getFromFarm().getX()
                  + "," + task.getFromFarm().getY() + ") to " + task.getToFarm().getVillageName() + "("
                  + task.getToFarm().getX() + "," + task.getToFarm().getY() + ")");
            return true;
         } else if (result.indexOf("运输资源总量不能超过商人最大运载总量") > 0) {
            // ignore it
            return false;
         } else {
            m_logger.info(result);
         }
      } catch (ThreadException e) {
         m_logger.error("Error when shipping resource: " + e, e);
      }

      return false;
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public void handle(ThreadContext ctx) throws ThreadException {
      try {
         List<TraderTask> tasks = m_plan.getTraderTasks(ctx);

         for (TraderTask task : tasks) {
            checkBound(ctx, task);

            if (doTrade(ctx, task)) {
               adjustStock(ctx, task);
            }
         }
      } catch (Exception e) {
         m_logger.error("Error when doing trading: " + e, e);
      }
   }
}
