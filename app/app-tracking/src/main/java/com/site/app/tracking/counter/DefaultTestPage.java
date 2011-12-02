package com.site.app.tracking.counter;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultTestPage implements TestPage {
   private Configuration m_configuration;

   private String buildLink(String text, String link) {
      return "<a href=\"" + m_configuration.getContextPath() + m_configuration.getServletPath() + link + "\">" + text
            + "</a>";
   }

   private void renderCategoryPage(HttpServletRequest req, StringBuilder sb) {
      sb.append("<h2>Category Page</h2>");
      renderTenDetailLinks(sb);
   }

   private void renderDetailPage(HttpServletRequest req, StringBuilder sb) {
      String onTop = req.getParameter("t");

      sb.append("<h2>Detail Page (Referer: <script>document.write(document.referrer);</script>)</h2>\r\n");
      sb.append("<script type=\"text/javascript\">");
      sb.append("var t=").append(onTop == null ? "0" : onTop).append(";");
      sb.append("document.write(\"<img src='");
      sb.append(m_configuration.getContextPath());
      sb.append(m_configuration.getServletPath());
      sb.append("?r=\"+escape(document.referrer)+\"&t=\"+t+\"'>\");");
      sb.append("</script>");
   }

   private void renderHomePage(HttpServletRequest req, StringBuilder sb) {
      sb.append("<h2>Home Page</h2>");
      renderTenDetailLinks(sb);
   }

   private void renderLandPage(HttpServletRequest req, StringBuilder sb) {
      int category = (int) (Math.random() * 900) + 100;

      sb.append("<h2>Land Page</h2>");
      sb.append(buildLink("Home page", "/?test=1")).append("<br>");
      sb.append(buildLink("Search page", "/search/?test=1&cat=" + category)).append("<br>");
      sb.append(buildLink("Category page", "/59/110100-" + category + ".html?test=1")).append("<br>");
      sb.append(buildLink("Unknown page", "/this/is/unknown/page?test=1")).append("<br>");
   }

   private void renderSearchPage(HttpServletRequest req, StringBuilder sb) {
      sb.append("<h2>Search Page</h2>");
      renderTenDetailLinks(sb);
   }

   private void renderTenDetailLinks(StringBuilder sb) {
      long base = (int) (Math.random() * 900000L) + 100000L;

      for (long id = base; id < base + 10; id++) {
         sb.append("ID: ").append(buildLink(String.valueOf(id), "/detail/" + id + ".html?test=1")).append("<br>");
      }
   }

   private void renderUnknownPage(HttpServletRequest req, StringBuilder sb) {
      sb.append("<h2>Unknown Page</h2>");
      renderTenDetailLinks(sb);
   }

   public void showTestPage(HttpServletRequest req, HttpServletResponse res) throws IOException {
      ServletOutputStream sos = res.getOutputStream();
      StringBuilder sb = new StringBuilder(2048);
      String pathInfo = req.getPathInfo();

      if (pathInfo == null) {
         renderLandPage(req, sb);
      } else if (pathInfo.equals("/")) {
         renderHomePage(req, sb);
      } else if (pathInfo.equals("/search/")) {
         renderSearchPage(req, sb);
      } else if (pathInfo.startsWith("/59/")) {
         renderCategoryPage(req, sb);
      } else if (pathInfo.startsWith("/this/is/unknown/page")) {
         renderUnknownPage(req, sb);
      } else if (pathInfo.startsWith("/detail/")) {
         renderDetailPage(req, sb);
      } else {
         renderLandPage(req, sb);
      }

      sos.write(sb.toString().getBytes());
      sos.close();
   }
}
