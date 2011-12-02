package com.ebay.eunit.cmd.model.entity;

import static com.ebay.eunit.cmd.model.Constants.ATTR_NAME;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_HEADER;

import java.util.ArrayList;
import java.util.List;

import com.ebay.eunit.cmd.model.BaseEntity;
import com.ebay.eunit.cmd.model.IVisitor;

public class HeaderEntity extends BaseEntity<HeaderEntity> {
   private String m_name;

   private List<String> m_values = new ArrayList<String>();

   public HeaderEntity(String name) {
      m_name = name;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitHeader(this);
   }

   public HeaderEntity addValue(String value) {
      m_values.add(value);
      return this;
   }

   public String getName() {
      return m_name;
   }

   public List<String> getValues() {
      return m_values;
   }

   @Override
   public void mergeAttributes(HeaderEntity other) {
      assertAttributeEquals(other, ENTITY_HEADER, ATTR_NAME, m_name, other.getName());

   }

}
