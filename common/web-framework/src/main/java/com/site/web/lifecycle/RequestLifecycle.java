package com.site.web.lifecycle;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestLifecycle {
   public void handle(final HttpServletRequest request, final HttpServletResponse response) throws IOException;
}
