package com.site.bes.engine.config;

import static com.site.kernel.dal.ValueType.LIST;
import static com.site.kernel.dal.model.NodeType.ELEMENT;

import java.util.ArrayList;
import java.util.List;

import com.site.kernel.dal.model.XmlModelField;
import com.site.kernel.dal.model.common.Formatter;
import com.site.kernel.dal.model.helpers.ConfigurationFormatter;

public class ConfigurationBo extends ConfigurationDo {
   public static final XmlModelField NAMES = new XmlModelField("names", ELEMENT, LIST);

   public static final XmlModelField VALUES = new XmlModelField("values", ELEMENT, LIST);

   static {
      init();
   }

   private List m_names = new ArrayList();

   private List m_values = new ArrayList();

   public Formatter getFormatter() {
      return new ConfigurationFormatter(this, "names", "values");
   }

   public List getNames() {
      return m_names;
   }

   public List getValues() {
      return m_values;
   }

   public void setNames(List names) {
      m_names = names;
   }

   public void setValues(List values) {
      m_values = values;
   }
}
