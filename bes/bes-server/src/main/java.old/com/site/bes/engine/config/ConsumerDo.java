package com.site.bes.engine.config;
import static com.site.kernel.dal.Cardinality.ZERO_TO_ONE;
import static com.site.kernel.dal.ValueType.BOOLEAN;
import static com.site.kernel.dal.ValueType.TIME;
import static com.site.kernel.dal.ValueType.STRING;
import static com.site.kernel.dal.model.NodeType.ATTRIBUTE;
import static com.site.kernel.dal.model.NodeType.ELEMENT;
import static com.site.kernel.dal.model.NodeType.MODEL;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;
import com.site.kernel.dal.model.ValidationException;

public class ConsumerDo extends XmlModel {
   public static final XmlModelField TYPE = new XmlModelField("type", ATTRIBUTE, STRING);
   public static final XmlModelField ENABLED = new XmlModelField("enabled", ATTRIBUTE, BOOLEAN, "true");
   public static final XmlModelField CHECK_INTERVAL = new XmlModelField("check-interval", ELEMENT, TIME, "1m");

   public static final XmlModelField CONFIGURATION = new XmlModelField("configuration", MODEL, ZERO_TO_ONE, ConfigurationDo.class);
   public static final XmlModelField LISTEN_ON = new XmlModelField("listen-on", MODEL, ZERO_TO_ONE, ListenOnDo.class);

   private String m_type;
   private boolean m_enabled;
   private long m_checkInterval;

   private ConfigurationDo m_configuration;
   private ListenOnDo m_listenOn;

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public ConsumerDo() {
      super(null, "consumer");
   }

   public boolean checkConstraint() throws ValidationException {
      if (isEmpty(m_type)) {
         return false;
      }
      
      return true;
   }

   public void destroy() {
      if (m_configuration != null) {
         m_configuration.destroy();
      }

      if (m_listenOn != null) {
         m_listenOn.destroy();
      }
   }

   protected void doValidate(Stack<XmlModel> parents) {
      if (m_configuration != null) {
         m_configuration.validate(parents);
      }

      if (m_listenOn != null) {
         m_listenOn.validate(parents);
      }
   }

   protected ConfigurationDo getConfigurationDo() {
      return m_configuration;
   }
   
   protected ListenOnDo getListenOnDo() {
      return m_listenOn;
   }
   
   public String getType() {
      return m_type;
   }
   
   public boolean isEnabled() {
      return m_enabled;
   }
   
   public long getCheckInterval() {
      return m_checkInterval;
   }
   
   public void loadAttributes(Attributes attrs) {
      setFieldValue(TYPE, attrs);
      setFieldValue(ENABLED, attrs);
      setFieldValue(CHECK_INTERVAL, attrs);
   }

   protected void setConfigurationDo(ConfigurationDo configuration) {
      m_configuration = configuration;
      setFieldUsed(CONFIGURATION, true);
   }
   
   protected void setListenOnDo(ListenOnDo listenOn) {
      m_listenOn = listenOn;
      setFieldUsed(LISTEN_ON, true);
   }
   
   public void setType(String type) {
      m_type = type;
      setFieldUsed(TYPE, true);
   }
   
   public void setEnabled(boolean enabled) {
      m_enabled = enabled;
      setFieldUsed(ENABLED, true);
   }
   
   public void setCheckInterval(long checkInterval) {
      m_checkInterval = checkInterval;
      setFieldUsed(CHECK_INTERVAL, true);
   }
   
}
