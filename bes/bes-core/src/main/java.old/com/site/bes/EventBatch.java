package com.site.bes;

public interface EventBatch<T extends Event<P>, P extends EventPayload> extends Iterable<T> {
   public T get(int index);

   public int length();
}
