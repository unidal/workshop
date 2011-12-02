package com.ebay.eunit.report.excel.model.entity;

import com.ebay.eunit.report.excel.model.BaseEntity;
import com.ebay.eunit.report.excel.model.IVisitor;

public class DateTimeEntity extends BaseEntity<DateTimeEntity> {
   private String m_format;

   private Integer m_offset;

   private String m_text;

   public DateTimeEntity() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitDateTime(this);
   }

   public String getFormat() {
      return m_format;
   }

   public Integer getOffset() {
      return m_offset;
   }

   public String getText() {
      return m_text;
   }

   @Override
   public void mergeAttributes(DateTimeEntity other) {
      if (other.getFormat() != null) {
         m_format = other.getFormat();
      }

      if (other.getOffset() != null) {
         m_offset = other.getOffset();
      }
   }

   public DateTimeEntity setFormat(String format) {
      m_format=format;
      return this;
   }

   public DateTimeEntity setOffset(Integer offset) {
      m_offset=offset;
      return this;
   }

   public DateTimeEntity setText(String text) {
      m_text=text;
      return this;
   }

}
