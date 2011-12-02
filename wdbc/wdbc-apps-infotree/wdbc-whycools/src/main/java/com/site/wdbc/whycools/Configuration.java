package com.site.wdbc.whycools;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.codehaus.plexus.component.repository.io.PlexusTools;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.codehaus.plexus.util.ReaderFactory;

public class Configuration implements Initializable {
   private String m_config;

   private Properties m_properties;

   private PlexusConfiguration m_configuration;

   public String getAction() {
      return getParameterValue("action");
   }

   public String getCategory() {
      return getParameterValue("category");
   }

   public String getCategoryInfoTree() {
      return getParameterValue("category.infotree");
   }

   public String getCityId() {
      return getParameterValue("city");
   }

   public String getCityName() {
      String cityId = getCityId();

      return getCityValue(cityId, "name");
   }

   protected String getCityValue(String cityId, String attribute) {
      PlexusConfiguration cityMap = m_configuration.getChild("city-map");
      String value = null;

      for (PlexusConfiguration city : cityMap.getChildren()) {
         String id = city.getAttribute("id", "");

         if (id.equals(cityId)) {
            value = city.getAttribute(attribute, null);
         }
      }

      if (value == null) {
         throw new IllegalArgumentException("Attribute(" + attribute + ") of city(" + cityId
               + ") should be defined at " + m_config);
      } else {
         return value;
      }
   }

   public int getMaxDays() {
      return getParameterValue("maxDays", 3);
   }

   public int getMaxPages() {
      return getParameterValue("maxPages", 2);
   }

   protected String getParameterValue(String property) {
      String value = m_properties.getProperty(property);

      if (value == null) {
         PlexusConfiguration parameters = m_configuration.getChild("parameters");
         PlexusConfiguration parameter = parameters.getChild(property);
         PlexusConfiguration defaultValue = parameter.getChild("default-value");

         value = defaultValue.getValue(null);

         if (value == null) {
            throw new IllegalArgumentException("Parameter(" + property + ") should be defined at " + m_config
                  + " or be passed in from command line");
         }
      }

      return value;
   }

   protected int getParameterValue(String property, int defaultValue) {
      try {
         return Integer.parseInt(getParameterValue(property));
      } catch (Exception e) {
         return defaultValue;
      }
   }

   protected String getParameterValue(String property, String defaultValue) {
      try {
         return getParameterValue(property);
      } catch (Exception e) {
         return defaultValue;
      }
   }

   public String getPassword() {
      return getParameterValue("password");
   }

   public String getProvinceName() {
      String cityId = getCityId();

      return getCityValue(cityId, "province");
   }

   public String getType() {
      return getParameterValue("type");
   }

   public String getUserName() {
      return getParameterValue("username");
   }
   
   public void initialize() throws InitializationException {
      Reader reader;

      try {
         if (new File(m_config).canRead()) {
            reader = ReaderFactory.newXmlReader(new File(m_config));
         } else {
            InputStream is = getClass().getClassLoader().getResourceAsStream(m_config);

            if (is != null) {
               reader = ReaderFactory.newXmlReader(is);
            } else {
               throw new InitializationException(m_config + " can't be found at current directory or as resource");
            }
         }

         m_configuration = PlexusTools.buildConfiguration(m_config, reader);
         reader.close();
      } catch (Exception e) {
         throw new InitializationException("Error when loading XML configuration " + m_config, e);
      }

      m_properties = System.getProperties();
   }

   public void setConfig(String config) {
      m_config = System.getProperty("config", config);
   }
}
