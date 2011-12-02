package com.site.service.pdf.tag;
import static com.site.kernel.dal.ValueType.INT;
import static com.site.kernel.dal.model.NodeType.ATTRIBUTE;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;
import com.site.kernel.dal.model.ValidationException;

public class HeaderDo extends XmlModel {
   public static final XmlModelField BORDER = new XmlModelField("border", ATTRIBUTE, INT, "2");

   private int m_border;

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public HeaderDo() {
      super(null, "header");
   }

   public boolean checkConstraint() throws ValidationException {
      return true;
   }

   public void destroy() {
   }

   protected void doValidate(Stack<XmlModel> parents) {
   }

   public int getBorder() {
      return m_border;
   }
   
   public void loadAttributes(Attributes attrs) {
      setFieldValue(BORDER, attrs);
   }

   public void setBorder(int border) {
      m_border = border;
      setFieldUsed(BORDER, true);
   }
   
}
