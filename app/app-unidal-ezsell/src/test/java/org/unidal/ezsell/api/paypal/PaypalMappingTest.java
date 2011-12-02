package org.unidal.ezsell.api.paypal;

import org.junit.Test;

import com.site.lookup.ComponentTestCase;

public class PaypalMappingTest extends ComponentTestCase {
   @Test
   public void testInject() throws Exception {
      PaypalMapping mapping = lookup(PaypalMapping.class);
      String content = "RECEIVERBUSINESS=kewan_wang%40hotmail%2ecom&RECEIVEREMAIL=kewan_wang%40hotmail%2ecom&RECEIVERID=DDRJGGZVAFQYU&EMAIL=the%2dwades%40sbcglobal%2enet&PAYERID=852WM976MKRJL&PAYERSTATUS=unverified&COUNTRYCODE=US&SHIPTONAME=Becky%20Wade&SHIPTOSTREET=3532%20Vinecrest%20Drive&SHIPTOCITY=Dallas&SHIPTOSTATE=TX&SHIPTOCOUNTRYCODE=US&SHIPTOCOUNTRYNAME=United%20States&SHIPTOZIP=75229&ADDRESSOWNER=PayPal&ADDRESSSTATUS=Confirmed&SALESTAX=0%2e00&BUYERID=214becky3532&CLOSINGDATE=2009%2d10%2d19T09%3a10%3a00Z&TIMESTAMP=2009%2d10%2d20T14%3a37%3a42Z&CORRELATIONID=81d5352588312&ACK=Success&VERSION=51%2e0&BUILD=1073465&FIRSTNAME=Rebecca&LASTNAME=Wade&TRANSACTIONID=9S392321Y8493191P&TRANSACTIONTYPE=webaccept&PAYMENTTYPE=instant&ORDERTIME=2009%2d10%2d19T16%3a32%3a54Z&AMT=17%2e98&FEEAMT=1%2e00&TAXAMT=0%2e00&CURRENCYCODE=USD&PAYMENTSTATUS=Completed&PENDINGREASON=None&REASONCODE=None&L_NAME0=Harry%20Potter%20Hermione%20Granger%20Magic%20Wand%20LED%20Light%20NIB&L_NUMBER0=300358300866&L_QTY0=1&L_CURRENCYCODE0=USD";
      PaypalTransaction trx = mapping.apply(PaypalTransaction.class, content);

      assertEquals("Success", trx.getAck());
      assertEquals(17.98d, trx.getAmount());
      assertEquals(1.00d, trx.getFeeAmount());

      assertNotNull(trx.getShipTo());
      assertEquals("Becky Wade", trx.getShipTo().getName());

      System.out.println(trx);
      System.out.println(trx.getShipTo());

      assertEquals(1, trx.getItems().size());

      for (PaypalTransaction.Item item : trx.getItems()) {
         System.out.println(item);
      }
   }
}
