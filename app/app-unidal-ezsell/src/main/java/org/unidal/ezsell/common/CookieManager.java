package org.unidal.ezsell.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieManager {
   public String getCookie(HttpServletRequest request, String name) {
      Cookie[] cookies = request.getCookies();

      if (cookies != null) {
         for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
               return cookie.getValue();
            }
         }
      }

      return null;
   }

   public void setCookie(HttpServletResponse response, String name, String value) {
      setCookie(response, name, value, null, null, 0, false, 0);
   }
   
   public void setCookie(HttpServletResponse response, String name, String value, String path) {
      setCookie(response, name, value, path, null, 0, false, 0);
   }

   public void setCookie(HttpServletResponse response, String name, String value, String path, String domain, int maxAge,
         boolean isSecure, int version) {
      Cookie cookie = new Cookie(name, value);

      if (domain != null) {
         cookie.setDomain(domain);
      }

      if (path != null) {
         cookie.setPath(path);
      }

      if (maxAge > 0) {
         cookie.setMaxAge(maxAge);
      }

      cookie.setSecure(isSecure);

      if (version > 0) {
         cookie.setVersion(version);
      }

      response.addCookie(cookie);
   }
}
