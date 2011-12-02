package com.ebay.eunit.report.excel.model.entity;

import com.ebay.eunit.report.excel.model.BaseEntity;
import com.ebay.eunit.report.excel.model.IVisitor;

public class PatternEntity extends BaseEntity<PatternEntity> {
   private String m_type;

   private String m_text;

   public PatternEntity() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitPattern(this);
   }

   public String getText() {
      return m_text;
   }

   public String getType() {
      return m_type;
   }

   @Override
   public void mergeAttributes(PatternEntity other) {
      if (other.getType() != null) {
         m_type = other.getType();
      }
   }

   public PatternEntity setText(String text) {
      m_text=text;
      return this;
   }

   public PatternEntity setType(String type) {
      m_type=type;
      return this;
   }

}
