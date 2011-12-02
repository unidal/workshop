package com.site.app.tracking.analysis;

import static com.site.app.tracking.analysis.JspFile.MAIN_PAGE;
import static com.site.app.tracking.analysis.JspFile.PAGE_DETAIL;
import static com.site.app.tracking.analysis.JspFile.SEARCH_TOP_N;
import static com.site.app.tracking.analysis.JspFile.VISIT_STATS;
import static com.site.app.tracking.analysis.JspFile.IP_STATS;
import static com.site.app.tracking.analysis.JspFile.PURGE_TABLE;

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
      case SEARCH_TOP_N:
         req.getRequestDispatcher(SEARCH_TOP_N.getPath()).forward(req, res);
         break;
      case PAGE_DETAIL:
         req.getRequestDispatcher(PAGE_DETAIL.getPath()).forward(req, res);
         break;
      case VISIT_STATS:
         req.getRequestDispatcher(VISIT_STATS.getPath()).forward(req, res);
         break;
      case IP_STATS:
         req.getRequestDispatcher(IP_STATS.getPath()).forward(req, res);
         break;
      case PURGE_TABLE:
         req.getRequestDispatcher(PURGE_TABLE.getPath()).forward(req, res);
         break;
      }
   }
}
