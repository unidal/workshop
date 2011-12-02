package com.site.app.tracking.counter;

import com.site.app.tracking.counter.Payload;
import com.site.app.tracking.counter.PayloadProvider;
import com.site.lookup.ComponentTestCase;

public class PayloadProviderTest extends ComponentTestCase {
   private String getKey(Payload payload) {
      return payload.getFromPage() + ":" + payload.getCategory1() + ":" + payload.getCategory2() + ":"
            + (payload.isOnTop() ? "1" : "0");
   }

   public void testBookmark() throws Exception {
      PayloadProvider provider = lookup(PayloadProvider.class);
      Payload payload = provider.getPayload("http://localhost/detail/714397.html", "r=&t=0");

      assertNotNull(payload);
      assertEquals("/detail/714397.html", payload.getPageUrl());
      assertEquals(":0:0:0", getKey(payload));
   }

   public void testBookmarkForTest() throws Exception {
      PayloadProvider provider = lookup(PayloadProvider.class);
      Payload payload = provider.getPayload("http://localhost/t/detail/714397.html", "r=&t=0");

      assertNotNull(payload);
      assertEquals("/detail/714397.html", payload.getPageUrl());
      assertEquals(":0:0:0", getKey(payload));
   }

   /**
    * @throws Exception
    */
   public void testCategory() throws Exception {
      PayloadProvider provider = lookup(PayloadProvider.class);
      Payload payload1 = provider.getPayload("http://localhost/detail/714398.html",
            "r=http%3A//localhost/51/110100-487.html%3F&t=0");

      assertNotNull(payload1);
      assertEquals("/detail/714398.html", payload1.getPageUrl());
      assertEquals("/51/:487:487:0", getKey(payload1));

      Payload payload2 = provider.getPayload("http://localhost/detail/714398.html",
            "r=http%3A//localhost/59/110100-487.html%3F&t=0");

      assertNotNull(payload2);
      assertEquals("/detail/714398.html", payload2.getPageUrl());
      assertEquals("/59/:487:487:0", getKey(payload2));

      Payload payload3 = provider.getPayload("http://localhost/detail/714398.html",
            "r=http%3A//localhost/5159/110100-487.html%3F&t=0");

      assertNotNull(payload3);
      assertEquals("/detail/714398.html", payload3.getPageUrl());
      assertEquals("/5159/:487:487:0", getKey(payload3));
   }

   public void testCategoryForTest() throws Exception {
      PayloadProvider provider = lookup(PayloadProvider.class);
      Payload payload1 = provider.getPayload("http://localhost/t/c/detail/714398.html",
            "r=http%3A//localhost/t/c/51/110100-487.html%3F&t=0");

      assertNotNull(payload1);
      assertEquals("/detail/714398.html", payload1.getPageUrl());
      assertEquals("/51/:487:487:0", getKey(payload1));

      Payload payload2 = provider.getPayload("http://localhost/t/c/detail/714398.html",
            "r=http%3A//localhost/t/c/59/110100-487.html%3F&t=0");

      assertNotNull(payload2);
      assertEquals("/detail/714398.html", payload2.getPageUrl());
      assertEquals("/59/:487:487:0", getKey(payload2));

      Payload payload3 = provider.getPayload("http://localhost/t/c/detail/714398.html",
            "r=http%3A//localhost/t/c/5159/110100-487.html%3F&t=0");

      assertNotNull(payload3);
      assertEquals("/detail/714398.html", payload3.getPageUrl());
      assertEquals("/5159/:487:487:0", getKey(payload3));
   }

   public void testHome() throws Exception {
      PayloadProvider provider = lookup(PayloadProvider.class);
      Payload payload = provider.getPayload("http://localhost/detail/977512.html",
            "r=http%3A//localhost/%3Fcat%3D123&t=1");

      assertNotNull(payload);
      assertEquals("/detail/977512.html", payload.getPageUrl());
      assertEquals("/:123:123:1", getKey(payload));
   }

   public void testHomeForTest() throws Exception {
      PayloadProvider provider = lookup(PayloadProvider.class);
      Payload payload = provider.getPayload("http://localhost/t/c/detail/977512.html",
            "r=http%3A//localhost/t/c/%3Fcat%3D123&t=1");

      assertNotNull(payload);
      assertEquals("/detail/977512.html", payload.getPageUrl());
      assertEquals("/:123:123:1", getKey(payload));
      
//      http://www.xiaoxishu.com/t/c?r=http%3A//www.xiaoxishu.com/%26cat%3D102&t=1
      payload = provider.getPayload("http://localhost/t/c/detail/977512.html",
      "r=http%3A//www.xiaoxishu.com/%3F%26cat%3D102&t=1");
      
      assertNotNull(payload);
      assertEquals("/detail/977512.html", payload.getPageUrl());
      assertEquals("/:102:102:1", getKey(payload));
   }

   public void testSearch() throws Exception {
      PayloadProvider provider = lookup(PayloadProvider.class);
      Payload payload = provider.getPayload("http://localhost/detail/923305.html",
            "r=http%3A//localhost/search/%3Fcat%3D783&t=1");

      assertNotNull(payload);
      assertEquals("/detail/923305.html", payload.getPageUrl());
      assertEquals("/search/:783:783:1", getKey(payload));
   }

   public void testSearchForTest() throws Exception {
      PayloadProvider provider = lookup(PayloadProvider.class);
      Payload payload = provider.getPayload("http://localhost/t/c/detail/923305.html",
            "r=http%3A//localhost/t/c/search/%3Fcat%3D783&t=1");

      assertNotNull(payload);
      assertEquals("/detail/923305.html", payload.getPageUrl());
      assertEquals("/search/:783:783:1", getKey(payload));
   }

   public void testUnknownPage() throws Exception {
      PayloadProvider provider = lookup(PayloadProvider.class);
      Payload payload = provider.getPayload("http://localhost/detail/923306.html",
            "r=http%3A//localhost/this/is/unknown/page&t=1");

      assertNotNull(payload);
      assertEquals("/detail/923306.html", payload.getPageUrl());
      assertEquals("/this/is/unknown/page:0:0:1", getKey(payload));
   }

   public void testUnknownPageForTest() throws Exception {
      PayloadProvider provider = lookup(PayloadProvider.class);
      Payload payload = provider.getPayload("http://localhost/t/c/detail/923306.html",
            "r=http%3A//localhost/t/c/this/is/unknown/page&t=1");

      assertNotNull(payload);
      assertEquals("/detail/923306.html", payload.getPageUrl());
      assertEquals("/this/is/unknown/page:0:0:1", getKey(payload));
   }
   
   public void testIssue0707() throws Exception {
      PayloadProvider provider = lookup(PayloadProvider.class);
      Payload payload = provider.getPayload("http://www.xiaoxishu.com/detail/218952.html",
      "r=http%3A//www.xiaoxishu.com/59/110105-786-1.html&t=1");
      
      assertNotNull(payload);
      assertEquals("/detail/218952.html", payload.getPageUrl());
      assertEquals("/59/:786:786:1", getKey(payload));
   }
}
