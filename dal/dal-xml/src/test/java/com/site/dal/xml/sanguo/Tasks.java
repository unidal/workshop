package com.site.dal.xml.sanguo;

import com.site.dal.xml.annotation.XmlAttribute;
import com.site.dal.xml.annotation.XmlElement;
import com.site.dal.xml.annotation.XmlElements;

@XmlElement(name = "tasks")
public class Tasks {
   @XmlAttribute(name = "zjzbid")
   private int m_zjzbid;

   @XmlElements(@XmlElement(name = "task", type = Task.class))
   private Task[] m_tasks;

   public int getZjzbid() {
      return m_zjzbid;
   }

   public void setZjzbid(int zjzbid) {
      m_zjzbid = zjzbid;
   }

   public Task[] getTasks() {
      return m_tasks;
   }

   public void setTasks(Task[] tasks) {
      m_tasks = tasks;
   }

}
