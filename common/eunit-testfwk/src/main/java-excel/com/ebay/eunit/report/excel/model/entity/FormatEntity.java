package com.ebay.eunit.report.excel.model.entity;

import static com.ebay.eunit.report.excel.model.Constants.ATTR_ID;
import static com.ebay.eunit.report.excel.model.Constants.ENTITY_FORMAT;

import com.ebay.eunit.report.excel.model.BaseEntity;
import com.ebay.eunit.report.excel.model.IVisitor;

public class FormatEntity extends BaseEntity<FormatEntity> {
   private String m_id;

   private FontEntity m_font;

   private PatternEntity m_pattern;

   public FormatEntity(String id) {
      m_id = id;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitFormat(this);
   }

   public FontEntity getFont() {
      return m_font;
   }

   public String getId() {
      return m_id;
   }

   public PatternEntity getPattern() {
      return m_pattern;
   }

   @Override
   public void mergeAttributes(FormatEntity other) {
      assertAttributeEquals(other, ENTITY_FORMAT, ATTR_ID, m_id, other.getId());

   }

   public FormatEntity setFont(FontEntity font) {
      m_font=font;
      return this;
   }

   public FormatEntity setPattern(PatternEntity pattern) {
      m_pattern=pattern;
      return this;
   }

}
