package com.site.dal.xml.sanguo;

import com.site.dal.xml.annotation.XmlAttribute;
import com.site.dal.xml.annotation.XmlElement;

@XmlElement(name = "village")
public class Village {
   @XmlAttribute(name = "id")
   private int m_id;

   @XmlAttribute(name = "name")
   private String m_name;

   @XmlAttribute(name = "x")
   private int m_x;

   @XmlAttribute(name = "y")
   private int m_y;

   @XmlAttribute(name = "vip")
   private boolean m_vip;

   @XmlAttribute(name = "ismain")
   private boolean m_isMain;

   @XmlAttribute(name = "statename")
   private String m_stateName;

   public int getId() {
      return m_id;
   }

   public String getName() {
      return m_name;
   }

   public String getStateName() {
      return m_stateName;
   }

   public int getX() {
      return m_x;
   }

   public int getY() {
      return m_y;
   }

   public boolean isMain() {
      return m_isMain;
   }

   public boolean isVip() {
      return m_vip;
   }

   public void setId(int id) {
      m_id = id;
   }

   public void setMain(boolean isMain) {
      m_isMain = isMain;
   }

   public void setName(String name) {
      m_name = name;
   }

   public void setStateName(String stateName) {
      m_stateName = stateName;
   }

   public void setVip(boolean vip) {
      m_vip = vip;
   }

   public void setX(int x) {
      m_x = x;
   }

   public void setY(int y) {
      m_y = y;
   }
}
