package com.site.dal.xml.sanguo;

import com.site.dal.xml.annotation.XmlAttribute;
import com.site.dal.xml.annotation.XmlElement;

@XmlElement(name = "actions")
public class Actions {
   @XmlAttribute(name = "quid")
   private int m_quid;

   @XmlAttribute(name = "quname")
   private String m_quname;

   @XmlAttribute(name = "act")
   private String m_act;

   @XmlAttribute(name = "villageid")
   private int m_villageId;

   @XmlAttribute(name = "rand")
   private int m_rand;

   public String getAct() {
      return m_act;
   }

   public int getQuid() {
      return m_quid;
   }

   public String getQuname() {
      return m_quname;
   }

   public int getRand() {
      return m_rand;
   }

   public int getVillageId() {
      return m_villageId;
   }

   public void setAct(String act) {
      m_act = act;
   }

   public void setQuid(int quid) {
      m_quid = quid;
   }

   public void setQuname(String quname) {
      m_quname = quname;
   }

   public void setRand(int rand) {
      m_rand = rand;
   }

   public void setVillageId(int villageId) {
      m_villageId = villageId;
   }
}
