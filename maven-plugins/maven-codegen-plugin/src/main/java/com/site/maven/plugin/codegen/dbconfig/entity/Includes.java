package com.site.maven.plugin.codegen.dbconfig.entity;

import java.util.ArrayList;
import java.util.List;

import com.site.maven.plugin.codegen.dbconfig.BaseEntity;
import com.site.maven.plugin.codegen.dbconfig.IVisitor;

public class Includes extends BaseEntity<Includes> {
   private List<String> m_includes = new ArrayList<String>();

   public Includes() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitIncludes(this);
   }

   public Includes addInclude(String include) {
      m_includes.add(include);
      return this;
   }

   public List<String> getIncludes() {
      return m_includes;
   }

   @Override
   public void mergeAttributes(Includes other) {
   }

}
