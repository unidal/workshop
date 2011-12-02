package com.site.wdbc.http.impl;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.wdbc.WdbcEngine;
import com.site.wdbc.WdbcException;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.WdbcSource;
import com.site.wdbc.http.Handler;
import com.site.wdbc.http.Processor;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.Response;
import com.site.wdbc.http.Session;

public class DefaultHandler implements Handler, LogEnabled {
   private WdbcEngine m_engine;

   private List<Request> m_requests;

   private List<Handler> m_children;

   private Processor m_processor;

   private WdbcQuery m_query;

   private boolean m_handleCurrentPage;

   private Logger m_logger;

   private long m_interval;

   public DefaultHandler() {
      m_requests = new ArrayList<Request>(3);
   }

   public void addRequest(Request request) {
      m_requests.add(request);
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public void execute(Session session) throws WdbcException {
      if (m_query != null) {
         WdbcSource source = session.getResponseSource();
         String queryName = m_query.getName();

         if (source != null) {
            if (m_handleCurrentPage && m_children != null) {
               m_logger.info("Handling current page content first");

               for (Handler child : m_children) {
                  child.execute(session);
               }

               m_logger.info("End of Handling current page");
            }

            m_logger.info("Executing Query: " + queryName);

            WdbcResult result = m_engine.execute(m_query, source);
            String[] columns = result.getColumns();
            int rows = result.getRowSize();

            m_logger.info(rows + " rows filtered");

            for (int i = 0; i < rows; i++) {
               for (int j = 0; j < columns.length; j++) {
                  Object cell = result.getCell(i, j);

                  session.setProperty(queryName + ":" + columns[j], cell);
               }

               if (m_children.size() > 0) {
                  m_logger.info("Processing query result of " + queryName + ", row " + (i + 1) + " of " + rows);
               } else {
                  m_logger.debug("Processing query result of " + queryName + ", row " + (i + 1) + " of " + rows);
               }

               executeOnce(session);
            }
         } else {
            throw new WdbcException("No valid HttpResponse within Session, can't execute Query: " + queryName);
         }
      } else {
         executeOnce(session);
      }
   }

   private void executeOnce(Session session) throws WdbcException {
      Response lastResponse = null;

      try {
         for (Request request : m_requests) {
            lastResponse = request.execute(session);
         }
      } catch (SocketException e) {
         // ignore it
      } catch (SocketTimeoutException e) {
         // ignore it
      } catch (Exception e) {
         throw new WdbcException("Error when executing request, " + e, e);
      }

      if (lastResponse != null) {
         try {
            session.setResponseSource(new HttpResponseSource(lastResponse));
         } catch (IOException e) {
            throw new WdbcException("Error when executing request, " + e, e);
         }

         if (m_children != null) {
            int size = m_children.size();

            for (int i = 0; i < size; i++) {
               Handler child = m_children.get(i);

               if (i == 0) { // first one
                  session.pushProperties();
               }

               child.execute(session);

               if (i == size - 1) { // last one
                  session.popProperties();
               }
            }
         }
      }

      if (m_processor != null) {
         m_processor.execute(session);
      }

      if (m_interval > 0) {
         try {
            long millis = (long) (m_interval / 2 + m_interval * Math.random());

            m_logger.info("Sleeping " + millis + " milliseconds");
            Thread.sleep(millis);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
   }

   public void setChildren(List<Handler> children) {
      m_children = children;
   }

   public void setProcessor(Processor processor) {
      m_processor = processor;
   }

   public void setQuery(WdbcQuery query) {
      m_query = query;
   }

   public void setHandleCurrentPage(boolean handleCurrentPage) {
      m_handleCurrentPage = handleCurrentPage;
   }

   public void setInterval(long seconds) {
      m_interval = seconds * 1000L;
   }
}
