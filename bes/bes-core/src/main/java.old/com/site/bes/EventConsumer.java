package com.site.bes;

public interface EventConsumer<E extends Event<P>, P extends EventPayload> {
   public EventRetryPolicy getRetryPolicy();

   public void processEvents(EventBatch<E, P> batch);
}
