package com.site.maven.plugin.codegen.dbconfig.transform;

import com.site.maven.plugin.codegen.dbconfig.entity.Config;
import com.site.maven.plugin.codegen.dbconfig.entity.DataSource;
import com.site.maven.plugin.codegen.dbconfig.entity.Excludes;
import com.site.maven.plugin.codegen.dbconfig.entity.Includes;
import com.site.maven.plugin.codegen.dbconfig.entity.Mapping;
import com.site.maven.plugin.codegen.dbconfig.entity.Properties;

public interface IParser<T> {
   public Config parse(IMaker<T> maker, ILinker linker, T node);

   public void parseForDataSource(IMaker<T> maker, ILinker linker, DataSource parent, T node);

   public void parseForExcludes(IMaker<T> maker, ILinker linker, Excludes parent, T node);

   public void parseForIncludes(IMaker<T> maker, ILinker linker, Includes parent, T node);

   public void parseForMapping(IMaker<T> maker, ILinker linker, Mapping parent, T node);

   public void parseForProperties(IMaker<T> maker, ILinker linker, Properties parent, T node);
}
