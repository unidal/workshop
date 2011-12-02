package com.site.wdbc.http;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.NameValuePair;

import com.site.wdbc.http.impl.HttpMethod;

public interface Request {
   public String getAction();

   public HttpMethod getMethod();

   public NameValuePair[] getInputs();

   public Response execute(Session session) throws HttpException, IOException;
}
