package com.site.app.tracking;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.logging.Logger;

import com.site.app.tracking.counter.Configuration;
import com.site.app.tracking.counter.ImageGenerator;
import com.site.app.tracking.counter.Payload;
import com.site.app.tracking.counter.PayloadProvider;
import com.site.app.tracking.counter.Processor;
import com.site.app.tracking.counter.TestPage;
import com.site.lookup.ContainerLoader;

public class CounterServlet extends HttpServlet {
   private static final long serialVersionUID = 5305242835544877394L;

   private Configuration m_configuration;

   private Processor m_processor;

   private PayloadProvider m_provider;

   private ImageGenerator m_generator;

   private TestPage m_testPage;

   private boolean m_test = true;

   private Logger m_logger;

   private String getRemoteIp(HttpServletRequest req) {
      String remoteIp = req.getHeader("X-REAL-IP");

      // in proxy case, remoteAddr means proxy ip address
      if (remoteIp != null) {
         return remoteIp;
      } else {
         return req.getRemoteAddr();
      }
   }

   @Override
   public void init(ServletConfig config) throws ServletException {
      super.init(config);

      try {
         PlexusContainer container = ContainerLoader.getDefaultContainer();

         // set up Logger
         m_logger = ((DefaultPlexusContainer) container).getLoggerManager().getLoggerForComponent(getClass().getName());

         // set up configuration data
         m_configuration = (Configuration) container.lookup(Configuration.class);
         m_configuration.setContextPath(config.getServletContext().getServletContextName());
         m_configuration.setCheckInterval(config.getInitParameter("check-interval"));
         m_configuration.setRefreshInterval(config.getInitParameter("refresh-interval"));
         m_configuration.setCategorySourceUrl(config.getInitParameter("category-source"));

         m_provider = (PayloadProvider) container.lookup(PayloadProvider.class);
         m_generator = (ImageGenerator) container.lookup(ImageGenerator.class);
         m_testPage = (TestPage) container.lookup(TestPage.class);
         m_processor = (Processor) container.lookup(Processor.class);
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
         m_configuration.setServletPath(req.getServletPath());

         if (m_test && "1".equals(req.getParameter("test"))) {
            m_testPage.showTestPage(req, res);
         } else {
            String referer = req.getHeader("Referer");
            String queryString = req.getQueryString();

            if (referer == null) {
               referer = req.getHeader("Referrer");
            }

            Payload payload = m_provider.getPayload(referer, queryString);

            if (payload != null) {
               payload.setClientIp(getRemoteIp(req));

               int totalVisits = m_processor.process(payload);

               m_generator.generate(res.getOutputStream(), totalVisits);
            } else {
               StringBuilder sb = new StringBuilder(256);

               sb.append("Bad tracking request!");
               sb.append("<!-- ").append(referer).append(" -->");
               sb.append("<!-- ").append(queryString).append(" -->");

               res.sendError(HttpServletResponse.SC_BAD_REQUEST, sb.toString());
            }
         }
      } catch (Exception e) {
         m_logger.error("Error when handling request. " + e, e);

         res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error!");
      }
   }
}
