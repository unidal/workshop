package com.site.dal.xml.sanguo;

import com.site.dal.xml.annotation.XmlAttribute;
import com.site.dal.xml.annotation.XmlElement;

@XmlElement(name = "reports")
public class Reports {
   @XmlAttribute(name = "reportnew")
   private int m_reportNew;

   @XmlAttribute(name = "msgnew")
   private int m_msgnew;

   public int getReportNew() {
      return m_reportNew;
   }

   public int getMsgnew() {
      return m_msgnew;
   }

   public void setReportNew(int reportNew) {
      m_reportNew = reportNew;
   }

   public void setMsgnew(int msgnew) {
      m_msgnew = msgnew;
   }
}
