package org.unidal.ezsell.callback;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.plexus.PlexusContainer;

import com.site.web.AbstractContainerServlet;

public class NotificationCallbackServlet extends AbstractContainerServlet {
   private static final long serialVersionUID = 1L;

   private NotificationCallbackHandler m_handler;

   @Override
   public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
      HttpServletRequest request = (HttpServletRequest) req;
      HttpServletResponse response = (HttpServletResponse) res;

      request.setCharacterEncoding("UTF-8");
      response.setContentType("text/html;charset=UTF-8");

      try {
         m_handler.handle(request, response);
      } catch (Throwable t) {
         String message = "Error occured when handling uri: " + request.getRequestURI();

         getLogger().error(message, t);

         if (!response.isCommitted()) {
            response.sendError(500, message);
         }
      }
   }

   @Override
   protected void initComponents(ServletConfig config) throws Exception {
      PlexusContainer container = getContainer();

      m_handler = (NotificationCallbackHandler) container.lookup(NotificationCallbackHandler.class);
   }
}
