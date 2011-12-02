package com.site.service.pdf.tag;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.ValidationException;

public class BrDo extends XmlModel {

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public BrDo() {
      super(null, "br");
   }

   public boolean checkConstraint() throws ValidationException {
      return true;
   }

   public void destroy() {
   }

   protected void doValidate(Stack<XmlModel> parents) {
   }

   public void loadAttributes(Attributes attrs) {
   }

}
