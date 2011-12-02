package com.site.kernel.initialization.config;

import static com.site.kernel.dal.ValueType.BOOLEAN;
import static com.site.kernel.dal.ValueType.STRING;
import static com.site.kernel.dal.model.NodeType.ATTRIBUTE;
import static com.site.kernel.dal.model.NodeType.MODEL;
import static com.site.kernel.dal.Cardinality.ZERO_TO_ONE;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.ValidationException;
import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;

public class ModuleDo extends XmlModel {
   public static final XmlModelField NAME = new XmlModelField("name", ATTRIBUTE, STRING);

   public static final XmlModelField VERBOSE = new XmlModelField("verbose", ATTRIBUTE, BOOLEAN, "true");

   public static final XmlModelField CONFIGURATION = new XmlModelField("configuration", MODEL, ZERO_TO_ONE, ConfigurationDo.class);

   private String m_name;

   private boolean m_verbose;

   private ConfigurationDo m_configuration;

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public ModuleDo() {
      super(null, "module");
   }

   public boolean checkConstraint() throws ValidationException {
      if (isEmpty(m_name)) {
         return false;
      }

      return true;
   }

   public void destroy() {
      if (m_configuration != null) {
         m_configuration.destroy();
      }
   }

   protected void doValidate(Stack<XmlModel> parents) {
      if (m_configuration != null) {
         m_configuration.validate(parents);
      }
   }

   protected ConfigurationDo getConfigurationDo() {
      return m_configuration;
   }

   public String getName() {
      return m_name;
   }

   public boolean isVerbose() {
      return m_verbose;
   }

   public void loadAttributes(Attributes attrs) {
      setFieldValue(NAME, attrs);
      setFieldValue(VERBOSE, attrs);
   }

   protected void setConfigurationDo(ConfigurationDo configuration) {
      m_configuration = configuration;
      setFieldUsed(CONFIGURATION, true);
   }

   public void setName(String name) {
      m_name = name;
      setFieldUsed(NAME, true);
   }

   public void setVerbose(boolean verbose) {
      m_verbose = verbose;
      setFieldUsed(VERBOSE, true);
   }

}
