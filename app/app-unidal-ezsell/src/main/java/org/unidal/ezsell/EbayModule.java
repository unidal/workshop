package org.unidal.ezsell;

import static org.unidal.ezsell.EbayConstant.CUSTOMS_DECLARATION;
import static org.unidal.ezsell.EbayConstant.DATA_LOADING;
import static org.unidal.ezsell.EbayConstant.DEFAULT;
import static org.unidal.ezsell.EbayConstant.DISPUTES;
import static org.unidal.ezsell.EbayConstant.HOME;
import static org.unidal.ezsell.EbayConstant.LOGIN;
import static org.unidal.ezsell.EbayConstant.MESSAGES;
import static org.unidal.ezsell.EbayConstant.MODULE_EBAY;
import static org.unidal.ezsell.EbayConstant.PROFILE;
import static org.unidal.ezsell.EbayConstant.REGISTER;
import static org.unidal.ezsell.EbayConstant.SELLER_TRANSACTION;
import static org.unidal.ezsell.EbayConstant.SELLER_TRANSACTIONS;

import java.io.IOException;

import javax.servlet.ServletException;

import org.unidal.ezsell.biz.CustomsDeclarationPage;
import org.unidal.ezsell.biz.CustomsDeclarationPayload;
import org.unidal.ezsell.biz.DataLoadingPage;
import org.unidal.ezsell.biz.DataLoadingPayload;
import org.unidal.ezsell.biz.DisputesPage;
import org.unidal.ezsell.biz.DisputesPayload;
import org.unidal.ezsell.biz.HomePage;
import org.unidal.ezsell.biz.HomePayload;
import org.unidal.ezsell.biz.LoginPage;
import org.unidal.ezsell.biz.LoginPayload;
import org.unidal.ezsell.biz.MessagesPage;
import org.unidal.ezsell.biz.MessagesPayload;
import org.unidal.ezsell.biz.ProfilePage;
import org.unidal.ezsell.biz.ProfilePayload;
import org.unidal.ezsell.biz.RegisterPage;
import org.unidal.ezsell.biz.RegisterPayload;
import org.unidal.ezsell.biz.SellerTransactionPage;
import org.unidal.ezsell.biz.SellerTransactionPayload;
import org.unidal.ezsell.biz.SellerTransactionsPage;
import org.unidal.ezsell.biz.SellerTransactionsPayload;

import com.site.lookup.annotation.Inject;
import com.site.web.mvc.AbstractModule;
import com.site.web.mvc.annotation.ErrorActionMeta;
import com.site.web.mvc.annotation.InboundActionMeta;
import com.site.web.mvc.annotation.ModuleMeta;
import com.site.web.mvc.annotation.OutboundActionMeta;
import com.site.web.mvc.annotation.PayloadMeta;
import com.site.web.mvc.annotation.PreInboundActionMeta;
import com.site.web.mvc.annotation.TransitionMeta;

@ModuleMeta(name = MODULE_EBAY, defaultInboundAction = HOME, defaultTransition = DEFAULT, defaultErrorAction = DEFAULT)
public class EbayModule extends AbstractModule {
   @Inject
   private RegisterPage m_register;

   @Inject
   private LoginPage m_login;

   @Inject
   private HomePage m_home;

   @Inject
   private SellerTransactionsPage m_sellerTransactions;

   @Inject
   private SellerTransactionPage m_sellerTransactionDetail;

   @Inject
   private CustomsDeclarationPage m_customsDeclaration;

   @Inject
   private DataLoadingPage m_dataLoading;
   
   @Inject
   private MessagesPage m_messages;
   
   @Inject
   private DisputesPage m_disputes;

   @Inject
   private ProfilePage m_profile;

   @PreInboundActionMeta(LOGIN)
   @PayloadMeta(CustomsDeclarationPayload.class)
   @InboundActionMeta(name = CUSTOMS_DECLARATION)
   public void doCustomsDeclaration(EbayContext ctx) {
      m_customsDeclaration.handleInbound(ctx);
   }

   @PreInboundActionMeta(LOGIN)
   @PayloadMeta(DataLoadingPayload.class)
   @InboundActionMeta(name = DATA_LOADING)
   public void doDataLoading(EbayContext ctx) {
      m_dataLoading.handleInbound(ctx);
   }

   @PreInboundActionMeta(LOGIN)
   @PayloadMeta(DisputesPayload.class)
   @InboundActionMeta(name = DISPUTES)
   public void doDisputes(EbayContext ctx) {
      m_disputes.handleInbound(ctx);
   }
   
   @PreInboundActionMeta(LOGIN)
   @PayloadMeta(HomePayload.class)
   @InboundActionMeta(name = HOME)
   public void doHome(EbayContext ctx) {
      m_home.handleInbound(ctx);
   }
   
   @PayloadMeta(LoginPayload.class)
   @InboundActionMeta(name = LOGIN)
   public void doLogin(EbayContext ctx) throws ServletException, IOException {
      m_login.handleInbound(ctx);
   }

   @PreInboundActionMeta(LOGIN)
   @PayloadMeta(MessagesPayload.class)
   @InboundActionMeta(name = MESSAGES)
   public void doMessages(EbayContext ctx) {
      m_messages.handleInbound(ctx);
   }

   @PreInboundActionMeta(LOGIN)
   @PayloadMeta(ProfilePayload.class)
   @InboundActionMeta(name = PROFILE)
   public void doProfile(EbayContext ctx) {
      m_profile.handleInbound(ctx);
   }

   @PayloadMeta(RegisterPayload.class)
   @InboundActionMeta(name = REGISTER)
   public void doRegister(EbayContext ctx) throws ServletException, IOException {
      m_register.handleInbound(ctx);
   }

   @PreInboundActionMeta(LOGIN)
   @PayloadMeta(SellerTransactionPayload.class)
   @InboundActionMeta(name = SELLER_TRANSACTION)
   public void doSellerTransaction(EbayContext ctx) {
      m_sellerTransactionDetail.handleInbound(ctx);
   }

   @PreInboundActionMeta(LOGIN)
   @PayloadMeta(SellerTransactionsPayload.class)
   @InboundActionMeta(name = SELLER_TRANSACTIONS)
   public void doSellerTransactions(EbayContext ctx) {
      m_sellerTransactions.handleInbound(ctx);
   }

   @TransitionMeta(name = DEFAULT)
   public void handleTransition(EbayContext ctx) {
      // simple cases, nothing here
   }

   @ErrorActionMeta(name = DEFAULT)
   public void onError(EbayContext ctx) {
      ctx.getException().printStackTrace();
   }

   @OutboundActionMeta(name = CUSTOMS_DECLARATION)
   public void showCustomsDeclaration(EbayContext ctx) throws IOException, ServletException {
      m_customsDeclaration.handleOutbound(ctx);
   }

   @OutboundActionMeta(name = DATA_LOADING)
   public void showDataLoading(EbayContext ctx) throws IOException, ServletException {
      m_dataLoading.handleOutbound(ctx);
   }

   @OutboundActionMeta(name = DISPUTES)
   public void showDisputes(EbayContext ctx) throws IOException, ServletException {
      m_disputes.handleOutbound(ctx);
   }
   
   @OutboundActionMeta(name = HOME)
   public void showHome(EbayContext ctx) throws IOException, ServletException {
      m_home.handleOutbound(ctx);
   }
   
   @OutboundActionMeta(name = LOGIN)
   public void showLogin(EbayContext ctx) throws IOException, ServletException {
      m_login.handleOutbound(ctx);
   }

   @OutboundActionMeta(name = MESSAGES)
   public void showMessages(EbayContext ctx) throws IOException, ServletException {
      m_messages.handleOutbound(ctx);
   }

   @OutboundActionMeta(name = PROFILE)
   public void showProfile(EbayContext ctx) throws IOException, ServletException {
      m_profile.handleOutbound(ctx);
   }

   @OutboundActionMeta(name = REGISTER)
   public void showRegister(EbayContext ctx) throws IOException, ServletException {
      m_register.handleOutbound(ctx);
   }

   @OutboundActionMeta(name = SELLER_TRANSACTION)
   public void showSellerTransaction(EbayContext ctx) throws IOException, ServletException {
      m_sellerTransactionDetail.handleOutbound(ctx);
   }

   @OutboundActionMeta(name = SELLER_TRANSACTIONS)
   public void showSellerTransactions(EbayContext ctx) throws IOException, ServletException {
      m_sellerTransactions.handleOutbound(ctx);
   }
}
