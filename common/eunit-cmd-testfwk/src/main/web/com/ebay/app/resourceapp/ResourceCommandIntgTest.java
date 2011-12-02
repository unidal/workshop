package com.ebay.app.resourceapp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ebay.eunit.mock.http.HttpClientMocks;
import com.ebay.eunit.mock.http.HttpServerMocks;

public class ResourceCommandIntgTest {
   @BeforeClass
   public static void initialize() {
      IResourceServiceConsumer consumer = (IResourceServiceConsumer) MockFactory.createInstance(new RuleBasedConsumer(
            "5", ResourceType.JS), IResourceServiceConsumer.class);
      DataManager manager = new DataManager(consumer, NullCache.INSTANCE, NullCache.INSTANCE);

      ResourceRuntime.getInstance().setDataManager(manager);
      DefaultResourceRuntimeInitializer.INSTANCE.registerHandlers();

      String commandName = EbayPageEnum.PageDynamicResourceAggregator.getPhysicalName();

      HttpServerMocks.forUrl().bindStaticPage("res", commandName, ResourceCommand.class);
   }

   @AfterClass
   public static void destroy() {
      HttpServerMocks.forUrl().unbind("res");
   }

   @Test
   public void testJs200() throws IOException {
      HttpClientMocks.forGet("res:/z/a-4.js") //
            .expectResponseCode(200) //
            .expectContentType("application/x-javascript") //
            .expectContentLength(8) //
            .expectResponseHeaders("Last-Modified", "Cache-Control", "Expires") //
            .expectResponseHeader("ETag", "a-4") //
            .expectResponseBody("a1a2a3a4") //
            .go();

      // expired Last Modified header
      HttpClientMocks.forGet("res:/z/a-4.js") //
            .withRequestHeader("If-Modified-Since", "Wed, 14 Jan 2010 13:31:00 GMT") //
            .withRequestHeader("If-None-Match", "a-4") //
            .expectResponseCode(200) //
            .expectContentType("application/x-javascript") //
            .expectResponseHeaders("Last-Modified", "Cache-Control", "Expires") //
            .expectResponseHeader("ETag", "a-4") //
            .go();
   }

   @Test
   public void testJs304() throws IOException {
      SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

      HttpClientMocks.forGet("res:/z/a-4.js") //
            .withRequestHeader("If-Modified-Since", format.format(new Date())) //
            .expectResponseCode(304) //
            .expectContentType("application/x-javascript") //
            .expectResponseHeaders("Last-Modified", "Cache-Control", "Expires") //
            .go();

      HttpClientMocks.forGet("res:/z/a-4.js") //
            .withRequestHeader("If-Modified-Since", format.format(new Date())) //
            .withRequestHeader("If-None-Match", "a-4") //
            .expectResponseCode(304) //
            .expectContentType("application/x-javascript") //
            .expectResponseHeaders("Last-Modified", "Cache-Control", "Expires") //
            .expectResponseHeader("ETag", "a-4") //
            .go();
      
      HttpClientMocks.forGet("res:/z/a-4.js") //
            .withRequestHeader("If-None-Match", "a-4") //
            .expectResponseCode(304) //
            .expectContentType("application/x-javascript") //
            .expectResponseHeaders("Last-Modified", "Cache-Control", "Expires") //
            .expectResponseHeader("ETag", "a-4") //
            .go();
   }

   @Test
   public void testJs404() throws IOException {
      HttpClientMocks.forGet("res:/z/a-10.js") // a5 does not exist, so returns 404
            .expectResponseCode(404) //
            .expectContentType("application/x-javascript") //
            .go();
   }
}
