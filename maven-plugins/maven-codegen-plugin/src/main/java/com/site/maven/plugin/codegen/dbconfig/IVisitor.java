package com.site.maven.plugin.codegen.dbconfig;

import com.site.maven.plugin.codegen.dbconfig.entity.Config;
import com.site.maven.plugin.codegen.dbconfig.entity.DataSource;
import com.site.maven.plugin.codegen.dbconfig.entity.Excludes;
import com.site.maven.plugin.codegen.dbconfig.entity.Includes;
import com.site.maven.plugin.codegen.dbconfig.entity.Mapping;
import com.site.maven.plugin.codegen.dbconfig.entity.Properties;

public interface IVisitor {

   public void visitConfig(Config config);

   public void visitDataSource(DataSource dataSource);

   public void visitExcludes(Excludes excludes);

   public void visitIncludes(Includes includes);

   public void visitMapping(Mapping mapping);

   public void visitProperties(Properties properties);
}
