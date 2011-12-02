package org.unidal.ezsell.view;

public enum EmailId {
   REGISTER_CONFIRM("register_confirm", false, "/jsp/ebay/email/register_confirm.jsp"),

   ;

   private String m_id;

   private boolean m_textOnly;

   private String m_path;

   private EmailId(String id, boolean isTextOnly, String path) {
      m_id = id;
      m_textOnly = isTextOnly;
      m_path = path;
   }

   public String getId() {
      return m_id;
   }

   public String getPath() {
      return m_path;
   }

   public boolean isTextOnly() {
      return m_textOnly;
   }
}
