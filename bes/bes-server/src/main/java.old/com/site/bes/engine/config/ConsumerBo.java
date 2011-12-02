package com.site.bes.engine.config;

public class ConsumerBo extends ConsumerDo {

   static {
      init();
   }

   public ConfigurationBo getConfigurationBo() {
      return (ConfigurationBo) getConfigurationDo();
   }

   public ListenOnBo getListenOnBo() {
      return (ListenOnBo) getListenOnDo();
   }

   public void setConfigurationBo(ConfigurationBo configuration) {
      setConfigurationDo(configuration);
   }

   public void setListenOnBo(ListenOnBo listenOn) {
      setListenOnDo(listenOn);
   }

   public void startThread(String eventType) {
      if (getListenOnBo() != null) {
         getListenOnBo().startThread(this, eventType);
      }
   }

   public void stopThread(String eventType) {
      if (getListenOnBo() != null) {
         getListenOnBo().stopThread(this, eventType);
      }
   }

   public boolean isThreadRunning(String eventType) {
      if (getListenOnBo() != null) {
         return getListenOnBo().isThreadRunning(eventType);
      }
      
      return false;
   }

}
