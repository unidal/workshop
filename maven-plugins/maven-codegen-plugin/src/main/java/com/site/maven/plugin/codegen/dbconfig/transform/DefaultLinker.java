package com.site.maven.plugin.codegen.dbconfig.transform;

import com.site.maven.plugin.codegen.dbconfig.entity.Config;
import com.site.maven.plugin.codegen.dbconfig.entity.DataSource;
import com.site.maven.plugin.codegen.dbconfig.entity.Excludes;
import com.site.maven.plugin.codegen.dbconfig.entity.Includes;
import com.site.maven.plugin.codegen.dbconfig.entity.Mapping;
import com.site.maven.plugin.codegen.dbconfig.entity.Properties;

public class DefaultLinker implements ILinker {

   @Override
   public boolean onDataSource(Config parent, DataSource dataSource) {
      parent.addDataSource(dataSource);
      return true;
   }

   @Override
   public boolean onExcludes(Config parent, Excludes excludes) {
      parent.setExcludes(excludes);
      return true;
   }

   @Override
   public boolean onIncludes(Config parent, Includes includes) {
      parent.setIncludes(includes);
      return true;
   }

   @Override
   public boolean onMapping(Config parent, Mapping mapping) {
      parent.addMapping(mapping);
      return true;
   }

   @Override
   public boolean onProperties(DataSource parent, Properties properties) {
      parent.setProperties(properties);
      return true;
   }
}
