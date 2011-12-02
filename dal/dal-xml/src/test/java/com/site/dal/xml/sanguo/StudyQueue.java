package com.site.dal.xml.sanguo;

import com.site.dal.xml.annotation.XmlAttribute;
import com.site.dal.xml.annotation.XmlElement;

@XmlElement(name = "studyqueue")
public class StudyQueue {
   @XmlAttribute(name = "bqs")
   private int m_bqs;

   @XmlAttribute(name = "ytj")
   private int m_ytj;

   @XmlAttribute(name = "hby")
   private int m_hby;

   @XmlAttribute(name = "jc")
   private int m_jc;

   @XmlAttribute(name = "xg")
   private int m_xg;

   public int getBqs() {
      return m_bqs;
   }

   public int getYtj() {
      return m_ytj;
   }

   public int getHby() {
      return m_hby;
   }

   public int getJc() {
      return m_jc;
   }

   public int getXg() {
      return m_xg;
   }

   public void setBqs(int bqs) {
      m_bqs = bqs;
   }

   public void setYtj(int ytj) {
      m_ytj = ytj;
   }

   public void setHby(int hby) {
      m_hby = hby;
   }

   public void setJc(int jc) {
      m_jc = jc;
   }

   public void setXg(int xg) {
      m_xg = xg;
   }
}
