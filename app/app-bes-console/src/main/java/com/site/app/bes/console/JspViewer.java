package com.site.app.bes.console;

import static com.site.app.bes.console.JspFile.DAILY_STATS;
import static com.site.app.bes.console.JspFile.EVENT_STATS;
import static com.site.app.bes.console.JspFile.MAIN_PAGE;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspViewer implements Viewer {
   public void view(HttpServletRequest req, HttpServletResponse res, Payload payload, Model model)
         throws ServletException, IOException {
      Action action = payload.getAction();

      req.setAttribute("payload", payload);
      req.setAttribute("model", model);

      switch (action) {
      case SIGN_IN:
         if (payload.getUser() != null) {
            Cookie cookie = new Cookie("user", payload.getUser());

            cookie.setPath("/");
            res.addCookie(cookie);
         }

         req.getRequestDispatcher(MAIN_PAGE.getPath()).forward(req, res);
         break;
      case MAIN_PAGE:
         req.getRequestDispatcher(MAIN_PAGE.getPath()).forward(req, res);
         break;
      case EVENT_STATS:
         req.getRequestDispatcher(EVENT_STATS.getPath()).forward(req, res);
         break;
      case DAILY_REPORT:
         req.getRequestDispatcher(DAILY_STATS.getPath()).forward(req, res);
         break;
      }
   }
}
