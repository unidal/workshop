package com.site.app.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Handler {
   public void handle(HttpServletRequest req, HttpServletResponse res);
}
