package com.site.kernel.dal.db.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheAdapter {
   private Map<CacheKey, Object> m_cache;

   protected CacheAdapter() {
      m_cache = new HashMap<CacheKey, Object>();
   }

   public Object getFromCache(CacheKey key) {
      return m_cache.get(key);
   }

   public void putToCache(CacheKey key, Object obj) {
      m_cache.put(key, obj);
   }

   public void removeFromCache(CacheKey key) {
      m_cache.remove(key);
   }
}
