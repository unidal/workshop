package com.ebay.eunit.cmd.testfwk;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.ebay.eunit.cmd.model.entity.CommandEntity;
import com.ebay.eunit.cmd.model.entity.HeaderEntity;
import com.ebay.eunit.cmd.model.entity.RequestEntity;
import com.ebay.eunit.cmd.model.entity.ResponseEntity;
import com.ebay.eunit.cmd.model.entity.RootEntity;
import com.ebay.eunit.mock.http.HttpClientMocks;
import com.ebay.eunit.mock.http.HttpClientMocks.HttpClient;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.testfwk.spi.ICaseContext;
import com.ebay.eunit.testfwk.spi.task.ITask;
import com.ebay.eunit.testfwk.spi.task.ITaskExecutor;
import com.ebay.eunit.testfwk.spi.task.ITaskType;

public enum HttpTaskExecutor implements ITaskExecutor<HttpTaskType> {
   EXECUTE(HttpTaskType.EXECUTE) {
      @Override
      public void execute(ICaseContext ctx) throws Throwable {
         ITask<ITaskType> task = ctx.getTask();
         EunitMethod eunitMethod = task.getEunitMethod();
         RootEntity root = ctx.getModel();
         CommandEntity cmd = root.findCommand(eunitMethod.getName());
         RequestEntity request = cmd.getWithRequest();
         ResponseEntity response = cmd.getExpectedResponse();

         execute(request, response);
      }

      private void execute(RequestEntity request, ResponseEntity response) throws IOException {
         HttpClient client;

         if ("GET".equals(request.getRequestMethod())) {
            client = HttpClientMocks.forGet(request.getRequestUrl());
         } else if ("POST".equals(request.getRequestMethod())) {
            client = HttpClientMocks.forPost(request.getRequestUrl(), request.getFormData());
         } else {
            throw new RuntimeException(String.format("Unsupported HTTP method: %s!", request.getRequestMethod()));
         }

         for (Map.Entry<String, HeaderEntity> e : request.getHeaders().entrySet()) {
            String name = e.getKey();
            List<String> values = e.getValue().getValues();

            for (String value : values) {
               client.withRequestHeader(name, value);
            }
         }

         client.expectResponseCode(response.getCode());

         if (response.getContentType().length() > 0) {
            client.expectContentType(response.getContentType());
         }

         if (response.getContentLength() >= 0) {
            client.expectContentLength(response.getContentLength());
         }

         if (response.getBody() != null) {
            client.expectResponseBody(response.getBody());
         }

         for (Map.Entry<String, HeaderEntity> e : response.getHeaders().entrySet()) {
            String name = e.getKey();
            List<String> values = e.getValue().getValues();

            if (values.isEmpty()) {
               client.expectResponseHeaders(name);
            } else {
               for (String value : values) {
                  client.expectResponseHeader(name, value);
               }
            }
         }

         client.go();
      }
   },

   VERIFY(HttpTaskType.VERIFY) {
      @Override
      public void execute(ICaseContext ctx) throws Throwable {
         ITask<ITaskType> task = ctx.getTask();
         EunitMethod eunitMethod = task.getEunitMethod();

         ctx.invokeWithInjection(eunitMethod);
      }
   };

   private HttpTaskType m_type;

   private HttpTaskExecutor(HttpTaskType type) {
      m_type = type;
   }

   @Override
   public HttpTaskType getTaskType() {
      return m_type;
   }
}