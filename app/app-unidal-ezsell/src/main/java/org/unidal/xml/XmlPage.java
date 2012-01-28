package org.unidal.xml;

import com.site.web.mvc.Page;

public enum XmlPage implements Page {
   SOURCE("source", "Input XML Source", true),

   MAPPING("mapping", "Namespace to Package Mapping", true),

   DOWNLOAD("download", "Download Java Source", true);

   private String m_name;

   private String m_description;

   private boolean m_realPage;

   private XmlPage(String name, String description, boolean realPage) {
      m_name = name;
      m_description = description;
      m_realPage = realPage;
   }

   public static XmlPage getByName(String name, XmlPage defaultPage) {
      for (XmlPage action : XmlPage.values()) {
         if (action.getName().equals(name)) {
            return action;
         }
      }

      return defaultPage;
   }

   public String getName() {
      return m_name;
   }
   
   public String getPath() {
      return m_name;
   }

   public String getDescription() {
      return m_description;
   }

   public boolean isRealPage() {
      return m_realPage;
   }

   public XmlPage[] getValues() {
      return XmlPage.values();
   }
}
