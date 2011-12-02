package com.site.kernel.dal.db.cache;

public class LongKey implements CacheKey {
   private long m_value;

   public LongKey(long value) {
      m_value = value;
   }

   public boolean equals(Object anotherKey) {
      if (anotherKey instanceof LongKey) {
         LongKey key = (LongKey) anotherKey;

         return m_value == key.m_value;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (int) (m_value ^ (m_value >>> 32));
   }

   public String toString() {
      return "LongKey[" + m_value + "]";
   }
}