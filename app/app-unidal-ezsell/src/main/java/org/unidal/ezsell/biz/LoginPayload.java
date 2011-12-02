package org.unidal.ezsell.biz;

import org.unidal.ezsell.EbayPayload;

import com.site.web.mvc.payload.annotation.FieldMeta;

public class LoginPayload extends EbayPayload {
   @FieldMeta("email")
   private String m_email;

   @FieldMeta("password")
   private String m_password;

   @FieldMeta("rtnUrl")
   private String m_rtnUrl;

   @FieldMeta("login")
   private boolean m_login;

   public String getEmail() {
      return m_email;
   }

   public String getPassword() {
      return m_password;
   }

   public String getRtnUrl() {
      return m_rtnUrl;
   }

   public boolean isLogin() {
      return m_login;
   }

   public void setEmail(String username) {
      m_email = username;
   }

   public void setPassword(String password) {
      m_password = password;
   }

   public void setRtnUrl(String rtnUrl) {
      m_rtnUrl = rtnUrl;
   }

   public void setLogin(boolean login) {
      m_login = login;
   }
}
