package com.site.game.sanguo.thread.handler.building;

public class Building {
   private int m_id;

   private String m_type;

   private int m_typeId;

   private int m_level;

   public int getId() {
      return m_id;
   }

   public int getTypeId() {
      return m_typeId;
   }

   public void setTypeId(int typeId) {
      m_typeId = typeId;
   }

   public String getType() {
      return m_type;
   }

   public int getLevel() {
      return m_level;
   }

   public void setId(int id) {
      m_id = id;
   }

   public void setType(String type) {
      m_type = type;
   }

   public void setLevel(int level) {
      m_level = level;
   }

   public boolean isNew() {
      return m_level <= 1;
   }

   public String toString() {
      return "Building[id=" + m_id + ",type=" + m_type + ", level=" + m_level + ",typeId=" + m_typeId + "]";
   }
}
