package org.unidal.ezsell.api.paypal;

import java.util.HashMap;
import java.util.Map;

import org.unidal.ezsell.dal.Seller;

import com.site.lookup.ContainerHolder;

public class PaypalApiManager extends ContainerHolder {
   private Map<String, PaypalApi> m_map = new HashMap<String, PaypalApi>();

   public PaypalApi getPaypalApi(Seller seller) {
      String account = seller.getEbayAccount();
      PaypalApi api = m_map.get(account);

      if (api == null) {
         synchronized (m_map) {
            api = m_map.get(account);

            if (api == null) {
               api = lookup(PaypalApi.class);

               configure(seller, api);
               m_map.put(account, api);
            }
         }
      }

      return api;
   }

   private void configure(Seller seller, AbstractPaypalApiRequest api) {
      api.setApiUsername(seller.getPaypalUsername());
      api.setApiPassword(seller.getPaypalPassword());
      api.setApiSignature(seller.getPaypalSignature());
   }
}
