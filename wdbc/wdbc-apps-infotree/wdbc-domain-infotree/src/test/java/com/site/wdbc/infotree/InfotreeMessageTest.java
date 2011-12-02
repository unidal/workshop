package com.site.wdbc.infotree;

import org.apache.http.NameValuePair;

import com.site.lookup.ComponentTestCase;

public class InfotreeMessageTest extends ComponentTestCase {
   public void testNameValuePairs() {
      InfoTreeMessage message = new InfoTreeMessage();

      NameValuePair[] nvpairs = message.toNameValuePairs();
      assertEquals(0, nvpairs.length);

      message.setUsername("username");
      nvpairs = message.toNameValuePairs();
      assertEquals(1, nvpairs.length);
      assertEquals(nvpairs[0].getName(), "user_name");
      assertEquals(nvpairs[0].getValue(), "username");
   }
}
