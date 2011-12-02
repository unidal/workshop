package com.site.bes.engine.config;
import static com.site.kernel.dal.ValueType.STRING;
import static com.site.kernel.dal.model.NodeType.ATTRIBUTE;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;
import com.site.kernel.dal.model.ValidationException;

public class EventDo extends XmlModel {
   public static final XmlModelField TYPE = new XmlModelField("type", ATTRIBUTE, STRING);

   private String m_type;

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public EventDo() {
      super(null, "event");
   }

   public boolean checkConstraint() throws ValidationException {
      if (isEmpty(m_type)) {
         return false;
      }
      
      return true;
   }

   public void destroy() {
   }

   protected void doValidate(Stack<XmlModel> parents) {
   }

   public String getType() {
      return m_type;
   }
   
   public void loadAttributes(Attributes attrs) {
      setFieldValue(TYPE, attrs);
   }

   public void setType(String type) {
      m_type = type;
      setFieldUsed(TYPE, true);
   }
   
}
