package com.site.bes.queue.memcache.lock;

public interface Lock {
   public boolean isLocked();
   
   public boolean lock(long timeout);
   
   public boolean unlock();
}
