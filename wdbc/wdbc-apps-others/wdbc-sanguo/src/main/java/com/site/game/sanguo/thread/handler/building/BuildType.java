package com.site.game.sanguo.thread.handler.building;

public enum BuildType {
   LC(0, "粮仓"),

   CK(1, "仓库"),

   NZT(2, "内政厅"),

   JC(3, "校场"),

   BQS(4, "兵器司"),

   BS(5, "兵舍"),

   TYJ(6, "冶铁监"),

   ZXG(8, "招贤馆"),

   ZJZ(9, "中军帐"),

   MC(10, "马场"),

   HFY(11, "虎贲营"),

   GJF(13, "工匠坊"),

   AC(15, "暗仓"),

   JYT(16, "聚义厅"),

   TMS(17, "土木司"),

   JS(18, "集市"),

   GD(19, "官邸"),

   BY(20, "别院"),

   GWF(22, "歌舞坊"),

   XG(23, "学馆"),

   JYF(24, "郡守府"),

   CSF(25, "刺史府"),

   CHY(26, "斥候营"),

   JT(27, "箭塔"), // unverified

   CQ(29, "城墙"), // unverified

   YM(30, "衙门"),

   LMQZ(31, "联盟旗帜");

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