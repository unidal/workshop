package com.site.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.logging.Logger;

import com.site.lookup.ContainerLoader;

public abstract class AbstractContainerServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;

   private PlexusContainer m_container;

   private Logger m_logger;

   @Override
   public void init(ServletConfig config) throws ServletException {
      super.init(config);

      try {
         if (m_container == null) {
            m_container = ContainerLoader.getDefaultContainer();
         }

         m_logger = m_container.getLoggerManager().getLoggerForComponent(getClass().getName());

         initComponents(config);
      } catch (Exception e) {
         if (m_logger != null) {
            m_logger.error("Servlet initializing failed. " + e, e);
         } else {
            System.err.println("Servlet initializing failed. " + e);
            e.printStackTrace();
         }

         throw new ServletException("Servlet initializing failed. " + e, e);
      }
   }

   protected abstract void initComponents(ServletConfig config) throws Exception;

   // For test purpose
   public AbstractContainerServlet setContainer(PlexusContainer container) {
      m_container = container;
      return this;
   }

   protected PlexusContainer getContainer() {
      return m_container;
   }

   protected Logger getLogger() {
      return m_logger;
   }
}
