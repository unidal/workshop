package com.site.app.tracking.counter;

public class Payload {
   private String m_pageUrl;

   private String m_fromPage;

   private int m_category1;

   private int m_category2;

   private boolean m_onTop;

   private String m_clientIp;

   public int getCategory1() {
      return m_category1;
   }

   public int getCategory2() {
      return m_category2;
   }

   public String getClientIp() {
      return m_clientIp;
   }

   public String getFromPage() {
      return m_fromPage;
   }

   public String getPageUrl() {
      return m_pageUrl;
   }

   public boolean isOnTop() {
      return m_onTop;
   }

   public void setCategory1(int category1) {
      m_category1 = category1;
   }

   public void setCategory2(int category2) {
      m_category2 = category2;
   }

   public void setClientIp(String clientIp) {
      m_clientIp = clientIp;
   }

   public void setFromPage(String fromPage) {
      m_fromPage = fromPage;
   }

   public void setOnTop(boolean onTop) {
      m_onTop = onTop;
   }

   public void setPageUrl(String pageUrl) {
      m_pageUrl = pageUrl;
   }

   @Override
   public String toString() {
      return "Payload[pageUrl=" + m_pageUrl + ", fromPage=" + m_fromPage + ", category1=" + m_category1
            + ", category2=" + m_category2 + ", onTop=" + m_onTop + "]";
   }
}
