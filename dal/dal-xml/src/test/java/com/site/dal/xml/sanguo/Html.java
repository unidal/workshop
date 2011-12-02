package com.site.dal.xml.sanguo;

import com.site.dal.xml.annotation.XmlAttribute;
import com.site.dal.xml.annotation.XmlElement;
import com.site.dal.xml.annotation.XmlValue;

@XmlElement(name = "html")
public class Html {
   @XmlAttribute(name = "id")
   private String m_id;

   @XmlValue
   private String m_value;

   public String getId() {
      return m_id;
   }

   public String getValue() {
      return m_value;
   }

   public void setId(String id) {
      m_id = id;
   }

   public void setValue(String value) {
      m_value = value;
   }

}
