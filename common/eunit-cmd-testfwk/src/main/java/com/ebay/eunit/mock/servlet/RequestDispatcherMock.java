package com.ebay.eunit.mock.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class RequestDispatcherMock implements RequestDispatcher {
   private String m_path;

   public RequestDispatcherMock(String path) {
      m_path = path;
   }

   @Override
   public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
      throw new UnsupportedOperationException("Not implemented!");
   }

   public String getPath() {
      return m_path;
   }

   @Override
   public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
      throw new UnsupportedOperationException("Not implemented!");
   }
}
