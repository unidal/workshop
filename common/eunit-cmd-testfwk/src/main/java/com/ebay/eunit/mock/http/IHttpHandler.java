package com.ebay.eunit.mock.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IHttpHandler {
   public String getProtocol();

   public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}