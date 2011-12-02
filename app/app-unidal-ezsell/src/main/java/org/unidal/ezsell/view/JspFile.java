package org.unidal.ezsell.view;

public enum JspFile {
   REGISTER_USER_FORM("/jsp/ebay/register/user_form.jsp"),
   
   REGISTER_EMAIL_SENT("/jsp/ebay/register/email_sent.jsp"),
   
   REGISTER_SELLER_FORM("/jsp/ebay/register/seller_form.jsp"),
   
   REGISTER_COMPLETED("/jsp/ebay/register/completed.jsp"),
   
   LOGIN("/jsp/ebay/login.jsp"),
   
   HOME("/jsp/ebay/home.jsp"),
   
   SELLER_TRANSACTIONS("/jsp/ebay/seller_transactions.jsp"),
   
   SELLER_TRANSACTION("/jsp/ebay/seller_transaction.jsp"),
   
   CUSTOMS_DECLARATION("/jsp/ebay/customs_declaration.jsp"),
   
   MESSAGES("/jsp/ebay/messages.jsp"),
   
   DISPUTES("/jsp/ebay/disputes.jsp"),
   
   DATA_LOADING("/jsp/ebay/data_loading.jsp"),
   
   PROFILE("/jsp/ebay/profile.jsp"),

   ;

   private String m_path;

   private JspFile(String path) {
      m_path = path;
   }

   public String getPath() {
      return m_path;
   }
}
