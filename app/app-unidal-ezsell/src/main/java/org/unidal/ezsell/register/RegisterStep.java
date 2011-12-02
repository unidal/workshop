package org.unidal.ezsell.register;

public enum RegisterStep {
   USER_FORM(0, "Basic Information"),

   EMAIL_SENT(1, "Check Your Email"),

   SELLER_FORM(2, "Link to eBay Account"),

   COMPLETED(3, "Complete"),

   ;

   private int m_id;

   private String m_description;

   private RegisterStep(int id, String description) {
      m_id = id;
      m_description = description;
   }

   public int getId() {
      return m_id;
   }

   public String getDescription() {
      return m_description;
   }

   public RegisterStep[] getValues() {
      return RegisterStep.values();
   }

   public static RegisterStep getById(int id, RegisterStep defaultStep) {
      for (RegisterStep step : RegisterStep.values()) {
         if (step.getId() == id) {
            return step;
         }
      }

      return defaultStep;
   }
}
