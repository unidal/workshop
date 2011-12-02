package org.unidal.ezsell.configuration;

import java.util.ArrayList;
import java.util.List;

import org.unidal.ezsell.cache.CacheManager;
import org.unidal.ezsell.cache.OperatorDaoCache;
import org.unidal.ezsell.cache.SellerDaoCache;
import org.unidal.ezsell.cache.SimpleCacheManager;
import org.unidal.ezsell.dal.BuyerDao;
import org.unidal.ezsell.dal.ConfigurationDao;
import org.unidal.ezsell.dal.FeedbackDao;
import org.unidal.ezsell.dal.ItemDao;
import org.unidal.ezsell.dal.ItemPreferenceDao;
import org.unidal.ezsell.dal.LabelDao;
import org.unidal.ezsell.dal.NotificationDao;
import org.unidal.ezsell.dal.OperatorDao;
import org.unidal.ezsell.dal.OrderDao;
import org.unidal.ezsell.dal.PaymentDao;
import org.unidal.ezsell.dal.SellerDao;
import org.unidal.ezsell.dal.SellerEbayTeamDao;
import org.unidal.ezsell.dal.ShippingAddressDao;
import org.unidal.ezsell.dal.ShippingDao;
import org.unidal.ezsell.dal.TransactionDao;

import com.site.dal.jdbc.QueryEngine;
import com.site.dal.jdbc.configuration.AbstractJdbcResourceConfigurator;
import com.site.lookup.configuration.Component;

final class DatabaseConfigurator extends AbstractJdbcResourceConfigurator {
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();
      String unidal = "jdbc-unidal";

      all.add(C(CacheManager.class, "simple", SimpleCacheManager.class).is(PER_LOOKUP));

      all.add(defineJdbcDataSourceConfigurationManagerComponent("datasource.xml"));
      all.add(defineJdbcDataSourceComponent(unidal, "${jdbc.driver}", "${jdbc.url.unidal}", "${jdbc.user}",
            "${jdbc.password}", "<![CDATA[${jdbc.connectionProperties}]]>"));
      all.add(defineSimpleTableProviderComponent(unidal, "buyer"));
      all.add(defineSimpleTableProviderComponent(unidal, "configuration"));
      all.add(defineSimpleTableProviderComponent(unidal, "feedback"));
      all.add(defineSimpleTableProviderComponent(unidal, "item"));
      all.add(defineSimpleTableProviderComponent(unidal, "item-preference", "item_preference"));
      all.add(defineSimpleTableProviderComponent(unidal, "label"));
      all.add(defineSimpleTableProviderComponent(unidal, "notification"));
      all.add(defineSimpleTableProviderComponent(unidal, "operator"));
      all.add(defineSimpleTableProviderComponent(unidal, "orders"));
      all.add(defineSimpleTableProviderComponent(unidal, "payment"));
      all.add(defineSimpleTableProviderComponent(unidal, "seller"));
      all.add(defineSimpleTableProviderComponent(unidal, "seller-ebay-team", "seller_ebay_team"));
      all.add(defineSimpleTableProviderComponent(unidal, "shipping"));
      all.add(defineSimpleTableProviderComponent(unidal, "shipping-address", "shipping_address"));
      all.add(defineSimpleTableProviderComponent(unidal, "transaction"));

      all.add(C(BuyerDao.class).req(QueryEngine.class));
      all.add(C(ConfigurationDao.class).req(QueryEngine.class));
      all.add(C(FeedbackDao.class).req(QueryEngine.class));
      all.add(C(ItemDao.class).req(QueryEngine.class));
      all.add(C(ItemPreferenceDao.class).req(QueryEngine.class));
      all.add(C(LabelDao.class).req(QueryEngine.class));
      all.add(C(NotificationDao.class).req(QueryEngine.class));
      all.add(C(OperatorDao.class, OperatorDaoCache.class).req(QueryEngine.class).req(CacheManager.class, "simple"));
      all.add(C(OrderDao.class).req(QueryEngine.class));
      all.add(C(PaymentDao.class).req(QueryEngine.class));
      all.add(C(SellerDao.class, SellerDaoCache.class).req(QueryEngine.class).req(CacheManager.class, "simple"));
      all.add(C(SellerEbayTeamDao.class).req(QueryEngine.class));
      all.add(C(ShippingDao.class).req(QueryEngine.class));
      all.add(C(ShippingAddressDao.class).req(QueryEngine.class));
      all.add(C(TransactionDao.class).req(QueryEngine.class));

      return all;
   }
}