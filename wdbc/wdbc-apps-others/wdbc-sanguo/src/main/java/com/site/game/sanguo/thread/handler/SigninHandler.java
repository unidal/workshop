package com.site.game.sanguo.thread.handler;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;

import org.apache.http.HttpException;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.game.sanguo.Configuration;
import com.site.game.sanguo.thread.ThreadContext;
import com.site.game.sanguo.thread.ThreadException;
import com.site.game.sanguo.thread.ThreadHandler;
import com.site.game.sanguo.thread.ThreadHelper;
import com.site.wdbc.http.Request;
import com.site.wdbc.http.Session;
import com.site.wdbc.http.impl.FormRequest;

public class SigninHandler implements ThreadHandler, LogEnabled {
   private Configuration m_configuration;

   private Session m_session;

   private Request m_request;

   private Logger m_logger;

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   private String getLandingPageUrl() {
      return String.format("http://%s/", m_configuration.getServerName());
   }

   private String getLoginFormUrl(String content) throws ParseException {
      MessageFormat format = new MessageFormat("{0}top.location=\"{1}\"{2}");

      Object[] parts = format.parse(content);
      return (String) parts[1];
   }

   public void handle(ThreadContext ctx) throws ThreadException {
      if (ctx.isSignedIn()) {
         return;
      }

      ctx.setSession(m_session);

      m_session.pushProperties();
      m_session.setProperty("login-url", m_configuration.getLoginUrl());
      m_session.setProperty("login-email", m_configuration.getLoginEmail());
      m_session.setProperty("login-password", m_configuration.getLoginPassword());
      m_session.setProperty("landing-page-url", getLandingPageUrl());

      m_logger.info("Login to " + m_configuration.getLoginUrl() + " with " + m_configuration.getLoginEmail() + " ...");

      int maxRetries = 2;
      
      while (!ctx.isSignedIn() && maxRetries > 0) {
         try {
            signin(ctx);
         } catch (Exception e) {
            throw new ThreadException("Error when requesting url: " + m_configuration.getLoginUrl(), e);
         }

         if (!ctx.isSignedIn()) {
            m_logger.warn("Login failure, waiting for one minute and retry ...");
            maxRetries--;
            
            try {
               Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
               break;
            }
         }
      }
   }

   private void signin(ThreadContext ctx) throws ParseException, HttpException, IOException, ThreadException {
      String content = ThreadHelper.executeRequest(m_session, m_request, false);

      if (content.contains("Loading...")) {
         String landingPageUrl = getLandingPageUrl();
         String loginFormUrl = getLoginFormUrl(content);
         FormRequest loginForm = new FormRequest();

         loginForm.enableLogging(m_logger);
         loginForm.setAction(landingPageUrl + loginFormUrl);
         ThreadHelper.executeRequest(m_session, loginForm, false);

         m_logger.info("Logged in successfully.");
         ctx.setSignedIn(true);
      }
   }
}
