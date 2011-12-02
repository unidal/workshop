package com.site.dal.xml.sanguo;

import com.site.dal.xml.annotation.XmlAttribute;
import com.site.dal.xml.annotation.XmlElement;

@XmlElement(name = "resource")
public class Resource {
   @XmlAttribute(name = "id")
   private String m_id;

   @XmlAttribute(name = "now")
   private double m_now;

   @XmlAttribute(name = "max")
   private double m_max;

   @XmlAttribute(name = "speed")
   private double m_speed;

   @XmlAttribute(name = "increase")
   private double m_increase;

   public String getId() {
      return m_id;
   }

   public double getIncrease() {
      return m_increase;
   }

   public double getMax() {
      return m_max;
   }

   public double getNow() {
      return m_now;
   }

   public double getSpeed() {
      return m_speed;
   }

   public void setId(String id) {
      m_id = id;
   }

   public void setIncrease(double increase) {
      m_increase = increase;
   }

   public void setMax(double max) {
      m_max = max;
   }

   public void setNow(double now) {
      m_now = now;
   }

   public void setSpeed(double speed) {
      m_speed = speed;
   }

}
