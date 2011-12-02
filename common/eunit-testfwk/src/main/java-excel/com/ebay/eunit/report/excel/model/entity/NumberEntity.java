package com.ebay.eunit.report.excel.model.entity;

import com.ebay.eunit.report.excel.model.BaseEntity;
import com.ebay.eunit.report.excel.model.IVisitor;

public class NumberEntity extends BaseEntity<NumberEntity> {
   private String m_format;

   private Double m_text;

   public NumberEntity() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitNumber(this);
   }

   public String getFormat() {
      return m_format;
   }

   public Double getText() {
      return m_text;
   }

   @Override
   public void mergeAttributes(NumberEntity other) {
      if (other.getFormat() != null) {
         m_format = other.getFormat();
      }
   }

   public NumberEntity setFormat(String format) {
      m_format=format;
      return this;
   }

   public NumberEntity setText(Double text) {
      m_text=text;
      return this;
   }

}
