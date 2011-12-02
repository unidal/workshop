package com.site.bes.queue.memcache;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class Cache implements Initializable {
   private MemCachedClient m_client = new MemCachedClient();

   public void initialize() throws InitializationException {
      SockIOPool pool = SockIOPool.getInstance();

      pool.setServers(new String[] { "localhost:11211" });
      pool.setMinConn(1);
      pool.setMaxConn(1);
      pool.initialize();
   }

   @SuppressWarnings("unchecked")
   public <T> T get(String key) {
      return (T) m_client.get(key);
   }

   public boolean add(String key, Object value) {
      return m_client.add(key, value);
   }

   public int getCounter(String key) {
      return (int) m_client.getCounter(key);
   }

   public boolean storeCounter(String key, int value) {
      return m_client.storeCounter(key, value);
   }

   public int incr(String key) {
      return (int) m_client.incr(key);
   }

   public int decr(String key) {
      return (int) m_client.decr(key);
   }

   public int addOrIncr(String key, int incr) {
      return (int) m_client.addOrIncr(key, incr);
   }

   public boolean set(String key, String value) {
      return m_client.set(key, value);
   }

   // only used by test case
   public void clearAll() {
      m_client.flushAll();
   }
}
