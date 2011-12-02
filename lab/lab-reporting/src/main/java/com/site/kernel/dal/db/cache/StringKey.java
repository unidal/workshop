package com.site.kernel.dal.db.cache;

public class StringKey implements CacheKey {
   private String m_value;

   public StringKey(String value) {
      m_value = value;
   }

   public boolean equals(Object anotherKey) {
      if (anotherKey instanceof StringKey) {
         StringKey key = (StringKey) anotherKey;

         if (m_value == null) {
            return key.m_value == null;
         } else {
            return m_value.equals(key.m_value);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (m_value == null ? 0 : m_value.hashCode());
   }

   public String toString() {
      return "StringKey[" + m_value + "]";
   }
}