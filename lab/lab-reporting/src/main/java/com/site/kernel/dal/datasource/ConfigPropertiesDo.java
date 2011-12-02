package com.site.kernel.dal.datasource;

import static com.site.kernel.dal.model.NodeType.MODEL;
import static com.site.kernel.dal.Cardinality.ZERO_TO_MANY;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.ValidationException;
import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;

public class ConfigPropertiesDo extends XmlModel {
   public static final XmlModelField PROPERTY = new XmlModelField("property", MODEL, ZERO_TO_MANY, PropertyDo.class);

   private List m_propertys = new ArrayList(3);

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public ConfigPropertiesDo() {
      super(null, "config-properties");
   }

   protected void addPropertyDo(PropertyDo property) {
      super.addChild(PROPERTY, property);
   }

   public boolean checkConstraint() throws ValidationException {
      return true;
   }

   public void destroy() {
      for (int i = 0; i != m_propertys.size(); i++) {
         PropertyDo property = (PropertyDo) m_propertys.get(i);

         property.destroy();
      }
   }

   protected void doValidate(Stack<XmlModel> parents) {
      for (int i = 0; i != m_propertys.size(); i++) {
         PropertyDo property = (PropertyDo) m_propertys.get(i);

         property.validate(parents);
      }
   }

   protected List getPropertyDos() {
      return m_propertys;
   }

   public void loadAttributes(Attributes attrs) {
   }

}
