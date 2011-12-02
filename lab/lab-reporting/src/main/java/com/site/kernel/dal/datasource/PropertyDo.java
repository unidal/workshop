package com.site.kernel.dal.datasource;

import static com.site.kernel.dal.ValueType.STRING;
import static com.site.kernel.dal.model.NodeType.ATTRIBUTE;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.ValidationException;
import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;

public class PropertyDo extends XmlModel {
   public static final XmlModelField NAME = new XmlModelField("name", ATTRIBUTE, STRING);

   public static final XmlModelField VALUE = new XmlModelField("value", ATTRIBUTE, STRING);

   private String m_name;

   private String m_value;

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public PropertyDo() {
      super(null, "property");
   }

   public boolean checkConstraint() throws ValidationException {
      if (isEmpty(m_name)) {
         return false;
      }

      return true;
   }

   public void destroy() {
   }

   protected void doValidate(Stack parents) {
   }

   public String getName() {
      return m_name;
   }

   public String getValue() {
      return m_value;
   }

   public void loadAttributes(Attributes attrs) {
      setFieldValue(NAME, attrs);
      setFieldValue(VALUE, attrs);
   }

   public void setName(String name) {
      m_name = name;
      setFieldUsed(NAME, true);
   }

   public void setValue(String value) {
      m_value = value;
      setFieldUsed(VALUE, true);
   }

}
