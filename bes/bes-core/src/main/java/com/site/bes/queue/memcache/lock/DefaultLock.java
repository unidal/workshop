package com.site.bes.queue.memcache.lock;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.site.bes.queue.memcache.Cache;

public class DefaultLock implements Lock, Initializable {
   private Cache m_cache;

   private String m_key = "lock";

   public void initialize() throws InitializationException {
      m_cache.addOrIncr(m_key, 0);
   }

   public boolean isLocked() {
      synchronized (this) {
         return m_cache.getCounter(m_key) > 0;
      }
   }

   public boolean lock(long timeout) {
      long start = System.currentTimeMillis();

      while (true) {
         synchronized (this) {
            int count = m_cache.addOrIncr(m_key, 1);

            if (count == 1) {
               // locked, we are winner
               return true;
            } else if (count > 1) {
               // concurrent locking happen, we are not winner
               m_cache.decr(m_key);
            } else if (count < 1) {
               throw new IllegalStateException("Invalid state of lock, please make sure memcached is started.");
            }
         }

         try {
            // sleep for a while (10 ms)
            Thread.sleep(10);
         } catch (Exception e) {
            // ignore it
            return false;
         }

         long end = System.currentTimeMillis();

         if (timeout > 0 && start + timeout < end) {
            return false;
         }
      }
   }

   public boolean unlock() {
      synchronized (this) {
         m_cache.decr(m_key);
      }

      return true;
   }
}
