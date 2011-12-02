package com.site.maven.plugin.codegen.dbconfig.entity;

import com.site.maven.plugin.codegen.dbconfig.BaseEntity;
import com.site.maven.plugin.codegen.dbconfig.IVisitor;

public class Mapping extends BaseEntity<Mapping> {
   private String m_name;

   private String m_pattern;

   public Mapping() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitMapping(this);
   }

   public String getName() {
      return m_name;
   }

   public String getPattern() {
      return m_pattern;
   }

   @Override
   public void mergeAttributes(Mapping other) {
      if (other.getName() != null) {
         m_name = other.getName();
      }

      if (other.getPattern() != null) {
         m_pattern = other.getPattern();
      }
   }

   public Mapping setName(String name) {
      m_name=name;
      return this;
   }

   public Mapping setPattern(String pattern) {
      m_pattern=pattern;
      return this;
   }

}
