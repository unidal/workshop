package org.unidal.expense.dal.cache;

import com.site.dal.jdbc.Readset;

public interface CacheManager<K, V> {
   public void evictFromCache(K key);

   public V getFromCache(K key, Readset<?> readset);

   public void putToCache(K key, Readset<?> readset, V value);
}