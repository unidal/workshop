package com.site.wdbc.http.cache;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;

import com.site.wdbc.WdbcSourceType;
import com.site.wdbc.http.Response;
import com.site.wdbc.http.cache.FileCacheManager.CacheResponse;

public class FileCacheManagerTest {
   @Test
   public void testCache() throws MalformedURLException, IOException {
      FileCacheManager manager = new FileCacheManager();
      URL url = new URL("http://www.google.com.hk/search?hl=zh-CN&source=hp");
      CacheResponse oldResponse = new CacheResponse(WdbcSourceType.XML, "utf-8", "Hello, world!");

      manager.setCache(url, oldResponse);

      Response newResponse = manager.getCache(url);

      Assert.assertEquals(oldResponse, newResponse);
   }
}
