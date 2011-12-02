package com.site.game.sanguo.thread.handler.building;

public enum BuildType {
   LC(0, "����"),

   CK(1, "�ֿ�"),

   NZT(2, "������"),

   JC(3, "У��"),

   BQS(4, "����˾"),

   BS(5, "����"),

   TYJ(6, "ұ����"),

   ZXG(8, "���͹�"),

   ZJZ(9, "�о���"),

   MC(10, "��"),

   HFY(11, "����Ӫ"),

   GJF(13, "������"),

   AC(15, "����"),

   JYT(16, "������"),

   TMS(17, "��ľ˾"),

   JS(18, "����"),

   GD(19, "��ۡ"),

   BY(20, "��Ժ"),

   GWF(22, "���跻"),

   XG(23, "ѧ��"),

   JYF(24, "���ظ�"),

   CSF(25, "��ʷ��"),

   CHY(26, "���Ӫ"),

   JT(27, "����"), // unverified

   CQ(29, "��ǽ"), // unverified

   YM(30, "����"),

   LMQZ(31, "��������");

   private String m_name;
   private int m_typeId;

   private BuildType(int typeId, String name) {
      m_name = name;
      m_typeId = typeId;
   }

   public String getName() {
      return m_name;
   }

   public int getTypeId() {
      return m_typeId;
   }

   public static BuildType getByName(String name) {
      for (BuildType e : BuildType.values()) {
         if (e.getName().equals(name)) {
            return e;
         }
      }

      throw new RuntimeException("No BuildType defined with name: " + name);
   }
}