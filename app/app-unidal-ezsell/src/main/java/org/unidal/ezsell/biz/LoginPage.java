package org.unidal.ezsell.biz;

import static com.site.lookup.util.StringUtils.isEmpty;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.unidal.ezsell.EbayPage;
import org.unidal.ezsell.EbayContext;
import org.unidal.ezsell.EbayModel;
import org.unidal.ezsell.common.CookieManager;
import org.unidal.ezsell.dal.Seller;
import org.unidal.ezsell.login.LoginLogic;
import org.unidal.ezsell.login.TokenManager;
import org.unidal.ezsell.view.JspViewer;

import com.site.app.tag.function.Encoder;
import com.site.dal.jdbc.DalException;
import com.site.lookup.annotation.Inject;
import com.site.web.mvc.ErrorObject;
import com.site.web.mvc.PageHandler;

public class LoginPage implements PageHandler<EbayContext>, LogEnabled {
   @Inject
   private JspViewer m_jspViewer;

   @Inject
   private LoginLogic m_logic;

   @Inject
   private CookieManager m_cookieManager;

   private Logger m_logger;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   public void handleInbound(EbayContext ctx) throws ServletException, IOException {
      TokenManager tokenManager = new TokenManager(m_cookieManager, ctx.getHttpServletRequest(), ctx
            .getHttpServletResponse());
      LoginPayload payload = (LoginPayload) ctx.getPayload();
      String token = tokenManager.getToken();
      boolean showLogin = true;
      int userId = 0;

      if (payload.getPage() == EbayPage.LOGIN) {
         if (isEmpty(payload.getRtnUrl())) {
            payload.setRtnUrl(ctx.getRequestContext().getActionUri(EbayPage.HOME.getName()));
         }

         if (payload.isLogin()) { // submit
            String email = payload.getEmail();
            String password = payload.getPassword();

            if (!isEmpty(email) && !isEmpty(password)) {
               try {
                  m_logic.login(tokenManager, email, password);

                  redirect(ctx, payload.getRtnUrl());
                  return;
               } catch (Exception e) {
                  ctx.addError(new ErrorObject("login.failure", e));
               }
            } else {
               ctx.addError(new ErrorObject("login.input.invalid", null));
            }
         }
      } else if (token != null) {
         userId = tokenManager.validateToken(token);

         if (userId >= 0) {
            showLogin = false;
         }
      }

      if (showLogin) {
         ctx.skipAction();
      } else {
         try {
            EbayContext parent = (EbayContext) ctx.getParent();

            if (parent != null) {
               Seller seller = m_logic.getSeller(userId);

               parent.setLoginUserId(userId);
               parent.setSeller(seller);

               logAccess(ctx, userId, seller);
            }
         } catch (DalException e) {
            m_logger.info("No seller found for user id: " + userId);
            ctx.addError(new ErrorObject("seller.dal.notFound", e));
            ctx.skipAction();
         }
      }
   }

   @SuppressWarnings("unchecked")
   private void logAccess(EbayContext ctx, int userId, Seller seller) {
      StringBuilder sb = new StringBuilder(256);
      SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
      HttpServletRequest request = ctx.getHttpServletRequest();
      String actionUri = ctx.getRequestContext().getActionUri();

      sb.append(dateFormat.format(new Date()));
      sb.append(" ").append(seller.getEbayAccount()).append('/').append(userId).append(' ');

      if (request.getMethod().equalsIgnoreCase("post")) {
         Enumeration<String> names = request.getParameterNames();
         boolean hasQuestion = actionUri.indexOf('?') >= 0;

         sb.append(actionUri);

         while (names.hasMoreElements()) {
            String name = names.nextElement();
            String[] attributes = request.getParameterValues(name);

            for (String attribute : attributes) {
               if (attribute.length() > 0) {
                  if (!hasQuestion) {
                     sb.append('?');
                     hasQuestion = true;
                  } else {
                     sb.append('&');
                  }

                  sb.append(name).append('=').append(Encoder.urlEncode(attribute));
               }
            }
         }
      } else {
         sb.append(actionUri);
      }

      m_logger.info(sb.toString());
   }

   public void handleOutbound(EbayContext ctx) throws ServletException, IOException {
      EbayModel model = new EbayModel(ctx);
      LoginPayload payload = (LoginPayload) ctx.getPayload();

      model.setPage(EbayPage.LOGIN);

      if (payload.getRtnUrl() == null && ctx.getParent() != null) {
         HttpServletRequest request = ctx.getHttpServletRequest();
         String qs = request.getQueryString();
         String requestURI = request.getRequestURI();

         if (qs != null) {
            payload.setRtnUrl(requestURI + "?" + qs);
         } else {
            payload.setRtnUrl(requestURI);
         }
      }

      m_jspViewer.view(ctx, model);
   }

   private void redirect(EbayContext ctx, String url) {
      HttpServletResponse response = ctx.getHttpServletResponse();

      response.setHeader("location", url);
      response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
      ctx.stopProcess();
   }
}
