package com.ebay.eunit.cmd.testfwk;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.ebay.eunit.cmd.CommandClassRunner;
import com.ebay.eunit.cmd.annotation.ExpectedResponse;
import com.ebay.eunit.cmd.annotation.ExpectedResponseBody;
import com.ebay.eunit.cmd.annotation.GET;
import com.ebay.eunit.cmd.annotation.HttpProtocol;
import com.ebay.eunit.cmd.annotation.POST;
import com.ebay.eunit.cmd.annotation.WithRequestHeaders;
import com.ebay.eunit.cmd.testfwk.CommandTest.EchoCommand;
import com.ebay.eunit.cmd.testfwk.CommandTest.MockCommand;
import com.ebay.eunit.helper.Files;
import com.ebay.eunit.mock.http.IHttpHandler;

@RunWith(CommandClassRunner.class)
@MockCommand
@EchoCommand
public class CommandTest {
   @Test
   @GET("echo:/z/a-4.js?a=1&b=2&b=3")
   @WithRequestHeaders( //
   names = { "If-Modified-Since", "If-None-Match" }, //
   values = { "Mon, 17 Jan 2011 15:07:00 GMT", "b3h2hjklfshjfks" })
   @ExpectedResponse(code = 200, contentType = "text/plain", contentLength = 149)
   @ExpectedResponseBody("[URI]\r\n" + //
         "/z/a-4.js\r\n" + //
         "\r\n" + //
         "[Headers]\r\n" + //
         "If-Modified-Since: Mon, 17 Jan 2011 15:07:00 GMT\r\n" + //
         "If-None-Match: b3h2hjklfshjfks\r\n" + //
         "\r\n" + //
         "[Parameters]\r\n" + //
         "a: 1\r\n" + //
         "b: 2\r\n" + //
         "b: 3\r\n" + //
         "\r\n")
   public void getEcho() {
   }

   @Test
   @GET("mock:/z/a-4.js?a=1&b=2&b=3")
   @ExpectedResponse(code = 200)
   @ExpectedResponseBody("Hello, mock server!\r\n" + //
         "\r\n" + //
         "[Parameters]\r\n" + //
         "a: 1\r\n" + //
         "b: 2\r\n" + //
         "b: 3\r\n" + //
         "\r\n")
   public void getMock() throws IOException {
   }

   @Test
   @POST(value = "echo:/z/a-4.css", formData = "forceReload=true")
   @WithRequestHeaders( //
   names = { "If-Modified-Since", "If-None-Match" }, //
   values = { "...", "..." })
   @ExpectedResponse(code = 200, contentType = "text/plain", contentLength = 202)
   @ExpectedResponseBody("[URI]\r\n" + //
         "/z/a-4.css\r\n" + //
         "\r\n" + //
         "[Headers]\r\n" + //
         "Content-Length: 16\r\n" + //
         "Content-Type: application/x-www-url-encoded\r\n" + //
         "If-Modified-Since: ...\r\n" + //
         "If-None-Match: ...\r\n" + //
         "\r\n" + //
         "[Parameters]\r\n" + //
         "forceReload: true\r\n" + //
         "\r\n" + //
         "[Body]\r\n" + //
         "forceReload=true")
   public void postEcho() throws IOException {
   }

   @Test
   @POST(value = "mock:/z/a-4.css", formData = "forceReload=true")
   @ExpectedResponse(code = 200)
   @ExpectedResponseBody("Hello, mock server!\r\n" + //
         "\r\n" + //
         "[Parameters]\r\n" + //
         "forceReload: true\r\n" + //
         "\r\n")
   public void postMock() throws IOException {
   }

   @Retention(RetentionPolicy.RUNTIME)
   @Target(ElementType.TYPE)
   @HttpProtocol(EchoHandler.class)
   public static @interface EchoCommand {
   }

   static enum EchoHandler implements IHttpHandler {
      INSTANCE;

      private static final String UTF_8 = "utf-8";

      private void addBody(StringBuilder sb, HttpServletRequest request, String name) throws IOException {
         if ("POST".equalsIgnoreCase(request.getMethod())) {
            String charset = request.getCharacterEncoding();
            ServletInputStream in = request.getInputStream();
            String body = Files.forIO().readFrom(in, charset == null ? UTF_8 : charset);

            sb.append(name).append("\r\n");
            sb.append(body);
         }
      }

      @SuppressWarnings("unchecked")
      private void addHeaders(StringBuilder sb, HttpServletRequest request, String name) {
         sb.append(name).append("\r\n");

         List<String> headerNames = Collections.list(request.getHeaderNames());

         Collections.sort(headerNames);

         for (String headerName : headerNames) {
            List<String> headers = Collections.list(request.getHeaders(headerName));

            for (String header : headers) {
               sb.append(headerName).append(": ").append(header).append("\r\n");
            }
         }

         sb.append("\r\n");
      }

      @SuppressWarnings("unchecked")
      private void addParameters(StringBuilder sb, HttpServletRequest request, String name) {
         sb.append(name).append("\r\n");

         List<String> parameterNames = Collections.list(request.getParameterNames());

         Collections.sort(parameterNames);

         for (String paramterName : parameterNames) {
            String[] parameters = request.getParameterValues(paramterName);

            for (String header : parameters) {
               sb.append(paramterName).append(": ").append(header).append("\r\n");
            }
         }

         sb.append("\r\n");
      }

      private void addUri(StringBuilder sb, HttpServletRequest request, String name) {
         sb.append(name).append("\r\n");
         sb.append(request.getRequestURI()).append("\r\n");
         sb.append("\r\n");
      }

      @Override
      public String getProtocol() {
         return "echo";
      }

      @Override
      public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         StringBuilder sb = new StringBuilder(4096);

         addUri(sb, request, "[URI]");
         addHeaders(sb, request, "[Headers]");
         addParameters(sb, request, "[Parameters]");
         addBody(sb, request, "[Body]");

         byte[] data = sb.toString().getBytes(UTF_8);
         ServletOutputStream out = response.getOutputStream();

         response.setStatus(200);
         response.setContentLength(data.length);
         response.setContentType("text/plain");
         response.setCharacterEncoding(UTF_8);

         out.write(data);
      }
   }

   @Retention(RetentionPolicy.RUNTIME)
   @Target(ElementType.TYPE)
   @HttpProtocol(MockHandler.class)
   public static @interface MockCommand {
   }

   static enum MockHandler implements IHttpHandler {
      INSTANCE;

      private static final String UTF_8 = "utf-8";

      @SuppressWarnings("unchecked")
      private void addParameters(StringBuilder sb, HttpServletRequest request, String name) {
         sb.append(name).append("\r\n");

         List<String> parameterNames = Collections.list(request.getParameterNames());

         Collections.sort(parameterNames);

         for (String paramterName : parameterNames) {
            String[] parameters = request.getParameterValues(paramterName);

            for (String header : parameters) {
               sb.append(paramterName).append(": ").append(header).append("\r\n");
            }
         }

         sb.append("\r\n");
      }

      @Override
      public String getProtocol() {
         return "mock";
      }

      @Override
      public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         StringBuilder sb = new StringBuilder(4096);

         sb.append("Hello, mock server!\r\n\r\n");

         addParameters(sb, request, "[Parameters]");

         byte[] data = sb.toString().getBytes(UTF_8);
         ServletOutputStream out = response.getOutputStream();

         response.setStatus(200);
         response.setContentLength(data.length);
         response.setContentType("text/plain");
         response.setCharacterEncoding(UTF_8);

         out.write(data);
      }
   }
}
