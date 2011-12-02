package org.unidal.ezsell.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.unidal.ezsell.EbayContext;
import org.unidal.ezsell.EbayModel;
import org.unidal.ezsell.EbayPayload;
import org.unidal.ezsell.TestServer;

import com.site.test.junit.HttpTestCase;
import com.site.test.server.EmbeddedServer;

public class EmailViewerTest extends HttpTestCase {
   @Override
   protected void configure(EmbeddedServer server) {
      server.addServlet(new EmailServlet(), "email-servlet", "/email/*");
   }

   @Test
   public void setup() throws Exception {
      main(new TestServer());
   }

   public void testEmail() throws Exception {
      checkRequest("/email", "DONE");
   }

   public static class EmailServlet extends HttpServlet {
      private static final long serialVersionUID = 1L;

      @Override
      protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
         EbayContext ctx = new EbayContext();
         EbayModel model = new EbayModel(ctx);
         EmailViewer viewer = new EmailViewer();

         ctx.initialize(request, response);
         ctx.setPayload(new EbayPayload());
         model.setEmailId(EmailId.REGISTER_CONFIRM);

         viewer.view(ctx, model);
         
         response.getOutputStream().print("DONE");
      }
   }
}
