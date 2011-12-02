package com.site.dal.jdbc.query.token;

import java.util.LinkedHashMap;
import java.util.Map;

import com.site.dal.jdbc.DalRuntimeException;

public class SimpleTagToken implements Token {
   private String m_token;

   private Map<String, String> m_attributes;

   private TokenType m_tag;

   public SimpleTagToken(String token, Map<String, String> attributes) {
      String upperToken = token.toUpperCase();

      m_token = upperToken;
      m_attributes = new LinkedHashMap<String, String>(attributes);

      try {
         m_tag = TokenType.valueOf(upperToken);
      } catch (IllegalArgumentException e) {
         throw new DalRuntimeException("Unsupported token: " + upperToken, e);
      }
   }

   public TokenType getType() {
      return m_tag;
   }

   public String getAttribute(String name, String defaultValue) {
      String value = m_attributes.get(name);

      if (value == null) {
         return defaultValue;
      } else {
         return value;
      }
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder(256);

      sb.append('<').append(m_token);

      for (Map.Entry<String, String> entry : m_attributes.entrySet()) {
         sb.append(' ').append(entry.getKey()).append("='").append(entry.getValue()).append('\'');
      }

      sb.append("/>");
      return sb.toString();
   }
}
