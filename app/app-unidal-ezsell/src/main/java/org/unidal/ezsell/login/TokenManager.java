package org.unidal.ezsell.login;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.unidal.ezsell.common.CookieManager;

public class TokenManager {
   private static final String SP = "|";

   private static final String TOKEN = "token";

   private static final long ONE_DAY = 24 * 60 * 60 * 1000L;

   private CookieManager m_cookieManager;

   private HttpServletRequest m_request;

   private HttpServletResponse m_response;

   public TokenManager(CookieManager cookieManager, HttpServletRequest request, HttpServletResponse response) {
      m_cookieManager = cookieManager;
      m_request = request;
      m_response = response;
   }

   public String getToken() {
      return m_cookieManager.getCookie(m_request, TOKEN);
   }

   public String buildToken(int userId) {
      StringBuilder sb = new StringBuilder(256);
      String remoteIp = m_request.getRemoteAddr();

      sb.append(userId).append(SP);
      sb.append(System.currentTimeMillis()).append(SP);
      sb.append(remoteIp).append(SP);
      sb.append(getCheckSum(sb.toString()));

      return sb.toString();
   }

   private int getCheckSum(String str) {
      return str.hashCode();
   }

   public void setToken(String token) {
      m_cookieManager.setCookie(m_response, TOKEN, token, "/");
   }

   public int validateToken(String token) {
      String[] parts = token.split(Pattern.quote(SP));

      if (parts.length == 4) {
         int index = 0;
         int userId = Integer.parseInt(parts[index++]);
         long lastLoginDate = Long.parseLong(parts[index++]);
         String remoteIp = parts[index++];
         int checkSum = Integer.parseInt(parts[index++]);
         String expectedRemoteIp = m_request.getRemoteAddr();
         int expectedCheckSum = getCheckSum(token.substring(0, token.lastIndexOf(SP) + 1));

         if (checkSum == expectedCheckSum) {
            if (remoteIp.equals(expectedRemoteIp)) {
               if (lastLoginDate + ONE_DAY > System.currentTimeMillis()) {
                  return userId;
               }
            }
         }
      }

      return -1;
   }
}
