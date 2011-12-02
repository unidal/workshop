package com.site.kernel.dal.datasource;

import static com.site.kernel.dal.Cardinality.ZERO_TO_ONE;
import static com.site.kernel.dal.ValueType.BOOLEAN;
import static com.site.kernel.dal.ValueType.INT;
import static com.site.kernel.dal.ValueType.STRING;
import static com.site.kernel.dal.ValueType.TIME;
import static com.site.kernel.dal.model.NodeType.ATTRIBUTE;
import static com.site.kernel.dal.model.NodeType.ELEMENT;
import static com.site.kernel.dal.model.NodeType.MODEL;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.ValidationException;
import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;

public class DataSourceDo extends XmlModel {
   public static final XmlModelField NAME = new XmlModelField("name", ATTRIBUTE, STRING);

   public static final XmlModelField TYPE = new XmlModelField("type", ATTRIBUTE, STRING);

   public static final XmlModelField ENABLED = new XmlModelField("enabled", ATTRIBUTE, BOOLEAN, "true");

   public static final XmlModelField MINIMUM_POOL_SIZE = new XmlModelField("minimum-pool-size", ELEMENT, INT, "0");

   public static final XmlModelField MAXIMUM_POOL_SIZE = new XmlModelField("maximum-pool-size", ELEMENT, INT, "3");

   public static final XmlModelField CONNECTION_TIMEOUT = new XmlModelField("connection-timeout", ELEMENT, TIME, "0");

   public static final XmlModelField IDLE_TIMEOUT = new XmlModelField("idle-timeout", ELEMENT, TIME, "0");

   public static final XmlModelField ORPHAN_TIMEOUT = new XmlModelField("orphan-timeout", ELEMENT, TIME, "0");

   public static final XmlModelField STATEMENT_CACHE_SIZE = new XmlModelField("statement-cache-size", ELEMENT, INT, "0");

   public static final XmlModelField CONFIG_PROPERTIES = new XmlModelField("config-properties", MODEL, ZERO_TO_ONE, ConfigPropertiesDo.class);

   private String m_name;

   private String m_type;

   private boolean m_enabled;

   private int m_minimumPoolSize;

   private int m_maximumPoolSize;

   private long m_connectionTimeout;

   private long m_idleTimeout;

   private long m_orphanTimeout;

   private int m_statementCacheSize;

   private ConfigPropertiesDo m_configProperties;

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public DataSourceDo() {
      super(null, "data-source");
   }

   public boolean checkConstraint() throws ValidationException {
      if (isEmpty(m_name)) {
         return false;
      }

      if (isEmpty(m_type)) {
         return false;
      }

      return true;
   }

   public void destroy() {
      if (m_configProperties != null) {
         m_configProperties.destroy();
      }
   }

   protected void doValidate(Stack<XmlModel> parents) {
      if (m_configProperties != null) {
         m_configProperties.validate(parents);
      }
   }

   protected ConfigPropertiesDo getConfigPropertiesDo() {
      return m_configProperties;
   }

   public String getName() {
      return m_name;
   }

   public String getType() {
      return m_type;
   }

   public boolean isEnabled() {
      return m_enabled;
   }

   public int getMinimumPoolSize() {
      return m_minimumPoolSize;
   }

   public int getMaximumPoolSize() {
      return m_maximumPoolSize;
   }

   public long getConnectionTimeout() {
      return m_connectionTimeout;
   }

   public long getIdleTimeout() {
      return m_idleTimeout;
   }

   public long getOrphanTimeout() {
      return m_orphanTimeout;
   }

   public int getStatementCacheSize() {
      return m_statementCacheSize;
   }

   public void loadAttributes(Attributes attrs) {
      setFieldValue(NAME, attrs);
      setFieldValue(TYPE, attrs);
      setFieldValue(ENABLED, attrs);
      setFieldValue(MINIMUM_POOL_SIZE, attrs);
      setFieldValue(MAXIMUM_POOL_SIZE, attrs);
      setFieldValue(CONNECTION_TIMEOUT, attrs);
      setFieldValue(IDLE_TIMEOUT, attrs);
      setFieldValue(ORPHAN_TIMEOUT, attrs);
      setFieldValue(STATEMENT_CACHE_SIZE, attrs);
   }

   protected void setConfigPropertiesDo(ConfigPropertiesDo configProperties) {
      m_configProperties = configProperties;
      setFieldUsed(CONFIG_PROPERTIES, true);
   }

   public void setName(String name) {
      m_name = name;
      setFieldUsed(NAME, true);
   }

   public void setType(String type) {
      m_type = type;
      setFieldUsed(TYPE, true);
   }

   public void setEnabled(boolean enabled) {
      m_enabled = enabled;
      setFieldUsed(ENABLED, true);
   }

   public void setMinimumPoolSize(int minimumPoolSize) {
      m_minimumPoolSize = minimumPoolSize;
      setFieldUsed(MINIMUM_POOL_SIZE, true);
   }

   public void setMaximumPoolSize(int maximumPoolSize) {
      m_maximumPoolSize = maximumPoolSize;
      setFieldUsed(MAXIMUM_POOL_SIZE, true);
   }

   public void setConnectionTimeout(long connectionTimeout) {
      m_connectionTimeout = connectionTimeout;
      setFieldUsed(CONNECTION_TIMEOUT, true);
   }

   public void setIdleTimeout(long idleTimeout) {
      m_idleTimeout = idleTimeout;
      setFieldUsed(IDLE_TIMEOUT, true);
   }

   public void setOrphanTimeout(long orphanTimeout) {
      m_orphanTimeout = orphanTimeout;
      setFieldUsed(ORPHAN_TIMEOUT, true);
   }

   public void setStatementCacheSize(int statementCacheSize) {
      m_statementCacheSize = statementCacheSize;
      setFieldUsed(STATEMENT_CACHE_SIZE, true);
   }

}
