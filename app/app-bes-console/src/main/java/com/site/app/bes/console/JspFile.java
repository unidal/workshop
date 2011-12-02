package com.site.app.bes.console;

public enum JspFile {
   MAIN_PAGE("/jsp/show_layout.jsp"),

   EVENT_STATS("/jsp/event_stats.jsp"),
   
   DAILY_STATS("/jsp/daily_report.jsp"),
   
   ;

   private String m_path;

   private JspFile(String path) {
      m_path = path;
   }

   public String getPath() {
      return m_path;
   }
}
