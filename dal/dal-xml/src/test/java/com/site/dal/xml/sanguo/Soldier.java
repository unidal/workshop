package com.site.dal.xml.sanguo;

import com.site.dal.xml.annotation.XmlAttribute;
import com.site.dal.xml.annotation.XmlElement;

@XmlElement(name = "soldier")
public class Soldier {
   @XmlAttribute(name = "vid")
   private int m_vid;

   @XmlAttribute(name = "tribe")
   private String m_tribe;

   @XmlAttribute(name = "type")
   private int m_type;

   @XmlAttribute(name = "count")
   private int m_count;

   public int getVid() {
      return m_vid;
   }

   public String getTribe() {
      return m_tribe;
   }

   public int getType() {
      return m_type;
   }

   public int getCount() {
      return m_count;
   }

   public void setVid(int vid) {
      m_vid = vid;
   }

   public void setTribe(String tribe) {
      m_tribe = tribe;
   }

   public void setType(int type) {
      m_type = type;
   }

   public void setCount(int count) {
      m_count = count;
   }
}
