package com.site.kernel.dal.db.cache;

public class IntKey implements CacheKey {
   private int m_value;

   public IntKey(int value) {
      m_value = value;
   }

   public boolean equals(Object anotherKey) {
      if (anotherKey instanceof IntKey) {
         IntKey key = (IntKey) anotherKey;

         return m_value == key.m_value;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return m_value;
   }

   public String toString() {
      return "IntKey[" + m_value + "]";
   }
}