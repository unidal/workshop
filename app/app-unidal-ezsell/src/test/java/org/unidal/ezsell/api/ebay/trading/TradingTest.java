package org.unidal.ezsell.api.ebay.trading;

import org.junit.Before;
import org.junit.Test;
import org.unidal.ezsell.api.ebay.trading.Trading;
import org.unidal.ezsell.feedback.FeedbackType;

import com.site.lookup.ComponentTestCase;
import com.site.wdbc.StringSource;
import com.site.wdbc.WdbcEngine;
import com.site.wdbc.WdbcQuery;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.WdbcSourceType;

public class TradingTest extends ComponentTestCase {
   private static final String AUTH_TOKEN = "AgAAAA**AQAAAA**aAAAAA**zq9rSg**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wHmISnCpCFpAWdj6x9nY+seQ**T68AAA**AAMAAA**hd50DHq+O4xsfgSskcuRWv1RBM9NpkwjiQf+/7YBw+vKuZ0PX4/LR1YCAFUOo9NJmVwgLKLXYAcGtbgaZ14wiA1s0v08fgF9w7IA3ceopIWn7QDvDj1FzT1SlEL00LNqe5tT15KbYzXrKD23FBG+QEPnE1qySb7cUCcQmjvunQZC1XcT0L7SOOoJrO23U9fK8GQ3CAUGU/dfC5nvRAFl6I2IQ2Wu1jd8fUhPIc33y+xvl88lmk7PTzidIRMOdvhVFpj28KjjRQxwMX9W3npbD7nj3QVuue0pConu6vTL3GRR9YgGc/8Hl/FwF7B3++fMFLAkZZRYGwL4HrqfOURkrNWAYVe1fI3jPbU4BiUMlCACIENxn5LJMQEotpYSf6IQj3VjjIbzGNjH8ZRCnHEJeu0UvyPar+yFtP6U22uK7x9GEPAbxLny8udS2wZT862WjyUh3racf1GhVfH8Wtg4M3+ke/pnb5AK/yFhcWZGgqyQWAkGbBBTZQ9DVdnhnpMloYNHIg1q/uCL3yD4R8rJgoMJOilXRuXy0DHebM3szYwBET0Oo/RylinGN0PR47M/ssjHMowzgjgQrZp21i++4X3xmx05caqnbb2LFuyaxUQCOmcJnir0c0rgRgNTC2jxoAvoTFl41wS88zNkaME59oDiJsSLbZVcqEwZz/MmyRBd9udEqP1KKevLFjEl73FP9UB793gulpiLL/6ol07dcVllPM05HZittvRGheK1Z3R/NuHVgLLntQ4SmH0OHwZv";

   private boolean DEBUG = false;

   private Trading m_trading;

   @Before
   @Override
   public void setUp() throws Exception {
      super.setUp();

      m_trading = lookup(Trading.class);
      m_trading.setEbayAuthToken(AUTH_TOKEN);
   }

   @Test
   public void testGetFeedbackByItemIdAndTransactionId() throws Exception {
      if (DEBUG) {
         String result = m_trading.getFeedbacks(300327093486L, 338096208020L);

         System.out.println(result);
      }
   }

   @Test
   public void testGetFeedbackByPage() throws Exception {
      if (DEBUG) {
         String result = m_trading.getFeedbacks(1);

         System.out.println(result);
      }
   }

   @Test
   public void testGetSellerTransactions() throws Exception {
      if (DEBUG) {
         String content = m_trading.getSellerTransactions(1);
         WdbcEngine engine = lookup(WdbcEngine.class);
         WdbcQuery query = lookup(WdbcQuery.class, "GetSellerTransactions");
         WdbcResult result = engine.execute(query, new StringSource(WdbcSourceType.XML, content));

         System.out.println(result);
         System.out.println(content);
      }
   }

   @Test
   public void testMarkAsShipped() throws Exception {
      if (DEBUG) {
         String result = m_trading.markAsShipped(300337471036L, 355476003020L, "RR762953037CN", "WANSE");

         System.out.println(result);
      }
   }

   @Test
   public void testMarkAsPaid() throws Exception {
      if (DEBUG) {
         String result = m_trading.markAsPaid(300337471036L, 355476003020L);

         System.out.println(result);
      }
   }

   @Test
   public void testLeavePositiveFeedback() throws Exception {
      if (false || DEBUG) { // failure
         String result = m_trading.leaveFeedback(300328645159L, 343683257020L, FeedbackType.POSITIVE, "A+++++",
               "mahlumd184");

         System.out.println(result);
      }
   }
}
