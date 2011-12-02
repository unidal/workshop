package org.unidal.ezsell.api.ebay;

import java.util.HashMap;
import java.util.Map;

import org.unidal.ezsell.api.ebay.shopping.Shopping;
import org.unidal.ezsell.api.ebay.trading.Trading;
import org.unidal.ezsell.dal.Seller;

import com.site.lookup.ContainerHolder;

public class EbayApiManager extends ContainerHolder {
   private Map<String, Shopping> m_shoppings = new HashMap<String, Shopping>();

   private Map<String, Trading> m_tradings = new HashMap<String, Trading>();

   public Shopping getShopping(Seller seller) {
      String account = seller.getEbayAccount();
      Shopping shopping = m_shoppings.get(account);

      if (shopping == null) {
         synchronized (m_shoppings) {
            shopping = m_shoppings.get(account);

            if (shopping == null) {
               shopping = lookup(Shopping.class);

               configure(seller, shopping, false);
               m_shoppings.put(account, shopping);
            }
         }
      }

      return shopping;
   }

   public Trading getTrading(Seller seller) {
      String account = seller.getEbayAccount();
      Trading trading = m_tradings.get(account);

      if (trading == null) {
         synchronized (m_tradings) {
            trading = m_tradings.get(account);

            if (trading == null) {
               trading = lookup(Trading.class);

               configure(seller, trading, true);
               m_tradings.put(account, trading);
            }
         }
      }

      return trading;
   }

   private void configure(Seller seller, AbstractEbayApiRequest api, boolean needsAuthToken) {
      api.setAppId(seller.getEbayAppId());

      if (needsAuthToken) {
         api.setEbayAuthToken(seller.getEbayAuthToken());
      }
   }
}
