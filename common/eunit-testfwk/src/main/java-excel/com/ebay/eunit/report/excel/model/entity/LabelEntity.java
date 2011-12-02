package com.ebay.eunit.report.excel.model.entity;

import com.ebay.eunit.report.excel.model.BaseEntity;
import com.ebay.eunit.report.excel.model.IVisitor;

public class LabelEntity extends BaseEntity<LabelEntity> {
   private String m_format;

   private String m_text;

   public LabelEntity() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitLabel(this);
   }

   public String getFormat() {
      return m_format;
   }

   public String getText() {
      return m_text;
   }

   @Override
   public void mergeAttributes(LabelEntity other) {
      if (other.getFormat() != null) {
         m_format = other.getFormat();
      }
   }

   public LabelEntity setFormat(String format) {
      m_format=format;
      return this;
   }

   public LabelEntity setText(String text) {
      m_text=text;
      return this;
   }

}
