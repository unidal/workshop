package com.site.kernel.dal.db.cache;

public class CompositeKey implements CacheKey {
   private CacheKey[] m_keys;

   public CompositeKey(CacheKey[] keys) {
      m_keys = keys;

      if (keys == null) {
         throw new NullPointerException();
      }
   }

   public CompositeKey(CacheKey key1, CacheKey key2) {
      this(new CacheKey[] { key1, key2 });
   }

   public CompositeKey(CacheKey key1, CacheKey key2, CacheKey key3) {
      this(new CacheKey[] { key1, key2, key3 });
   }

   public boolean equals(Object anotherKey) {
      if (anotherKey instanceof CompositeKey) {
         CompositeKey key = (CompositeKey) anotherKey;

         if (m_keys.length == key.m_keys.length) {
            for (int i = 0; i < m_keys.length; i++) {
               if (!m_keys[i].equals(key.m_keys[i])) {
                  return false;
               }
            }

            return true;
         }
      }

      return false;
   }

   public int hashCode() {
      int len = m_keys.length;
      int hashCode = 0;

      for (int i = 0; i < len; i++) {
         hashCode = hashCode * 31 + m_keys[i].hashCode();
      }

      return hashCode;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer(256);

      sb.append("CompositeKey[");

      for (int i = 0; i < m_keys.length; i++) {
         if (i > 0) {
            sb.append(", ");
         }

         sb.append(m_keys[i].toString());
      }

      sb.append("]");

      return sb.toString();
   }
}
