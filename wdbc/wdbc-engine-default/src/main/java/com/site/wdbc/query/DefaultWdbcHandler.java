package com.site.wdbc.query;

import java.util.Map;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;

public class DefaultWdbcHandler implements WdbcHandler, LogEnabled {
   private Logger m_logger;

   private WdbcQuery m_query;

   private WdbcContext m_context;

   private WdbcResult m_result;

   private boolean m_debug;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public WdbcResult getResult() {
      return m_result;
   }

   public void handleComment(String comment) {
      m_context.setComment(comment);
      m_query.handleEvent(m_context, m_result, WdbcEventType.COMMENT);
   }

   public void handleEndDocument() {
      m_query.handleEvent(m_context, m_result, WdbcEventType.END_DOCUMENT);
   }

   public void handleEndTag(String tagName) {
      m_query.handleEvent(m_context, m_result, WdbcEventType.END_TAG);
      m_context.pop(tagName);
   }

   public void handleError(String message, Throwable cause) {
      if (m_debug) {
         m_logger.warn(message, cause);
      }
   }

   public void handleStartDocument() {
      m_query.handleEvent(m_context, m_result, WdbcEventType.START_DOCUMENT);
   }

   public void handleStartTag(String tagName, Map<String, String> attributes) {
      m_context.push(tagName, attributes);
      m_query.handleEvent(m_context, m_result, WdbcEventType.START_TAG);
   }

   public void handleText(String text) {
      m_context.setText(text);
      m_query.handleEvent(m_context, m_result, WdbcEventType.TEXT);
   }

   public void setQuery(WdbcQuery query) {
      m_query = query;
      m_context.setTagTree(query.buildTagTree());
   }
}
