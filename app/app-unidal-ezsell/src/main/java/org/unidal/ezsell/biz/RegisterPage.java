package org.unidal.ezsell.biz;

import static com.site.lookup.util.StringUtils.isEmpty;

import java.io.IOException;

import javax.servlet.ServletException;

import org.unidal.ezsell.EbayPage;
import org.unidal.ezsell.EbayContext;
import org.unidal.ezsell.dal.Seller;
import org.unidal.ezsell.dal.Operator;
import org.unidal.ezsell.register.EbayAccountLink;
import org.unidal.ezsell.register.RegisterLogic;
import org.unidal.ezsell.register.RegisterModel;
import org.unidal.ezsell.register.RegisterStep;
import org.unidal.ezsell.register.OperatorAlreadyExistedException;
import org.unidal.ezsell.register.OperatorStatus;
import org.unidal.ezsell.view.EmailId;
import org.unidal.ezsell.view.EmailViewer;
import org.unidal.ezsell.view.JspViewer;

import com.site.dal.jdbc.DalException;
import com.site.lookup.annotation.Inject;
import com.site.web.mvc.ErrorObject;
import com.site.web.mvc.PageHandler;

public class RegisterPage implements PageHandler<EbayContext> {
   @Inject
   private JspViewer m_jspViewer;

   @Inject
   private EmailViewer m_emailViewer;

   @Inject
   private RegisterLogic m_registerLogic;

   @Inject
   private EbayAccountLink m_accountLink;

   private boolean m_emailOn = false;

   public void handleInbound(EbayContext ctx) throws ServletException, IOException {
      if (!ctx.hasErrors()) {
         RegisterPayload payload = (RegisterPayload) ctx.getPayload();

         normalize(ctx);

         if (payload.isSubmit()) {
            switch (payload.getStep()) {
            case USER_FORM: // user form submit
               if (!isEmpty(payload.getEmail()) && !isEmpty(payload.getPassword())) {
                  try {
                     Operator user = m_registerLogic.addOperator(payload.getEmail(), payload.getPassword());

                     ctx.setUser(user);
                     payload.setNextStep();

                     if (m_emailOn) {
                        sendRegisterConfirmEmail(ctx);
                     }
                  } catch (DalException e) {
                     ctx.addError(new ErrorObject("dal.user.error", e));
                  } catch (OperatorAlreadyExistedException e) {
                     ctx.addError(new ErrorObject("dal.user.existed", e));
                  }
               } else {
                  if (isEmpty(payload.getEmail())) {
                     ctx.addError(new ErrorObject("email.required", null));
                  }

                  if (isEmpty(payload.getPassword())) {
                     ctx.addError(new ErrorObject("password.required", null));
                  }
               }

               break;
            case EMAIL_SENT: // email click back
               int userId = m_registerLogic.decodeToken(payload.getToken()).getOperatorId();

               try {
                  Operator user = m_registerLogic.changeOperatorStatus(userId, OperatorStatus.EMAIL_CONFIRMED);

                  ctx.setUser(user);
                  payload.setUserId(userId);
                  payload.setNextStep();
               } catch (DalException e) {
                  ctx.addError(new ErrorObject("dal.user.error", e));
               }

               break;
            case SELLER_FORM: // seller form submit
               String account = payload.getEbayAccount();
               String authToken = payload.getEbayAuthToken();

               if (!isEmpty(account)) {
                  Seller seller = m_registerLogic.bindSeller(payload.getUserId(), account);

                  if (seller != null) {
                     ctx.setSeller(seller);
                     payload.setNextStep();
                  } else if (isEmpty(authToken)) {
                     String url = m_accountLink.getAccountLinkUrl(account);

                     payload.setEbayAccountLinkUrl(url);
                  } else {
                     if (authToken.length() < m_accountLink.getAuthTokenMinLength()) {
                        ctx.addError(new ErrorObject("authToken.invalid", null));
                     } else {
                        try {
                           seller = m_registerLogic.addSeller(payload.getUserId(), account, authToken);

                           ctx.setSeller(seller);
                           payload.setNextStep();
                        } catch (DalException e) {
                           ctx.addError(new ErrorObject("dal.seller.error", e));
                        }
                     }
                  }
               }

               break;
            case COMPLETED:
               break;
            }
         }
      }
   }

   public void handleOutbound(EbayContext ctx) throws ServletException, IOException {
      RegisterPayload payload = (RegisterPayload) ctx.getPayload();
      RegisterModel model = new RegisterModel(ctx);

      model.setPage(EbayPage.REGISTER);

      switch (payload.getStep()) {
      case USER_FORM:
         break;
      case EMAIL_SENT:
         model.setUser(ctx.getUser());
         model.setToken(m_registerLogic.encodeToken(ctx.getUser()));
         break;
      case SELLER_FORM:
         break;
      case COMPLETED:
         break;
      }

      m_jspViewer.view(ctx, model);
   }

   private void normalize(EbayContext ctx) {
      RegisterPayload payload = (RegisterPayload) ctx.getPayload();

      if (payload.getToken() != null) { // email click back
         payload.setStep(RegisterStep.EMAIL_SENT);
         payload.setSubmit(true);
      } else if (payload.getStep() == null) {
         payload.setStep(RegisterStep.USER_FORM);
      }
   }

   private void sendRegisterConfirmEmail(EbayContext ctx) throws ServletException, IOException {
      RegisterModel model = new RegisterModel(ctx);

      model.setEmailId(EmailId.REGISTER_CONFIRM);
      model.setUser(ctx.getUser());

      m_emailViewer.view(ctx, model);
   }
}
