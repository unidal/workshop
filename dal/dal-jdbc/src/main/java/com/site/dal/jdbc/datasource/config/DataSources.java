package com.site.dal.jdbc.datasource.config;


import com.site.dal.xml.annotation.XmlElement;
import com.site.dal.xml.annotation.XmlRootElement;
import com.site.dal.xml.annotation.XmlElements;

@XmlRootElement(name = "data-sources")
public class DataSources {
   @XmlElements(@XmlElement(name = "data-source", type = DataSource.class))
   private DataSource[] m_dataSourceList = new DataSource[0];

   public DataSource[] getDataSourceList() {
      return m_dataSourceList;
   }

   public void setDataSourceList(DataSource[] dataSourceList) {
      m_dataSourceList = dataSourceList;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();

      sb.append("DataSources[");
      sb.append("dataSourceList=").append(m_dataSourceList);
      sb.append("]");

      return sb.toString();
   }

}
