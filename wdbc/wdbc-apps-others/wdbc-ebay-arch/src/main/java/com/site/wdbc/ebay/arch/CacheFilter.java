package com.site.wdbc.ebay.arch;

import java.net.URL;

import com.site.wdbc.http.cache.FileCacheManager.Filter;

public class CacheFilter implements Filter {
   public boolean isSupport(URL url) {
      String path = url.getPath();

      if (path == null || path.length() == 1) {
         return false;
      } else if (path.startsWith("/login/")) {
         return false;
      }

      return true;
   }
}
