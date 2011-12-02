package org.unidal.weather.web.view;

public enum JspFile {
   REPORT("/jsp/weather/report.jsp"),

   ;

   private String m_path;

   private JspFile(String path) {
      m_path = path;
   }

   public String getPath() {
      return m_path;
   }
}
