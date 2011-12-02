package com.site.wdbc.http.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.site.lookup.ContainerHolder;
import com.site.lookup.LookupException;
import com.site.wdbc.WdbcException;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcSource;
import com.site.wdbc.WdbcSourceType;
import com.site.wdbc.http.Flow;
import com.site.wdbc.http.Handler;
import com.site.wdbc.http.Processor;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.Session;

public class DefaultFlow extends ContainerHolder implements Flow, LogEnabled, Initializable {
   private Session m_session;

   private Handler m_handler;

   private Logger m_logger;

   private PlexusConfiguration m_config;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public void execute() throws WdbcException {
      if (m_handler != null) {
         // setup empty source
         m_session.setResponseSource(new WdbcSource() {
            public Reader getReader() throws IOException {
               return new StringReader("<html><body></body></html>");
            }

            public WdbcSourceType getType() {
               return WdbcSourceType.HTML;
            }
         });

         m_session.pushProperties();
         m_handler.execute(m_session);
      } else {
         m_logger.warn("No Handler defined, nothing done");
      }
   }

   private Handler createHandler(PlexusConfiguration config) {
      String handlerName = config.getAttribute("name", null);
      String queryName = config.getAttribute("query", null);
      String handleCurrentPage = config.getAttribute("handle-current-page", null);
      String interval = config.getAttribute("interval", null);
      Handler handler = lookup(Handler.class, handlerName);

      if (queryName != null) {
         WdbcQuery query = lookup(WdbcQuery.class, queryName);

         query.setName(queryName);
         handler.setQuery(query);
      }

      if ("true".equals(handleCurrentPage)) {
         handler.setHandleCurrentPage(true);
      }

      if (interval != null) {
         try {
            handler.setInterval(Integer.parseInt(interval));
         } catch (NumberFormatException e) {
            // ignore it
            m_logger.warn("Invalid interval: " + interval + ", ignored");
         }
      }

      PlexusConfiguration[] pages = config.getChildren("page");
      for (PlexusConfiguration page : pages) {
         String requestName = page.getAttribute("name", null);
         Request request = lookup(Request.class, requestName);

         handler.addRequest(request);
      }

      PlexusConfiguration[] children = config.getChildren("handler");
      if (children != null) {
         List<Handler> childHandlers = new ArrayList<Handler>(children.length);

         for (PlexusConfiguration child : children) {
            Handler childHandler = createHandler(child);

            childHandlers.add(childHandler);
         }

         handler.setChildren(childHandlers);
      }

      PlexusConfiguration process = config.getChild("processor", false);
      if (process != null) {
         String processorName = process.getAttribute("name", null);
         Processor processor = lookup(Processor.class, processorName);

         handler.setProcessor(processor);
      }

      return handler;
   }

   public void initialize() throws InitializationException {
      try {
         m_handler = createHandler(m_config);
      } catch (LookupException e) {
         throw new IllegalArgumentException(e.getMessage(), e);
      }

      if (m_session == null) {
         try {
            m_session = lookup(Session.class);

            m_logger.info("No specific Session configured, use default Session");
         } catch (LookupException e) {
            throw new InitializationException("Error when lookup default Session", e);
         }
      }
   }

   public void setHandler(PlexusConfiguration config) {
      m_config = config;
   }
}
