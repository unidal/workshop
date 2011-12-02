package org.unidal.ezsell.biz;

import java.io.IOException;

import javax.servlet.ServletException;

import org.unidal.ezsell.EbayPage;
import org.unidal.ezsell.EbayContext;
import org.unidal.ezsell.EbayModel;
import org.unidal.ezsell.view.JspViewer;

import com.site.lookup.annotation.Inject;
import com.site.web.mvc.PageHandler;

public class HomePage implements PageHandler<EbayContext> {
   @Inject
   private JspViewer m_jspViewer;

   public void handleInbound(EbayContext ctx) {
      // display only, no action here
   }

   public void handleOutbound(EbayContext ctx) throws ServletException, IOException {
      EbayModel model = new EbayModel(ctx);

      model.setPage(EbayPage.HOME);
      m_jspViewer.view(ctx, model);
   }
}
