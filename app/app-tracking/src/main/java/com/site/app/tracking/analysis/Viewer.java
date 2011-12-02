package com.site.app.tracking.analysis;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Viewer {
   public void view(HttpServletRequest req, HttpServletResponse res, Payload payload, Model model)
         throws ServletException, IOException;
}
