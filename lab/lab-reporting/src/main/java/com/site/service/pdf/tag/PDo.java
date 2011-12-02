package com.site.service.pdf.tag;
import static com.site.kernel.dal.ValueType.STRING;
import static com.site.kernel.dal.model.NodeType.ATTRIBUTE;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;
import com.site.kernel.dal.model.ValidationException;

public class PDo extends XmlModel {
   public static final XmlModelField ALIGN = new XmlModelField("align", ATTRIBUTE, STRING);

   private String m_align;

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public PDo() {
      super(null, "p");
   }

   public boolean checkConstraint() throws ValidationException {
      return true;
   }

   public void destroy() {
   }

   protected void doValidate(Stack<XmlModel> parents) {
   }

   public String getAlign() {
      return m_align;
   }
   
   public void loadAttributes(Attributes attrs) {
      setFieldValue(ALIGN, attrs);
   }

   public void setAlign(String align) {
      m_align = align;
      setFieldUsed(ALIGN, true);
   }
   
}
