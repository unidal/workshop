package com.site.app.tracking.counter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TestPage {
   public void showTestPage(HttpServletRequest req, HttpServletResponse res) throws IOException;
}
