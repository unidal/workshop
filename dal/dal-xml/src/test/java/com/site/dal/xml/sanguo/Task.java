package com.site.dal.xml.sanguo;

import java.util.Date;

import com.site.dal.xml.annotation.XmlAttribute;
import com.site.dal.xml.annotation.XmlElement;

@XmlElement(name = "task")
public class Task {
   // type="8" queueid="853907" intro="ÍÁÄ¾Ë¾ £¨Lv 6£©"
   // begintime="2008-09-06 11:16:33" endtime="2008-09-06 12:16:22"
   @XmlAttribute(name = "type")
   private int m_type;

   @XmlAttribute(name = "queueid")
   private int m_queueId;

   @XmlAttribute(name = "intro")
   private String m_intro;

   @XmlAttribute(name = "begintime", format = "yyyy-MM-dd HH:mm:ss")
   private Date m_beginTime;

   @XmlAttribute(name = "endtime", format = "yyyy-MM-dd HH:mm:ss")
   private Date m_endTime;

   public int getType() {
      return m_type;
   }

   public int getQueueId() {
      return m_queueId;
   }

   public String getIntro() {
      return m_intro;
   }

   public Date getBeginTime() {
      return m_beginTime;
   }

   public Date getEndTime() {
      return m_endTime;
   }

   public void setType(int type) {
      m_type = type;
   }

   public void setQueueId(int queueId) {
      m_queueId = queueId;
   }

   public void setIntro(String intro) {
      m_intro = intro;
   }

   public void setBeginTime(Date beginTime) {
      m_beginTime = beginTime;
   }

   public void setEndTime(Date endTime) {
      m_endTime = endTime;
   }
}
