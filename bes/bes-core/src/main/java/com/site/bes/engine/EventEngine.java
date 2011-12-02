package com.site.bes.engine;

import com.site.bes.consumer.EventConsumerListener;

public interface EventEngine extends EventConsumerListener {
   public void start();

   public void stop();
}
