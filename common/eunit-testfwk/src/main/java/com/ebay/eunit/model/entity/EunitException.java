package com.ebay.eunit.model.entity;

import com.ebay.eunit.model.BaseEntity;
import com.ebay.eunit.model.IVisitor;

public class EunitException extends BaseEntity<EunitException> {
   private Class<?> m_type;

   private String m_message;

   private String m_pattern;

   public EunitException() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitEunitException(this);
   }

   public String getMessage() {
      return m_message;
   }

   public String getPattern() {
      return m_pattern;
   }

   public Class<?> getType() {
      return m_type;
   }

   @Override
   public void mergeAttributes(EunitException other) {
      if (other.getType() != null) {
         m_type = other.getType();
      }

      if (other.getMessage() != null) {
         m_message = other.getMessage();
      }

      if (other.getPattern() != null) {
         m_pattern = other.getPattern();
      }
   }

   public EunitException setMessage(String message) {
      m_message=message;
      return this;
   }

   public EunitException setPattern(String pattern) {
      m_pattern=pattern;
      return this;
   }

   public EunitException setType(Class<?> type) {
      m_type=type;
      return this;
   }

}
