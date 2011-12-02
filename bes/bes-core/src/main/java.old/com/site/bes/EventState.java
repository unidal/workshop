package com.site.bes;

public enum EventState {
   PROCESSING(0, "processing"),

   COMPLETED(1, "completed"),

   SKIPPED(2, "skipped"),

   ABANDON(8, "abandon"),

   PAYLOAD_MALFORMED(9, "payload malformed"),

   RESCHEDULE(-1, "reschedule"),

   RETRY(-2, "retry"),

   ;

   private int m_id;

   private String m_name;

   private EventState(int id, String name) {
      m_id = id;
      m_name = name;
   }

   public int getId() {
      return m_id;
   }

   public String getName() {
      return m_name;
   }

   public static final EventState getById(int id) {
      for (EventState e : EventState.values()) {
         if (e.getId() == id) {
            return e;
         }
      }

      throw new IllegalArgumentException("No enum defined in EventState for id: " + id);
   }
}
