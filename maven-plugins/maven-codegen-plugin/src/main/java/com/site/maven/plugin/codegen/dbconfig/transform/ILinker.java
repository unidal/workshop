package com.site.maven.plugin.codegen.dbconfig.transform;

import com.site.maven.plugin.codegen.dbconfig.entity.Config;
import com.site.maven.plugin.codegen.dbconfig.entity.DataSource;
import com.site.maven.plugin.codegen.dbconfig.entity.Excludes;
import com.site.maven.plugin.codegen.dbconfig.entity.Includes;
import com.site.maven.plugin.codegen.dbconfig.entity.Mapping;
import com.site.maven.plugin.codegen.dbconfig.entity.Properties;

public interface ILinker {

   public boolean onDataSource(Config parent, DataSource dataSource);

   public boolean onExcludes(Config parent, Excludes excludes);

   public boolean onIncludes(Config parent, Includes includes);

   public boolean onMapping(Config parent, Mapping mapping);

   public boolean onProperties(DataSource parent, Properties properties);
}
