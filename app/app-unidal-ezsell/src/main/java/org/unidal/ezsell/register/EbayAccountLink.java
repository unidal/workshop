package org.unidal.ezsell.register;

import java.text.MessageFormat;

import com.site.lookup.annotation.Inject;

public class EbayAccountLink {
   @Inject
   private String m_appId;

   @Inject
   private String m_accountLinkUrlPattern;

   @Inject
   private int m_authTokenMinLength;

   private MessageFormat m_format;

   public synchronized String getAccountLinkUrl(String account) {
      String url = m_format.format(new Object[] { account });

      return url;
   }

   public String getAppId() {
      return m_appId;
   }

   public int getAuthTokenMinLength() {
      return m_authTokenMinLength;
   }

   public void setAccountLinkUrlPattern(String accountLinkUrlPattern) {
      m_accountLinkUrlPattern = accountLinkUrlPattern;
      m_format = new MessageFormat(m_accountLinkUrlPattern);
   }

   public void setAppId(String appId) {
      m_appId = appId;
   }

   public void setAuthTokenMinLength(int authTokenMinLength) {
      m_authTokenMinLength = authTokenMinLength;
   }
}
