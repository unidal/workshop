package com.site.game.sanguo.thread;

public enum HandlerType {
   QUESTION(0, "question", "�Զ��ش����⣬ÿСʱһ��"),

   BUILDING(1, "building", "�Զ��������������"),

   RESOURCE(2, "resource", "�Զ�������Դ��"),

   FIGHT(3, "fight", "ʰ���ߣ�ר������"),

   GENERAL(4, "general", "�Զ�ִ����������"),

   COURT(5, "court", "�Զ�ִ�г�͢����"),

   GIFT(6, "gift", "��������Զ�����"),

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
