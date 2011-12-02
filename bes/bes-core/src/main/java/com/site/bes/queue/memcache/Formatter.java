package com.site.bes.queue.memcache;

public interface Formatter<T, K> {
   public String getId(K key) throws FormatterException;

   public String getValue(T object) throws FormatterException;

   public T parseValue(String value) throws FormatterException;
}
