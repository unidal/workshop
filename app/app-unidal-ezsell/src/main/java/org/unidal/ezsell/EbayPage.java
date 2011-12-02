package org.unidal.ezsell;

import com.site.web.mvc.Page;

public enum EbayPage implements Page {
   REGISTER(EbayConstant.REGISTER, "Register", false),
   
   LOGIN(EbayConstant.LOGIN, "Login", false),
   
   HOME(EbayConstant.HOME, "Home", true),

   SELLER_TRANSACTIONS(EbayConstant.SELLER_TRANSACTIONS, "Transactions", true),

   SELLER_TRANSACTION(EbayConstant.SELLER_TRANSACTION, "Transaction", false),

   CUSTOMS_DECLARATION(EbayConstant.CUSTOMS_DECLARATION, "Customs Declaration", false),
   
   MESSAGES(EbayConstant.MESSAGES, "Messages", true),
   
   DISPUTES(EbayConstant.DISPUTES, "Disputes", true),
   
   DATA_LOADING(EbayConstant.DATA_LOADING, "Data Loading", true),
   
   PROFILE(EbayConstant.PROFILE, "Profile", true),
   
   NOTIFICATION(EbayConstant.NOTIFICATION, "Notification", false), // callback
   
   ;

   private String m_name;

   private String m_description;

   private boolean m_realPage;

   private EbayPage(String name, String description, boolean realPage) {
      m_name = name;
      m_description = description;
      m_realPage = realPage;
   }

   public static EbayPage getByName(String name, EbayPage defaultPage) {
      for (EbayPage action : EbayPage.values()) {
         if (action.getName().equals(name)) {
            return action;
         }
      }

      return defaultPage;
   }

   public String getName() {
      return m_name;
   }

   public String getDescription() {
      return m_description;
   }

   public boolean isRealPage() {
      return m_realPage;
   }

   public EbayPage[] getValues() {
      return EbayPage.values();
   }
}
