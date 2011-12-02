package com.site.game.sanguo.thread;

public enum HandlerType {
   QUESTION(0, "question", "自动回答问题，每小时一次"),

   BUILDING(1, "building", "自动建造和升级建筑"),

   RESOURCE(2, "resource", "自动升级资源田"),

   FIGHT(3, "fight", "拾荒者，专打死羊"),

   GENERAL(4, "general", "自动执行势力任务"),

   COURT(5, "court", "自动执行朝廷献纳"),

   GIFT(6, "gift", "新手礼包自动申领"),

   ;

   private int m_index;

   private String m_name;

   private String m_description;

   private HandlerType(int index, String name, String description) {
      m_index = index;
      m_name = name;
      m_description = description;
   }

   public String getDescription() {
      return m_description;
   }

   public int getIndex() {
      return m_index;
   }

   public String getName() {
      return m_name;
   }
}
