package com.site.kernel.dal.datasource;

import java.io.File;

import com.site.kernel.SystemPathFinder;
import com.site.kernel.dal.model.DefaultHandler;
import com.site.kernel.dal.model.ModelRegistry;

/**
 * @author qwu
 */
public class DataSourceInitializer {
   private static final String DATA_SOURCE = "datasource.xml";

   private static void initModels() {
      ModelRegistry.register(com.site.kernel.dal.datasource.DataSourcesDo.class, com.site.kernel.dal.datasource.DataSourcesBo.class);
      ModelRegistry.register(com.site.kernel.dal.datasource.DataSourceDo.class, com.site.kernel.dal.datasource.DataSourceBo.class);
      ModelRegistry.register(com.site.kernel.dal.datasource.ConfigPropertiesDo.class, com.site.kernel.dal.datasource.ConfigPropertiesBo.class);
      ModelRegistry.register(com.site.kernel.dal.datasource.PropertyDo.class, com.site.kernel.dal.datasource.PropertyBo.class);
   }

   public static void initialize() {
      initModels();
      
      File xmlFile = new File(SystemPathFinder.getAppConfig(), DATA_SOURCE);

      try {
         DefaultHandler parser = new DefaultHandler(DataSourcesBo.class);

         parser.parse(xmlFile.getAbsolutePath());
         parser.validateModel();

         DataSourcesBo dataSources = (DataSourcesBo) parser.getRootModel();

         dataSources.initDataSources();
      } catch (DataSourceException e) {
         throw e;
      } catch (Throwable t) {
         throw new DataSourceException(t.getMessage() + ", DataSource: " + xmlFile.getAbsolutePath(), t);
      }
   }

}
