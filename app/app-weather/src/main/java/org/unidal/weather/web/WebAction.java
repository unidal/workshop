package org.unidal.weather.web;

import com.site.web.mvc.Action;

public enum WebAction implements Action {
   REPORT(WebConstant.REPORT, "Report", true),

   ;

   private String m_name;

   private String m_description;

   private boolean m_realPage;

   private WebAction(String name, String description, boolean realPage) {
      m_name = name;
      m_description = description;
      m_realPage = realPage;
   }

   public static WebAction getByName(String name, WebAction defaultAction) {
      for (WebAction action : WebAction.values()) {
         if (action.getName().equals(name)) {
            return action;
         }
      }

      return defaultAction;
   }

   public String getName() {
      return m_name;
   }

   public String getDescription() {
      return m_description;
   }

   public boolean isRealPage() {
      return m_realPage;
   }

   public WebAction[] getValues() {
      return WebAction.values();
   }
}
