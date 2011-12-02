package org.unidal.ezsell.configuration;

import java.util.ArrayList;
import java.util.List;

import org.unidal.ezsell.api.ebay.EbayApiManager;
import org.unidal.ezsell.api.ebay.shopping.Shopping;
import org.unidal.ezsell.api.ebay.trading.Trading;
import org.unidal.ezsell.api.paypal.PaypalApi;
import org.unidal.ezsell.api.paypal.PaypalApiManager;
import org.unidal.ezsell.api.paypal.PaypalMapping;
import org.unidal.ezsell.register.EbayAccountLink;

import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;

class ApiConfigurator extends AbstractResourceConfigurator {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.addAll(getEbayApiComponents());
      all.addAll(getPaypalApiComponents());

      return all;
   }

   private List<Component> getEbayApiComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(EbayApiManager.class));
      all.add(C(Shopping.class));
      all.add(C(Trading.class));

      all.add(C(EbayAccountLink.class).config(
            E("appId").value("eBaycom8e-3410-4a8f-a366-e317c7cff15"),
            E("authTokenMinLength").value("500"),
            E("accountLinkUrlPattern").value(
                  "<![CDATA[https://signin.ebay.com/ws/eBayISAPI.dll?SignIn&runame=auth_tool_sdk_e7834fb260ba4b76814076b53adfc450"
                        + "&ruparams=params|Production-auth_tool_sdk_e7834fb260ba4b76814076b53adfc450-true-67073"
                        + "&pUserId={0}]]>")));

      return all;
   }
   
   private List<Component> getPaypalApiComponents() {
      List<Component> all = new ArrayList<Component>();
      
      all.add(C(PaypalApiManager.class));
      all.add(C(PaypalMapping.class));
      all.add(C(PaypalApi.class));
      
      return all;
   }
}