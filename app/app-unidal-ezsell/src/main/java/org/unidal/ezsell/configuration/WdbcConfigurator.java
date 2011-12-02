package org.unidal.ezsell.configuration;

import java.util.ArrayList;
import java.util.List;

import org.unidal.ezsell.api.ebay.trading.CompleteSale;
import org.unidal.ezsell.api.ebay.trading.GetFeedback;
import org.unidal.ezsell.api.ebay.trading.wdbc.EbayCompleteSale;
import org.unidal.ezsell.api.ebay.trading.wdbc.EbayFeedback;
import org.unidal.ezsell.api.ebay.trading.wdbc.EbayTransaction;
import org.unidal.ezsell.api.ebay.trading.wdbc.WdbcMapping;

import com.site.lookup.configuration.Component;
import com.site.wdbc.WdbcEngine;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.http.configuration.AbstractWdbcComponentsConfigurator;

final class WdbcConfigurator extends AbstractWdbcComponentsConfigurator {
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(WdbcMapping.class));
      
      all.addAll(WDBC(EbayTransaction.class));
      all.addAll(WDBC(EbayFeedback.class));
      all.addAll(WDBC(EbayCompleteSale.class));
      
      all.add(C(GetFeedback.class).req(WdbcEngine.class).req(WdbcQuery.class, "GetFeedback"));
      all.add(C(CompleteSale.class).req(WdbcEngine.class).req(WdbcQuery.class, "CompleteSale"));
      
      return all;
   }

}