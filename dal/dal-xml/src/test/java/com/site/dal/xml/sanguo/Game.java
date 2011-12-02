package com.site.dal.xml.sanguo;

import java.util.Date;
import java.util.List;

import com.site.dal.xml.annotation.XmlElement;
import com.site.dal.xml.annotation.XmlElementWrapper;
import com.site.dal.xml.annotation.XmlRootElement;

@XmlRootElement(name = "game")
public class Game {
   @XmlElement(name = "script")
   private String m_script;

   @XmlElement(name = "time", format = "yyyy-MM-dd HH:mm:ss")
   private Date m_time;
   
   @XmlElement(name = "village")
   private Village m_village;

   @XmlElement(name = "actions")
   private Actions m_actions;

   @XmlElementWrapper(name = "resos")
   @XmlElement(name = "reso")
   private List<Resource> m_resources;

   @XmlElementWrapper(name = "villagelist")
   @XmlElement(name = "village")
   private List<Village> m_villages;

   @XmlElement(name = "reports")
   private Reports m_reports;

   @XmlElement(name = "studyqueue")
   private StudyQueue m_studyQueue;

   @XmlElement(name = "tasks")
   private Tasks m_tasks;

   @XmlElement(name = "soldiers")
   private Soldiers m_soldiers;

   @XmlElementWrapper(name = "htmls")
   @XmlElement(name = "html")
   private List<Html> m_htmls;

   public Actions getActions() {
      return m_actions;
   }

   public List<Html> getHtmls() {
      return m_htmls;
   }

   public Reports getReports() {
      return m_reports;
   }

   public List<Resource> getResources() {
      return m_resources;
   }

   public String getScript() {
      return m_script;
   }

   public Soldiers getSoldiers() {
      return m_soldiers;
   }

   public StudyQueue getStudyQueue() {
      return m_studyQueue;
   }

   public Tasks getTasks() {
      return m_tasks;
   }

   public Date getTime() {
      return m_time;
   }

   public Village getVillage() {
      return m_village;
   }

   public List<Village> getVillages() {
      return m_villages;
   }

   public void setActions(Actions actions) {
      m_actions = actions;
   }

   public void setHtmls(List<Html> htmls) {
      m_htmls = htmls;
   }

   public void setReports(Reports reports) {
      m_reports = reports;
   }

   public void setResources(List<Resource> resources) {
      m_resources = resources;
   }

   public void setScript(String script) {
      m_script = script;
   }

   public void setSoldiers(Soldiers soldiers) {
      m_soldiers = soldiers;
   }

   public void setStudyQueue(StudyQueue studyQueue) {
      m_studyQueue = studyQueue;
   }

   public void setTasks(Tasks tasks) {
      m_tasks = tasks;
   }

   public void setTime(Date time) {
      m_time = time;
   }

   public void setVillage(Village village) {
      m_village = village;
   }

   public void setVillages(List<Village> villages) {
      m_villages = villages;
   }
}
