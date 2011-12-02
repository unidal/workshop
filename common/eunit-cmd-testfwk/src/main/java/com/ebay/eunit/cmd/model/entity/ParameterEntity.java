package com.ebay.eunit.cmd.model.entity;

import static com.ebay.eunit.cmd.model.Constants.ATTR_NAME;
import static com.ebay.eunit.cmd.model.Constants.ENTITY_PARAMETER;

import java.util.ArrayList;
import java.util.List;

import com.ebay.eunit.cmd.model.BaseEntity;
import com.ebay.eunit.cmd.model.IVisitor;

public class ParameterEntity extends BaseEntity<ParameterEntity> {
   private String m_name;

   private List<String> m_values = new ArrayList<String>();

   public ParameterEntity(String name) {
      m_name = name;
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitParameter(this);
   }

   public ParameterEntity addValue(String value) {
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
   public void mergeAttributes(ParameterEntity other) {
      assertAttributeEquals(other, ENTITY_PARAMETER, ATTR_NAME, m_name, other.getName());

   }

}
