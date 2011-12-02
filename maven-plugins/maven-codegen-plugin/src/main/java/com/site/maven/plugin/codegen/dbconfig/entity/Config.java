package com.site.maven.plugin.codegen.dbconfig.entity;

import java.util.ArrayList;
import java.util.List;

import com.site.maven.plugin.codegen.dbconfig.BaseEntity;
import com.site.maven.plugin.codegen.dbconfig.IVisitor;

public class Config extends BaseEntity<Config> {
   private List<Mapping> m_mappings = new ArrayList<Mapping>();

   private Includes m_includes;

   private Excludes m_excludes;

   private List<DataSource> m_dataSources = new ArrayList<DataSource>();

   public Config() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitConfig(this);
   }

   public Config addDataSource(DataSource dataSource) {
      m_dataSources.add(dataSource);
      return this;
   }

   public Config addMapping(Mapping mapping) {
      m_mappings.add(mapping);
      return this;
   }

   public List<DataSource> getDataSources() {
      return m_dataSources;
   }

   public Excludes getExcludes() {
      return m_excludes;
   }

   public Includes getIncludes() {
      return m_includes;
   }

   public List<Mapping> getMappings() {
      return m_mappings;
   }

   @Override
   public void mergeAttributes(Config other) {
   }

   public Config setExcludes(Excludes excludes) {
      m_excludes=excludes;
      return this;
   }

   public Config setIncludes(Includes includes) {
      m_includes=includes;
      return this;
   }

}
