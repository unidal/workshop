package com.site.kernel.dal.model.helpers;

import java.util.Stack;

import org.xml.sax.Locator;

import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.common.Formatter;

public class FormatterContext {
   private Object m_formatResult;

   private Stack<Formatter> m_formatters;

   private Locator m_locator;

   private Stack<XmlModel> m_models;

   private XmlModel m_rootModel;

   public FormatterContext() {
      m_formatters = new Stack<Formatter>();
      m_models = new Stack<XmlModel>();
   }

   public Object getFormatResult() {
      return m_formatResult;
   }

   public XmlModel getLeafModel() {
      if (m_models.isEmpty()) {
         return null;
      } else {
         return m_models.peek();
      }
   }

   public Formatter getLeafFormatter() {
      return m_formatters.peek();
   }

   public Locator getLocator() {
      return m_locator;
   }

   public XmlModel getRootModel() {
      return m_rootModel;
   }

   public void setCurrentModel(XmlModel model) {
      m_models.push(model);
   }

   public void setCurrentFormatter(Formatter formatter) {
      m_formatters.push(formatter);
   }

   /**
    * It's used by model formatter
    */
   public void notifyModelCompleted() {
      m_formatters.pop();
      m_models.pop();
   }

   /**
    * It's used by simple other formatter
    */
   public void setFormatResult(Object formatResult) {
      m_formatResult = formatResult;

      m_formatters.pop();
   }

   public void setLocator(Locator locator) {
      m_locator = locator;
   }

   public void setRootModel(XmlModel rootModel) {
      m_rootModel = rootModel;
   }

}
