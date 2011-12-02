package com.site.maven.plugin.codegen.dbconfig.entity;

import java.util.ArrayList;
import java.util.List;

import com.site.maven.plugin.codegen.dbconfig.BaseEntity;
import com.site.maven.plugin.codegen.dbconfig.IVisitor;

public class Excludes extends BaseEntity<Excludes> {
   private List<String> m_excludes = new ArrayList<String>();

   public Excludes() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitExcludes(this);
   }

   public Excludes addExclude(String exclude) {
      m_excludes.add(exclude);
      return this;
   }

   public List<String> getExcludes() {
      return m_excludes;
   }

   @Override
   public void mergeAttributes(Excludes other) {
   }

}
