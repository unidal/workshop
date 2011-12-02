package com.site.kernel.dal.datasource;

import com.site.kernel.SystemRegistry;

public class DataSourceBo extends DataSourceDo {

   static {
      init();
   }

   public ConfigPropertiesBo getConfigPropertiesBo() {
      return (ConfigPropertiesBo) getConfigPropertiesDo();
   }

   public void setConfigPropertiesBo(ConfigPropertiesBo configProperties) {
      setConfigPropertiesDo(configProperties);
   }

   public void initDataSource() {
      Class dsClass = (Class) SystemRegistry.lookup(DataSource.class, getType());

      try {
         DataSource ds = (DataSource) dsClass.newInstance();

         ds.initialize(this);
         SystemRegistry.register(DataSource.class, getType() + ":" + getName(), ds);
      } catch (Exception e) {
         throw new DataSourceException("Error initializing data source: " + getName() + ", message: " + e.toString(), e);
      }
   }

}
