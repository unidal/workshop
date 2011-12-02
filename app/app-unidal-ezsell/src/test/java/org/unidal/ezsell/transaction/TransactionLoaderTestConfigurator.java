package org.unidal.ezsell.transaction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.unidal.ezsell.api.ebay.trading.Trading;
import org.unidal.ezsell.api.paypal.PaypalApi;
import org.unidal.ezsell.transaction.TransactionLoaderTest.PaypalApiMock;
import org.unidal.ezsell.transaction.TransactionLoaderTest.TradingMock;

import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;

public class TransactionLoaderTestConfigurator extends AbstractResourceConfigurator {
   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new TransactionLoaderTestConfigurator());
   }

   @Override
   protected File getConfigurationFile() {
      return new File("src/test/resources/" + TransactionLoaderTest.class.getName().replace('.', '/') + ".xml");
   }

   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(Trading.class, TradingMock.class));
      all.add(C(PaypalApi.class, PaypalApiMock.class));

      return all;
   }
}