package com.site.kernel.initialization.config;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.ValidationException;
import com.site.kernel.dal.model.XmlModel;

public class ConfigurationDo extends XmlModel {

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public ConfigurationDo() {
      super(null, "configuration");
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
