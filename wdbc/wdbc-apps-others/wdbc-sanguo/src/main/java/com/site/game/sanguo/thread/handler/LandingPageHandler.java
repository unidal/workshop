package com.site.game.sanguo.thread.handler;

import java.net.URL;
import java.text.MessageFormat;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.game.sanguo.Configuration;
import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.ThreadException;
import com.site.game.sanguo.thread.ThreadHandler;
import com.site.game.sanguo.thread.ThreadHelper;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.Session;

public class LandingPageHandler implements ThreadHandler, LogEnabled {
   private Configuration m_configuration;

   private Session m_session;

   private Request m_request;

   private Logger m_logger;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public void handle(ThreadContext ctx) throws ThreadException {
      if (!ctx.isSignedIn() || ctx.getMainVillageId() != null) {
         return;
      }

      m_session.setProperty("server-name", m_configuration.getServerName());
      m_session.pushProperties();

      URL pageUrl = null;

      try {
         pageUrl = m_session.resolveUrl(m_request);

         m_logger.info("Get page " + pageUrl);

         // w.villageid = '4705';
         // <div id="tribeimg"> 
         //<img src="http://static2.sanguo.xiaonei.com/1.6/images/index/wei.gif" />
         String content = ThreadHelper.executeRequest(m_session, m_request, false);
         MessageFormat format = new MessageFormat("{0}w.villageid = ''{1}'';{2}tribeimg{3}/images/index/{4}.gif{5}");
         Object[] parts = format.parse(content);

         if (parts.length > 0) {
            String villageId = (String) parts[1];
            String tribe = (String) parts[4];

            ctx.setMainVillageId(villageId);
            ctx.getSession().setProperty("village-id", villageId);
            ctx.getSession().setProperty("tribe", tribe);
            m_logger.info("Village id is " + villageId + ", tribe is " + tribe);
         }
      } catch (Exception e) {
         throw new ThreadException("Error when requesting url: " + pageUrl, e);
      }
   }
}
