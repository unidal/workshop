package org.unidal.ezsell.api.ebay.trading.wdbc;

import java.util.List;

import org.codehaus.plexus.util.IOUtil;
import org.junit.Test;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.StringSource;
import com.site.wdbc.WdbcSourceType;

public class WdbcMappingTest extends ComponentTestCase {
   @Test
   public void testInject() throws Exception {
      WdbcMapping mapping = lookup(WdbcMapping.class);
      String content = IOUtil.toString(getClass().getResourceAsStream(
            "/org/unidal/ezsell/transaction/GetSellerTransactions.xml"));
      StringSource source = new StringSource(WdbcSourceType.XML, content);
      List<EbayTransaction> list = mapping.apply(EbayTransaction.class, source);

      System.out.println(list.size());

      for (EbayTransaction trx : list) {
         System.out.println(trx);
      }
   }
}
