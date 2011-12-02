package com.site.kernel.dal.db.helpers;

public abstract class Token {
   private String m_token;

   public Token(String token) {
      m_token = token;
   }

   protected String getToken() {
      return m_token;
   }

   public abstract String getToken(QueryContext ctx);
}