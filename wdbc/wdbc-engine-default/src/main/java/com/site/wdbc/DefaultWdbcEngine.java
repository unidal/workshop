package com.site.wdbc;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.lookup.ContainerHolder;
import com.site.wdbc.query.WdbcHandler;
import com.site.wdbc.query.WdbcParser;

public class DefaultWdbcEngine extends ContainerHolder implements WdbcEngine, LogEnabled {
   private Logger m_logger;

   public WdbcResult execute(WdbcQuery query, WdbcSource source) throws WdbcException {
      if (query == null) {
         throw new WdbcException("Query expected!");
      } else if (source == null) {
         throw new WdbcException("No source found!");
      }

      WdbcHandler handler = lookup(WdbcHandler.class);
      WdbcParser parser = null;

      try {
         handler.setQuery(query);
         parser = lookup(WdbcParser.class, source.getType().toString());
         parser.parse(handler, source);
      } catch (WdbcException e) {
         m_logger.warn("Error when parsing the response as type " + source.getType() + ", message: " + e);
         m_logger.info("Try to parse it again as type HTML");

         release(parser);
         parser = lookup(WdbcParser.class, WdbcSourceType.HTML.toString());
         parser.parse(handler, source);
      } finally {
         release(handler);
         release(parser);
      }

      return handler.getResult();
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }
}
