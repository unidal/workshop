package org.unidal.ezsell.configuration;

import java.util.ArrayList;
import java.util.List;

import org.unidal.ezsell.api.ebay.EbayApiManager;
import org.unidal.ezsell.api.ebay.trading.CompleteSale;
import org.unidal.ezsell.api.ebay.trading.GetFeedback;
import org.unidal.ezsell.api.ebay.trading.wdbc.WdbcMapping;
import org.unidal.ezsell.api.paypal.PaypalApiManager;
import org.unidal.ezsell.api.paypal.PaypalMapping;
import org.unidal.ezsell.common.CookieManager;
import org.unidal.ezsell.common.LastUrlManager;
import org.unidal.ezsell.dal.BuyerDao;
import org.unidal.ezsell.dal.FeedbackDao;
import org.unidal.ezsell.dal.ItemDao;
import org.unidal.ezsell.dal.LabelDao;
import org.unidal.ezsell.dal.OperatorDao;
import org.unidal.ezsell.dal.OrderDao;
import org.unidal.ezsell.dal.PaymentDao;
import org.unidal.ezsell.dal.SellerDao;
import org.unidal.ezsell.dal.ShippingAddressDao;
import org.unidal.ezsell.dal.TransactionDao;
import org.unidal.ezsell.feedback.FeedbackLoader;
import org.unidal.ezsell.login.LoginLogic;
import org.unidal.ezsell.payment.PayPalPaymentLoader;
import org.unidal.ezsell.register.EbayAccountLink;
import org.unidal.ezsell.register.RegisterLogic;
import org.unidal.ezsell.shipping.AddressBuilder;
import org.unidal.ezsell.shipping.AddressManager;
import org.unidal.ezsell.shipping.CSPAddressBuilder;
import org.unidal.ezsell.shipping.PCSAddressBuilder;
import org.unidal.ezsell.shipping.ShippingSummaryLoader;
import org.unidal.ezsell.transaction.TransactionLabel;
import org.unidal.ezsell.transaction.TransactionLoader;
import org.unidal.ezsell.transaction.TransactionProcessor;

import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;

class LogicConfigurator extends AbstractResourceConfigurator {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.addAll(defineAddressComponents());

      all.add(C(CookieManager.class));
      all.add(C(LastUrlManager.class).req(CookieManager.class));
      all.add(C(TransactionLabel.class).req(TransactionDao.class, LabelDao.class));
      all.add(C(PayPalPaymentLoader.class));
      all.add(C(ShippingSummaryLoader.class));
      all.add(C(TransactionLoader.class).req(EbayApiManager.class, WdbcMapping.class, PaypalApiManager.class,
            PaypalMapping.class, SellerDao.class, TransactionDao.class, ItemDao.class, OrderDao.class, BuyerDao.class,
            ShippingAddressDao.class, PaymentDao.class));
      all.add(C(TransactionProcessor.class).req(EbayApiManager.class, CompleteSale.class));
      all.add(C(FeedbackLoader.class).req(EbayApiManager.class, GetFeedback.class, SellerDao.class, FeedbackDao.class,
            TransactionDao.class));

      all.add(C(RegisterLogic.class).req(OperatorDao.class, SellerDao.class, EbayAccountLink.class));
      all.add(C(LoginLogic.class).req(OperatorDao.class, SellerDao.class));

      return all;
   }

   private List<Component> defineAddressComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(AddressManager.class).req(ShippingAddressDao.class));
      all.add(C(AddressBuilder.class, "default", CSPAddressBuilder.class));
      all.add(C(AddressBuilder.class, "Germany", PCSAddressBuilder.class));
      all.add(C(AddressBuilder.class, "Belgium", PCSAddressBuilder.class));
      all.add(C(AddressBuilder.class, "Switzerland", PCSAddressBuilder.class));
      all.add(C(AddressBuilder.class, "Netherlands", PCSAddressBuilder.class));
      all.add(C(AddressBuilder.class, "Italy", PCSAddressBuilder.class));
      all.add(C(AddressBuilder.class, "Singapore", PCSAddressBuilder.class));

      // no stateOrProvince?
      all.add(C(AddressBuilder.class, "France", PCSAddressBuilder.class));

      // stateOrProvince is 'default'?
      all.add(C(AddressBuilder.class, "Denmark", CSPAddressBuilder.class));

      return all;
   }

}