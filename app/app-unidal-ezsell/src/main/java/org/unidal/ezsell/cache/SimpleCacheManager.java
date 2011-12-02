package org.unidal.ezsell.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.site.dal.jdbc.Readset;

public class SimpleCacheManager<K, V> implements CacheManager<K, V>, Initializable {
   private int m_initialSize = 1024;

   private Map<K, Map<Readset<?>, V>> m_cache;

   /*
    * (non-Javadoc)
    * 
    * @see org.unidal.ezsell.cache.CacheManager#evictFromCache(K)
    */
   public void evictFromCache(K key) {
      m_cache.remove(key);
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.unidal.ezsell.cache.CacheManager#getFromCache(K,
    * com.site.dal.jdbc.Readset)
    */
   public V getFromCache(K key, Readset<?> readset) {
      Map<Readset<?>, V> map = m_cache.get(key);
      V value = map == null ? null : map.get(readset);

      return value;
   }

   public void initialize() throws InitializationException {
      m_cache = new ConcurrentHashMap<K, Map<Readset<?>, V>>(m_initialSize);
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.unidal.ezsell.cache.CacheManager#putToCache(K,
    * com.site.dal.jdbc.Readset, V)
    */
   public void putToCache(K key, Readset<?> readset, V value) {
      Map<Readset<?>, V> map = m_cache.get(key);

      if (map == null) {
         synchronized (m_cache) {
            map = m_cache.get(key);

            if (map == null) {
               map = new HashMap<Readset<?>, V>();
               m_cache.put(key, map);
            }
         }
      }

      synchronized (map) {
         map.put(readset, value);
      }
   }

   public void setInitialSize(int initialSize) {
      m_initialSize = initialSize;
   }
}
