package com.site.kernel.dal.datasource;

import java.util.List;
import java.util.Properties;


public class ConfigPropertiesBo extends ConfigPropertiesDo {

   static {
      init();
   }

   public void addPropertyBo(PropertyBo property) {
      addPropertyDo(property);
   }

   public List getPropertyBos() {
      return getPropertyDos();
   }

   public Properties getProperties() {
      Properties props = new Properties();
      List bos = getPropertyBos();

      for (int i = 0; i < bos.size(); i++) {
         PropertyBo bo = (PropertyBo) bos.get(i);

         props.put(bo.getName(), bo.getValue());
      }
      
      return props;
   }

}
