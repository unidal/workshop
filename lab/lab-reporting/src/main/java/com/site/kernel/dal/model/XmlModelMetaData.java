package com.site.kernel.dal.model;

import com.site.kernel.dal.DataObjectMetaData;
import com.site.kernel.dal.DataObjectNaming;

public class XmlModelMetaData extends DataObjectMetaData {
   private Namespace[] m_namespaces;

   public XmlModelMetaData(DataObjectNaming naming, Class doClass, Namespace[] namespaces) {
      super(naming, doClass);

      m_namespaces = namespaces;
   }

   public Namespace[] getNamespaces() {
      return m_namespaces;
   }

   public void setNamespaces(Namespace[] namespaces) {
      m_namespaces = namespaces;
   }
}
