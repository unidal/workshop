package com.site.kernel.initialization.config;

import static com.site.kernel.dal.model.NodeType.MODEL;
import static com.site.kernel.dal.Cardinality.ONE_TO_MANY;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.ValidationException;
import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;

public class ModulesDo extends XmlModel {
   public static final XmlModelField MODULE = new XmlModelField("module", MODEL, ONE_TO_MANY, ModuleDo.class);

   private List m_modules = new ArrayList(3);

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public ModulesDo() {
      super(null, "modules");
   }

   protected void addModuleDo(ModuleDo module) {
      super.addChild(MODULE, module);
   }

   public boolean checkConstraint() throws ValidationException {
      return true;
   }

   public void destroy() {
      for (int i = 0; i != m_modules.size(); i++) {
         ModuleDo module = (ModuleDo) m_modules.get(i);

         module.destroy();
      }
   }

   protected void doValidate(Stack<XmlModel> parents) {
      if (m_modules.size() == 0) {
         throw new ValidationException("At least one module should be defined under " + this);
      }

      for (int i = 0; i != m_modules.size(); i++) {
         ModuleDo module = (ModuleDo) m_modules.get(i);

         module.validate(parents);
      }
   }

   protected List getModuleDos() {
      return m_modules;
   }

   public void loadAttributes(Attributes attrs) {
   }

}
