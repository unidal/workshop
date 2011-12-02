package com.ebay.eunit.report.excel.model.entity;

import com.ebay.eunit.report.excel.model.BaseEntity;
import com.ebay.eunit.report.excel.model.IVisitor;

public class FontEntity extends BaseEntity<FontEntity> {
   private String m_name;

   private Integer m_size;

   private Boolean m_bold;

   private Boolean m_italic;

   public FontEntity() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitFont(this);
   }

   public Boolean getBold() {
      return m_bold;
   }

   public Boolean getItalic() {
      return m_italic;
   }

   public String getName() {
      return m_name;
   }

   public Integer getSize() {
      return m_size;
   }

   public boolean isBold() {
      return m_bold != null && m_bold.booleanValue();
   }

   public boolean isItalic() {
      return m_italic != null && m_italic.booleanValue();
   }

   @Override
   public void mergeAttributes(FontEntity other) {
      if (other.getName() != null) {
         m_name = other.getName();
      }

      if (other.getSize() != null) {
         m_size = other.getSize();
      }

      if (other.getBold() != null) {
         m_bold = other.getBold();
      }

      if (other.getItalic() != null) {
         m_italic = other.getItalic();
      }
   }

   public FontEntity setBold(Boolean bold) {
      m_bold=bold;
      return this;
   }

   public FontEntity setItalic(Boolean italic) {
      m_italic=italic;
      return this;
   }

   public FontEntity setName(String name) {
      m_name=name;
      return this;
   }

   public FontEntity setSize(Integer size) {
      m_size=size;
      return this;
   }

}
