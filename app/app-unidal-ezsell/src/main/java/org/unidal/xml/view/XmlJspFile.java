package org.unidal.xml.view;

public enum XmlJspFile {
   SOURCE("/jsp/xml/source.jsp"),

   MAPPING("/jsp/xml/mapping.jsp"),

   DOWNLOAD("/jsp/xml/download.jsp"),

   ;

   private String m_path;

   private XmlJspFile(String path) {
      m_path = path;
   }

   public String getPath() {
      return m_path;
   }
}
