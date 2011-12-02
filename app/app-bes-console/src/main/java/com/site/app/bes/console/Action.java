package com.site.app.bes.console;

public enum Action {
   SIGN_IN("signin", "Signin", false),

   MAIN_PAGE("main", "Main", true),

   EVENT_STATS("stats", "Statistics", true),
   
   DAILY_REPORT("report", "Daily Report", true),

   ;

   private String m_name;

   private String m_description;

   private boolean m_realPage;

   private Action(String name, String description, boolean realPage) {
      m_name = name;
      m_description = description;
      m_realPage = realPage;
   }

   public static Action get(String name, Action defaultValue) {
      if (name != null) {
         for (Action e : Action.values()) {
            if (name.equals(e.getName())) {
               return e;
            }
         }
      }

      return defaultValue;
   }

   public String getDescription() {
      return m_description;
   }

   public String getName() {
      return m_name;
   }

   public boolean isRealPage() {
      return m_realPage;
   }
   
   public Action[] getValues() {
	   return Action.values();
   }
}
