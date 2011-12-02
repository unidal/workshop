package com.site.game.sanguo.thread.handler.trader;

import com.site.game.sanguo.model.Farm;

public class TraderTask {
   private Farm m_fromFarm;

   private Farm m_toFarm;

   private int m_lumber;

   private int m_clay;

   private int m_iron;

   private int m_crop;

   public int getClay() {
      return m_clay;
   }

   public int getCrop() {
      return m_crop;
   }

   public Farm getFromFarm() {
      return m_fromFarm;
   }

   public int getIron() {
      return m_iron;
   }

   public int getLumber() {
      return m_lumber;
   }

   public Farm getToFarm() {
      return m_toFarm;
   }

   public void setClay(int clay) {
      m_clay = clay;
   }

   public void setCrop(int crop) {
      m_crop = crop;
   }

   public void setFromFarm(Farm fromFarm) {
      m_fromFarm = fromFarm;
   }

   public void setIron(int iron) {
      m_iron = iron;
   }

   public void setLumber(int lumber) {
      m_lumber = lumber;
   }

   public void setToFarm(Farm toFarm) {
      m_toFarm = toFarm;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();

      sb.append("TraderTask[");
      sb.append(m_fromFarm.getVillageName()).append("=>").append(m_toFarm.getVillageName());
      sb.append(", (").append(m_lumber);
      sb.append(",").append(m_clay);
      sb.append(",").append(m_iron);
      sb.append(",").append(m_crop);
      sb.append(")]");

      return sb.toString();
   }
}
