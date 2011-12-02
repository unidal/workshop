package com.site.bes.server.admin;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.site.web.page.PageContext;

public class AdminCtrl {
   public String handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
      request.setCharacterEncoding("UTF-8");

      PageContext ctx = new PageContext(request, response);
      AdminRequest req = AdminNormalizer.getInstance().normalize(ctx);
      AdminResponse res = new AdminResponse();

      AdminApp.newInstance().handle(ctx, req, res);

      request.setAttribute("CONTEXT", ctx);
      request.setAttribute("REQUEST", req);
      request.setAttribute("RESPONSE", res);
      
      return res.getResponseStatus().getName();
   }
}
