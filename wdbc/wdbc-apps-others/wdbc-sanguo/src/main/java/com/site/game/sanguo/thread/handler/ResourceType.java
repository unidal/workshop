package com.site.game.sanguo.thread.handler;

public enum ResourceType {
   LUMBER(0, "lumber"),

   CLAY(1, "clay"),

   IRON(2, "iron"),

   CROP(3, "crop");

   private int m_index;
   private String m_name;

   private ResourceType(int index, String name) {
      m_index = index;
      m_name = name;
   }

   public int getIndex() {
      return m_index;
   }

   public String getName() {
      return m_name;
   }
}