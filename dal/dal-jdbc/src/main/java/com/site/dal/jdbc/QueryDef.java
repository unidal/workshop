package com.site.dal.jdbc;

import java.util.List;

import com.site.dal.jdbc.query.token.Token;
import com.site.dal.jdbc.query.token.TokenParser;
import com.site.dal.jdbc.raw.RawEntity;

public class QueryDef {
   private Class<?> m_entityClass;

   private String m_pattern;

   private QueryType m_type;

   private List<Token> m_tokens;

   private boolean m_storeProcedure;

   private boolean m_raw;

   public QueryDef(Class<?> entityClass, QueryType type, String pattern) {
      m_entityClass = entityClass;
      m_type = type;
      m_pattern = pattern;
      m_raw = RawEntity.class.isAssignableFrom(entityClass);
   }

   public QueryDef(Class<?> entityClass, QueryType type, String pattern, boolean isStoreProcedure) {
      this(entityClass, type, pattern);

      m_storeProcedure = isStoreProcedure;
   }

   public Class<?> getEntityClass() {
      return m_entityClass;
   }

   public QueryType getType() {
      return m_type;
   }

   public boolean isRaw() {
      return m_raw;
   }

   public boolean isStoreProcedure() {
      return m_storeProcedure;
   }

   public List<Token> parse(TokenParser parser) {
      if (m_tokens == null) {
         synchronized (this) {
            if (m_tokens == null) {
               m_tokens = parser.parse(m_pattern);
            }
         }
      }

      return m_tokens;
   }

   @Override
   public String toString() {
      return m_pattern;
   }
}
