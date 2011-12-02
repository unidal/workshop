package com.site.maven.plugin.codegen.dbconfig.transform;

import com.site.maven.plugin.codegen.dbconfig.entity.Config;
import com.site.maven.plugin.codegen.dbconfig.entity.DataSource;
import com.site.maven.plugin.codegen.dbconfig.entity.Excludes;
import com.site.maven.plugin.codegen.dbconfig.entity.Includes;
import com.site.maven.plugin.codegen.dbconfig.entity.Mapping;
import com.site.maven.plugin.codegen.dbconfig.entity.Properties;

public interface IMaker<T> {

   public Config buildConfig(T node);

   public DataSource buildDataSource(T node);

   public String buildExclude(T node);

   public Excludes buildExcludes(T node);

   public String buildInclude(T node);

   public Includes buildIncludes(T node);

   public Mapping buildMapping(T node);

   public Properties buildProperties(T node);
}
