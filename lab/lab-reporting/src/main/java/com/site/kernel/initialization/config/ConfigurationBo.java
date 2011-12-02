package com.site.kernel.initialization.config;

import static com.site.kernel.dal.ValueType.LIST;
import static com.site.kernel.dal.model.NodeType.ELEMENT;

import java.util.ArrayList;
import java.util.List;

import com.site.kernel.dal.model.XmlModelField;
import com.site.kernel.dal.model.common.Formatter;
import com.site.kernel.dal.model.helpers.ConfigurationFormatter;
import com.site.kernel.initialization.ModuleContext;

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

   public void setParameters(ModuleContext ctx) {
      int len = (m_names == null ? 0 : m_names.size());

      for (int i = 0; i < len; i++) {
         String name = (String) m_names.get(i);
         List value = (List) m_values.get(i);

         ctx.setParameter(name, value);
      }
   }

   public void setValues(List values) {
      m_values = values;
   }
}
