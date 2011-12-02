package com.site.game.sanguo.thread.handler;

import java.io.IOException;
import java.text.ParseException;

import org.apache.http.HttpException;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.dal.xml.XmlException;
import com.site.game.sanguo.model.Court;
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

public class CourtHandler implements ThreadHandler, LogEnabled {
   private Request m_stateCourtRequest;

   private Logger m_logger;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public void handle(ThreadContext ctx) throws ThreadException {
      Court court = ctx.getModel().getCourt();

      if (court != null && court.isEligible()) {
         try {
            startCourt(ctx);
            updateModel(ctx);
         } catch (Exception e) {
            m_logger.warn("Error when handling Court.", e);
         }
      }
   }

   private void startCourt(ThreadContext ctx) throws HttpException, IOException {
      Session session = ctx.getSession();

      ThreadHelper.setRandom(ctx);

      try {
         ThreadHelper.executeRequest(session, m_stateCourtRequest, true);
         m_logger.info("овдирямЙЁи");
      } catch (ThreadException e) {
         // ignore it
      }
   }

   private void updateModel(ThreadContext ctx) throws HttpException, IOException, XmlException, WdbcException,
         ParseException {
      Model model = ctx.getModel();
      Farm mainFarm = model.getFarms().get(0);
      Status status = new Status();

      status.setName("court");
      status.setDirty(true);
      mainFarm.getHandlerStatuses().put(status.getName(), status);
   }
}
