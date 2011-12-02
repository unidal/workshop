package org.unidal.ezsell.view;

import org.unidal.ezsell.EbayContext;
import org.unidal.ezsell.EbayModel;
import org.unidal.ezsell.EbayPage;
import org.unidal.ezsell.biz.RegisterPayload;

import com.site.web.mvc.Action;
import com.site.web.mvc.view.BaseJspViewer;

public class JspViewer extends BaseJspViewer<EbayPage, Action, EbayContext, EbayModel> {
   @Override
   protected String getJspFilePath(EbayContext ctx, EbayModel model) {
      switch (model.getPage()) {
      case REGISTER:
         RegisterPayload payload = (RegisterPayload) ctx.getPayload();

         switch (payload.getStep()) {
         case USER_FORM:
            return JspFile.REGISTER_USER_FORM.getPath();
         case EMAIL_SENT:
            return JspFile.REGISTER_EMAIL_SENT.getPath();
         case SELLER_FORM:
            return JspFile.REGISTER_SELLER_FORM.getPath();
         case COMPLETED:
            return JspFile.REGISTER_COMPLETED.getPath();
         }
         
         break;
      case LOGIN:
         return JspFile.LOGIN.getPath();
      case HOME:
         return JspFile.HOME.getPath();
      case SELLER_TRANSACTIONS:
         return JspFile.SELLER_TRANSACTIONS.getPath();
      case SELLER_TRANSACTION:
         return JspFile.SELLER_TRANSACTION.getPath();
      case CUSTOMS_DECLARATION:
         return JspFile.CUSTOMS_DECLARATION.getPath();
      case DATA_LOADING:
         return JspFile.DATA_LOADING.getPath();
      case MESSAGES:
         return JspFile.MESSAGES.getPath();
      case DISPUTES:
         return JspFile.DISPUTES.getPath();
      case PROFILE:
         return JspFile.PROFILE.getPath();
      }

      throw new RuntimeException("Unknown action: " + model.getPage());
   }
}
