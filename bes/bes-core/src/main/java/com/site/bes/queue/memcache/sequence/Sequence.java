package com.site.bes.queue.memcache.sequence;

import com.site.bes.queue.memcache.Cacheable;

public interface Sequence extends Cacheable {
   public String getName();

   public int getCurrentValue();

   public int getNextValue();
}
