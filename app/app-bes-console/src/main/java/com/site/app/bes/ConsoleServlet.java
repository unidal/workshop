package com.site.app.bes;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.logging.Logger;

import com.site.app.bes.console.Model;
import com.site.app.bes.console.Payload;
import com.site.app.bes.console.PayloadProvider;
import com.site.app.bes.console.Processor;
import com.site.app.bes.console.Viewer;
import com.site.lookup.ContainerLoader;

public class ConsoleServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;

   private PayloadProvider m_provider;

   private Processor m_processor;

   private Viewer m_viewer;

   private Logger m_logger;

   @Override
   public void init(ServletConfig config) throws ServletException {
      super.init(config);

      try {
         PlexusContainer container = ContainerLoader.getDefaultContainer();

         // set up Logger
         m_logger = container.getLoggerManager().getLoggerForComponent(getClass().getName());
         m_provider = (PayloadProvider) container.lookup(PayloadProvider.class);
         m_processor = (Processor) container.lookup(Processor.class);
         m_viewer = (Viewer) container.lookup(Viewer.class);
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

   @Override
   protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
      try {
         Payload payload = m_provider.getPayload(req);

         if (payload != null) {
            Model model = m_processor.process(payload);

            m_viewer.view(req, res, payload, model);
         } else {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request!");
         }
      } catch (Exception e) {
         m_logger.error("Error when handling request. " + e, e);

         res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error!");
      }
   }
}
