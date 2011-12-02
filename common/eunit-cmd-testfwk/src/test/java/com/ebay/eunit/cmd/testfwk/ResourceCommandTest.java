package com.ebay.eunit.cmd.testfwk;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.ebay.eunit.cmd.CommandClassRunner;
import com.ebay.eunit.cmd.annotation.ExpectedResponse;
import com.ebay.eunit.cmd.annotation.ExpectedResponseBody;
import com.ebay.eunit.cmd.annotation.ExpectedResponseHeaders;
import com.ebay.eunit.cmd.annotation.GET;
import com.ebay.eunit.cmd.annotation.HttpProtocol;
import com.ebay.eunit.cmd.annotation.WithRequestHeaders;
import com.ebay.eunit.cmd.annotation.WithRequestProvider;
import com.ebay.eunit.cmd.spi.IRequest;
import com.ebay.eunit.cmd.testfwk.ResourceCommandTest.ResourceCommand;
import com.ebay.eunit.mock.http.IHttpHandler;

@RunWith(CommandClassRunner.class)
@ResourceCommand
public class ResourceCommandTest {
   protected void provideIfModifiedSince(IRequest request) {
      SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");

      request.setHeader("If-Modified-Since", format.format(new Date()));
   }

   @Test
   @GET("res:/z/a-4.js")
   @ExpectedResponse(code = 200, contentType = "application/x-javascript", contentLength = 8)
   @ExpectedResponseHeaders( //
   names = { "Last-Modified", "Cache-Control", "Expires", "ETag" }, //
   values = { "", "", "", "a-4" })
   @ExpectedResponseBody("a1a2a3a4")
   public void testJs200() {

   }

   @Test
   @GET("res:/z/a-4.js")
   @WithRequestHeaders( //
   names = { "If-Modified-Since", "If-None-Match" }, //
   values = { "Wed, 14 Jan 2010 13:31:00 GMT", "a-4" })
   @ExpectedResponse(code = 200, contentType = "application/x-javascript")
   @ExpectedResponseHeaders( //
   names = { "Last-Modified", "Cache-Control", "Expires", "ETag" }, //
   values = { "", "", "", "a-4" })
   public void testJs200_2() {

   }

   @Test
   @GET("res:/z/a-4.js")
   @WithRequestProvider("provideIfModifiedSince")
   @ExpectedResponse(code = 304, contentType = "application/x-javascript")
   @ExpectedResponseHeaders( //
   names = { "Last-Modified", "Cache-Control", "Expires" }, //
   values = { "", "", "" })
   public void testJs304() {

   }

   @Test
   @GET("res:/z/a-4.js")
   @WithRequestProvider("provideIfModifiedSince")
   @WithRequestHeaders( //
   names = "If-None-Match", //
   values = "a-4")
   @ExpectedResponse(code = 304, contentType = "application/x-javascript")
   @ExpectedResponseHeaders( //
   names = { "Last-Modified", "Cache-Control", "Expires", "ETag" }, //
   values = { "", "", "", "a-4" })
   public void testJs304_2() {

   }

   @Test
   @GET("res:/z/a-4.js")
   @WithRequestHeaders( //
   names = "If-None-Match", //
   values = "a-4")
   @ExpectedResponse(code = 304, contentType = "application/x-javascript")
   @ExpectedResponseHeaders( //
   names = { "Last-Modified", "Cache-Control", "Expires", "ETag" }, //
   values = { "", "", "", "a-4" })
   public void testJs304_3() {

   }

   @Test
   @GET("res:/z/a-10.js")
   @ExpectedResponse(code = 404, contentType = "application/x-javascript")
   public void testJs404() {

   }

   @Retention(RetentionPolicy.RUNTIME)
   @Target(ElementType.TYPE)
   @HttpProtocol(ResourceHandler.class)
   public static @interface ResourceCommand {
   }

   public static class ResourceHandler implements IHttpHandler {
      @Override
      public String getProtocol() {
         return "res";
      }

      @Override
      public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         System.out.println("haha");
      }
   }
}
