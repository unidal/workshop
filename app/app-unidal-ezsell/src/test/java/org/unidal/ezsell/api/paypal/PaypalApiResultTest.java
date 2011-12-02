package org.unidal.ezsell.api.paypal;

import java.text.SimpleDateFormat;

import junit.framework.Assert;

import org.junit.Test;

public class PaypalApiResultTest {
   @Test
   public void testParseGetTransactionDetails() {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      String content = "RECEIVERBUSINESS=kewan_wang%40hotmail%2ecom&RECEIVEREMAIL=kewan_wang%40hotmail%2ecom&RECEIVERID=DDRJGGZVAFQYU&EMAIL=the%2dwades%40sbcglobal%2enet&PAYERID=852WM976MKRJL&PAYERSTATUS=unverified&COUNTRYCODE=US&SHIPTONAME=Becky%20Wade&SHIPTOSTREET=3532%20Vinecrest%20Drive&SHIPTOCITY=Dallas&SHIPTOSTATE=TX&SHIPTOCOUNTRYCODE=US&SHIPTOCOUNTRYNAME=United%20States&SHIPTOZIP=75229&ADDRESSOWNER=PayPal&ADDRESSSTATUS=Confirmed&SALESTAX=0%2e00&BUYERID=214becky3532&CLOSINGDATE=2009%2d10%2d19T09%3a10%3a00Z&TIMESTAMP=2009%2d10%2d20T14%3a37%3a42Z&CORRELATIONID=81d5352588312&ACK=Success&VERSION=51%2e0&BUILD=1073465&FIRSTNAME=Rebecca&LASTNAME=Wade&TRANSACTIONID=9S392321Y8493191P&TRANSACTIONTYPE=webaccept&PAYMENTTYPE=instant&ORDERTIME=2009%2d10%2d19T16%3a32%3a54Z&AMT=17%2e98&FEEAMT=1%2e00&TAXAMT=0%2e00&CURRENCYCODE=USD&PAYMENTSTATUS=Completed&PENDINGREASON=None&REASONCODE=None&L_NAME0=Harry%20Potter%20Hermione%20Granger%20Magic%20Wand%20LED%20Light%20NIB&L_NUMBER0=300358300866&L_QTY0=1&L_CURRENCYCODE0=USD";
      PaypalApiResult result = new PaypalApiResult(content);

      Assert.assertEquals("Success", result.getAck());
      Assert.assertEquals("81d5352588312", result.getCorelationId());
      Assert.assertEquals("51.0", result.getVersion());
      Assert.assertEquals(1073465, result.getBuild());
      Assert.assertEquals("2009-10-20T14:37:42Z", dateFormat.format(result.getTimestamp()));

      Assert.assertEquals(17.98D, result.getDoubleProperty("Amt", 0));
      Assert.assertEquals(1.00D, result.getDoubleProperty("FeeAmt", 0));
      Assert.assertEquals("USD", result.getStringProperty("CurrencyCode"));
      Assert.assertEquals("9S392321Y8493191P", result.getStringProperty("TransactionId"));
      Assert.assertEquals("kewan_wang@hotmail.com", result.getStringProperty("ReceiverEmail"));

      Assert.assertEquals("[NAME, QTY, CURRENCYCODE, NUMBER]", result.getColumnNames().toString());

      Assert.assertEquals("Harry Potter Hermione Granger Magic Wand LED Light NIB", result.getString(0, "Name"));
      Assert.assertEquals(300358300866L, result.getLong(0, "Number", 0));
      Assert.assertEquals(1, result.getInt(0, "Qty", 0));
      Assert.assertEquals("USD", result.getString(0, "CurrencyCode"));
   }

   @Test
   public void testParseTransactionSearch() {
      String format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
      SimpleDateFormat dateFormat = new SimpleDateFormat(format);
      String content = "L_TIMESTAMP0=2009%2d10%2d20T14%3a28%3a19Z&L_TIMESTAMP1=2009%2d10%2d20T04%3a17%3a46Z&L_TIMESTAMP2=2009%2d10%2d20T01%3a44%3a55Z&L_TIMESTAMP3=2009%2d10%2d19T22%3a53%3a11Z&L_TIMESTAMP4=2009%2d10%2d19T21%3a39%3a59Z&L_TIMESTAMP5=2009%2d10%2d19T21%3a24%3a32Z&L_TIMESTAMP6=2009%2d10%2d19T17%3a03%3a57Z&L_TIMESTAMP7=2009%2d10%2d19T16%3a44%3a35Z&L_TIMESTAMP8=2009%2d10%2d19T16%3a32%3a54Z&L_TIMESTAMP9=2009%2d10%2d19T15%3a50%3a38Z&L_TIMESTAMP10=2009%2d10%2d19T14%3a47%3a13Z&L_TIMESTAMP11=2009%2d10%2d19T14%3a46%3a46Z&L_TIMEZONE0=GMT&L_TIMEZONE1=GMT&L_TIMEZONE2=GMT&L_TIMEZONE3=GMT&L_TIMEZONE4=GMT&L_TIMEZONE5=GMT&L_TIMEZONE6=GMT&L_TIMEZONE7=GMT&L_TIMEZONE8=GMT&L_TIMEZONE9=GMT&L_TIMEZONE10=GMT&L_TIMEZONE11=GMT&L_TYPE0=Payment&L_TYPE1=Payment&L_TYPE2=Refund&L_TYPE3=Payment&L_TYPE4=Payment&L_TYPE5=Payment&L_TYPE6=Payment&L_TYPE7=Payment&L_TYPE8=Payment&L_TYPE9=Payment&L_TYPE10=Payment&L_TYPE11=Payment&L_EMAIL0=bcolohan123484%40yahoo%2eie&L_EMAIL1=warleok%40gmail%2ecom&L_EMAIL2=jlm2281%40netzero%2enet&L_EMAIL3=jlm2281%40netzero%2enet&L_EMAIL4=kraig501%40hotmail%2ecom&L_EMAIL5=gon2thedogz%40bellsouth%2enet&L_EMAIL6=duppsyn%40start%2eno&L_EMAIL7=michaela%2eedwards%40btinternet%2ecom&L_EMAIL8=the%2dwades%40sbcglobal%2enet&L_EMAIL9=bulldog986%40hotmail%2ecom&L_EMAIL10=donangcourt%40verizon%2enet&L_EMAIL11=dyousse%40verizon%2enet&L_NAME0=barry%20colohan&L_NAME1=Ricky%20Mason&L_NAME2=Shane%20Meadows&L_NAME3=Shane%20Meadows&L_NAME4=Kraig%20Magby&L_NAME5=linda%20phillips&L_NAME6=Oyvind%20Nordheim&L_NAME7=michaela%20edwards&L_NAME8=Rebecca%20Wade&L_NAME9=Richard%20Rabbelier&L_NAME10=Angela%20McSherry&L_NAME11=Debra%20Yousse&L_TRANSACTIONID0=5CA18762S1483182G&L_TRANSACTIONID1=9H11118934320752W&L_TRANSACTIONID2=891091584P859561U&L_TRANSACTIONID3=4K761871UY8076726&L_TRANSACTIONID4=50E657221G553901M&L_TRANSACTIONID5=64G503138K549393B&L_TRANSACTIONID6=1ST61641YD2883618&L_TRANSACTIONID7=59048870PV7584226&L_TRANSACTIONID8=9S392321Y8493191P&L_TRANSACTIONID9=45V369641P055110M&L_TRANSACTIONID10=4AC509976L387242A&L_TRANSACTIONID11=41918524Y6255952D&L_STATUS0=Completed&L_STATUS1=Completed&L_STATUS2=Completed&L_STATUS3=Refunded&L_STATUS4=Completed&L_STATUS5=Completed&L_STATUS6=Completed&L_STATUS7=Completed&L_STATUS8=Completed&L_STATUS9=Uncleared&L_STATUS10=Completed&L_STATUS11=Completed&L_AMT0=42%2e98&L_AMT1=16%2e98&L_AMT2=%2d75%2e97&L_AMT3=75%2e97&L_AMT4=75%2e97&L_AMT5=35%2e76&L_AMT6=22%2e16&L_AMT7=49%2e58&L_AMT8=17%2e98&L_AMT9=75%2e97&L_AMT10=11%2e98&L_AMT11=37%2e78&L_CURRENCYCODE0=USD&L_CURRENCYCODE1=USD&L_CURRENCYCODE2=USD&L_CURRENCYCODE3=USD&L_CURRENCYCODE4=USD&L_CURRENCYCODE5=USD&L_CURRENCYCODE6=USD&L_CURRENCYCODE7=USD&L_CURRENCYCODE8=USD&L_CURRENCYCODE9=USD&L_CURRENCYCODE10=USD&L_CURRENCYCODE11=USD&L_FEEAMT0=%2d1%2e98&L_FEEAMT1=%2d0%2e96&L_FEEAMT2=3%2e26&L_FEEAMT3=%2d3%2e26&L_FEEAMT4=%2d3%2e26&L_FEEAMT5=%2d1%2e69&L_FEEAMT6=%2d1%2e16&L_FEEAMT7=%2d2%2e23&L_FEEAMT8=%2d1%2e00&L_FEEAMT9=0%2e00&L_FEEAMT10=%2d0%2e77&L_FEEAMT11=%2d1%2e77&L_NETAMT0=41%2e00&L_NETAMT1=16%2e02&L_NETAMT2=%2d72%2e71&L_NETAMT3=72%2e71&L_NETAMT4=72%2e71&L_NETAMT5=34%2e07&L_NETAMT6=21%2e00&L_NETAMT7=47%2e35&L_NETAMT8=16%2e98&L_NETAMT9=75%2e97&L_NETAMT10=11%2e21&L_NETAMT11=36%2e01&TIMESTAMP=2009%2d10%2d20T14%3a32%3a55Z&CORRELATIONID=d6bbd8e1aa214&ACK=Success&VERSION=51%2e0&BUILD=1073465";
      PaypalApiResult result = new PaypalApiResult(content);

      Assert.assertEquals("Success", result.getAck());
      Assert.assertEquals("d6bbd8e1aa214", result.getCorelationId());
      Assert.assertEquals("51.0", result.getVersion());
      Assert.assertEquals(1073465, result.getBuild());
      Assert.assertEquals("2009-10-20T14:32:55Z", dateFormat.format(result.getTimestamp()));

      Assert.assertEquals("[NETAMT, NAME, TRANSACTIONID, AMT, FEEAMT, TIMEZONE, EMAIL, STATUS, TIMESTAMP, CURRENCYCODE, TYPE]", result.getColumnNames().toString());

      Assert.assertEquals("2009-10-20T14:28:19Z", dateFormat.format(result.getDate(0, "TIMESTAMP", null, format)));
      Assert.assertEquals("2009-10-19T14:46:46Z", dateFormat.format(result.getDate(11, "TIMESTAMP", null, format)));
      Assert.assertEquals(42.98d, result.getDouble(0, "Amt", 0));
      Assert.assertEquals(37.78d, result.getDouble(11, "Amt", 0));
      Assert.assertEquals("Completed", result.getString(0, "Status"));
      Assert.assertEquals("Refunded", result.getString(3, "Status"));
      Assert.assertEquals("Uncleared", result.getString(9, "Status"));
      Assert.assertEquals("Completed", result.getString(11, "Status"));
      Assert.assertEquals("Payment", result.getString(0, "Type"));
      Assert.assertEquals("Refund", result.getString(2, "Type"));
      Assert.assertEquals("5CA18762S1483182G", result.getString(0, "TransactionId"));
      Assert.assertEquals("891091584P859561U", result.getString(2, "TransactionId"));
   }
}
