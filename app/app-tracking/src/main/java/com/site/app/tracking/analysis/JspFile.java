package com.site.app.tracking.analysis;

public enum JspFile {
   MAIN_PAGE("/jsp/show_layout.jsp"),

   SEARCH_TOP_N("/jsp/search_top_n.jsp"),

   PAGE_DETAIL("/jsp/page_detail.jsp"),
   
   VISIT_STATS("/jsp/visit_stats.jsp"),
   
   IP_STATS("/jsp/ip_stats.jsp"),
   
   PURGE_TABLE("/jsp/purge_table.jsp"),
   
   ;

   private String m_path;

   private JspFile(String path) {
      m_path = path;
   }

   public String getPath() {
      return m_path;
   }
}
