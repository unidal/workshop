package com.site.wdbc.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;

import com.site.wdbc.WdbcSource;
import com.site.wdbc.http.cache.CacheManager;

public interface Session {
   public Request createRequest(String url) throws MalformedURLException;

   public List<Header> getHeaders();

   public HttpClient getHttpClient();

   public CacheManager getCacheManager();
   
   public URL getLastUrl();

   public Map<String, String> getProperties();

   public Object getProperty(String name, Object defaultValue);

   public WdbcSource getResponseSource();

   public String parseValue(String value);

   public void popProperties();

   public void pushProperties();

   public URL resolveUrl(Request request) throws MalformedURLException;

   public void setHeader(String name, String value);

   public void setLastUrl(URL lastUrl);

   public void setProperty(String name, Object value);

   public void setResponseSource(WdbcSource source);
}
