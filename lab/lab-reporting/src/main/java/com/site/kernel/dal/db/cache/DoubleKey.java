package com.site.kernel.dal.db.cache;

public class DoubleKey implements CacheKey {
   private double m_value;

   public DoubleKey(double value) {
      m_value = value;
   }

   public boolean equals(Object anotherKey) {
      if (anotherKey instanceof DoubleKey) {
         DoubleKey key = (DoubleKey) anotherKey;

         return m_value == key.m_value;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return new Double(m_value).hashCode();
   }

   public String toString() {
      return "DoubleKey[" + m_value + "]";
   }
}