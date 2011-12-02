package com.site.dal.xml.sanguo;

import com.site.dal.xml.annotation.XmlAttribute;
import com.site.dal.xml.annotation.XmlElement;
import com.site.dal.xml.annotation.XmlElements;

@XmlElement(name = "soldiers")
public class Soldiers {
   @XmlAttribute(name = "zjzbid")
   private int m_zjzbid;

   @XmlElements(@XmlElement(name = "soldier", type = Soldier.class))
   private Soldier[] m_soldiers;

   public int getZjzbid() {
      return m_zjzbid;
   }

   public Soldier[] getSoldiers() {
      return m_soldiers;
   }

   public void setZjzbid(int zjzbid) {
      m_zjzbid = zjzbid;
   }

   public void setSoldiers(Soldier[] soldiers) {
      m_soldiers = soldiers;
   }
}
