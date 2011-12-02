package com.site.game.sanguo.thread.handler;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

import org.apache.http.HttpException;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.dal.xml.XmlException;
import com.site.game.sanguo.model.Farm;
import com.site.game.sanguo.model.Model;
import com.site.game.sanguo.model.Status;
import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.ThreadException;
import com.site.game.sanguo.thread.ThreadHandler;
import com.site.game.sanguo.thread.ThreadHelper;
import com.site.wdbc.WdbcException;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.Session;

public class GiftHandler implements ThreadHandler, LogEnabled {
   private Request m_mygiftRequest;

   private Logger m_logger;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   private void getMyGift(ThreadContext ctx) throws HttpException, IOException {
      Session session = ctx.getSession();

      ThreadHelper.setRandom(ctx);

      try {
         ThreadHelper.executeRequest(session, m_mygiftRequest, true);
         m_logger.info("领取新手礼包");
      } catch (ThreadException e) {
         // ignore it
      }
   }

   public void handle(ThreadContext ctx) throws ThreadException {
      if (ctx.getModel().getGift().isEligible()) {
         try {
            getMyGift(ctx);
            updateModel(ctx);
         } catch (Exception e) {
            m_logger.warn("Error when handling MyGift.", e);
         }
      }
   }

   private void updateModel(ThreadContext ctx) throws HttpException, IOException, XmlException, WdbcException,
         ParseException {
      Model model = ctx.getModel();
      Farm mainFarm = model.getFarms().get(0);
      Status status = new Status();
      Calendar cal = Calendar.getInstance();

      // set to next day
      cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);

      status.setName("gift");
      status.setDirty(true);
      status.setNextSchedule(cal.getTime());
      mainFarm.getHandlerStatuses().put(status.getName(), status);
   }
}
