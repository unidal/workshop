package org.unidal.ezsell.configuration;

import java.util.ArrayList;
import java.util.List;

import org.unidal.ezsell.EbayModule;
import org.unidal.ezsell.biz.CustomsDeclarationPage;
import org.unidal.ezsell.biz.DataLoadingPage;
import org.unidal.ezsell.biz.DisputesPage;
import org.unidal.ezsell.biz.HomePage;
import org.unidal.ezsell.biz.LoginPage;
import org.unidal.ezsell.biz.MessagesPage;
import org.unidal.ezsell.biz.ProfilePage;
import org.unidal.ezsell.biz.RegisterPage;
import org.unidal.ezsell.biz.SellerTransactionPage;
import org.unidal.ezsell.biz.SellerTransactionsPage;
import org.unidal.ezsell.common.CookieManager;
import org.unidal.ezsell.common.LastUrlManager;
import org.unidal.ezsell.dal.ItemPreferenceDao;
import org.unidal.ezsell.dal.OperatorDao;
import org.unidal.ezsell.dal.PaymentDao;
import org.unidal.ezsell.dal.SellerDao;
import org.unidal.ezsell.dal.SellerEbayTeamDao;
import org.unidal.ezsell.dal.ShippingDao;
import org.unidal.ezsell.dal.TransactionDao;
import org.unidal.ezsell.feedback.FeedbackLoader;
import org.unidal.ezsell.login.LoginLogic;
import org.unidal.ezsell.payment.PayPalPaymentLoader;
import org.unidal.ezsell.register.EbayAccountLink;
import org.unidal.ezsell.register.RegisterLogic;
import org.unidal.ezsell.shipping.ShippingSummaryLoader;
import org.unidal.ezsell.transaction.TransactionLabel;
import org.unidal.ezsell.transaction.TransactionLoader;
import org.unidal.ezsell.transaction.TransactionProcessor;
import org.unidal.ezsell.view.EmailViewer;
import org.unidal.ezsell.view.FileViewer;
import org.unidal.ezsell.view.JspViewer;
import org.unidal.ezsell.view.ReportGenerator;
import org.unidal.ezsell.view.ReportViewer;

import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;

class WebConfigurator extends AbstractResourceConfigurator {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(JspViewer.class));
      all.add(C(ReportGenerator.class));
      all.add(C(ReportViewer.class).req(ReportGenerator.class));
      all.add(C(FileViewer.class));
      all.add(C(EmailViewer.class));

      all.add(C(EbayModule.class).req(RegisterPage.class, LoginPage.class, HomePage.class,
            SellerTransactionsPage.class, SellerTransactionPage.class, CustomsDeclarationPage.class,
            MessagesPage.class, DisputesPage.class, DataLoadingPage.class, ProfilePage.class));

      all.add(C(RegisterPage.class).req(JspViewer.class, EmailViewer.class).req(RegisterLogic.class,
            EbayAccountLink.class));
      all.add(C(LoginPage.class).req(JspViewer.class, LoginLogic.class, CookieManager.class));
      all.add(C(HomePage.class).req(JspViewer.class));
      all.add(C(CustomsDeclarationPage.class).req(JspViewer.class, ReportViewer.class, LastUrlManager.class,
            TransactionDao.class, ItemPreferenceDao.class));
      all.add(C(SellerTransactionsPage.class).req(JspViewer.class, ReportViewer.class, FileViewer.class,
            TransactionLabel.class, TransactionLoader.class, FeedbackLoader.class, TransactionDao.class,
            LastUrlManager.class));
      all.add(C(SellerTransactionPage.class).req(JspViewer.class, ReportViewer.class, TransactionLabel.class,
            TransactionProcessor.class, ShippingDao.class, TransactionDao.class, LastUrlManager.class));
      all.add(C(MessagesPage.class).req(JspViewer.class));
      all.add(C(DisputesPage.class).req(JspViewer.class));
      all.add(C(DataLoadingPage.class).req(JspViewer.class, PayPalPaymentLoader.class, ShippingSummaryLoader.class,
            PaymentDao.class, ShippingDao.class, TransactionDao.class));
      all.add(C(ProfilePage.class).req(JspViewer.class, OperatorDao.class, SellerDao.class, SellerEbayTeamDao.class));

      return all;
   }
}