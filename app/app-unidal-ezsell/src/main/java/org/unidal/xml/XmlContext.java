package org.unidal.xml;

import java.util.ArrayList;
import java.util.List;

import com.site.web.mvc.Action;
import com.site.web.mvc.ActionContext;
import com.site.web.mvc.ActionPayload;
import com.site.web.mvc.Page;

public class XmlContext<T extends ActionPayload<Page, Action>> extends ActionContext<T> {
   private XmlModel m_model;

   private List<String> m_namespaces = new ArrayList<String>();

   public XmlModel getModel() {
      return m_model;
   }

   public void setModel(XmlModel model) {
      m_model = model;
   }

   public void addNamespace(String namespace) {
      m_namespaces.add(namespace);
   }

   public List<String> getNamespaces() {
      return m_namespaces;
   }
}
