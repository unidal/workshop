package com.site.kernel.dal.db.helpers;

import java.util.ArrayList;
import java.util.List;

import javax.sql.ConnectionPoolDataSource;

import com.site.kernel.dal.db.DataRow;
import com.site.kernel.dal.db.DataRowField;
import com.site.kernel.dal.db.Entity;
import com.site.kernel.dal.db.QueryType;
import com.site.kernel.dal.db.Readset;
import com.site.kernel.dal.db.Updateset;

/**
 * @author qwu
 */
public class QueryContext {
   private static final List<Object> EMPTY = new ArrayList<Object>(0);

   private ConnectionPoolDataSource m_dataSource;

   private Entity m_entity;

   private String m_logicalDataSourceName;

   private DataRow m_protoDo;

   private QueryType m_queryType;

   private boolean m_storedProcedure;

   private Readset m_readset;

   private String m_sqlStatement;

   private Updateset m_updateset;

   private boolean m_withinInTag;

   private boolean m_withinIfTag;

   private boolean m_noOutput;

   private List<Object> m_parameters;

   private List<Object> m_outFields;

   private int m_fetchSize;

   private TokenResolver m_tokenResolver;

   public void addOutField(DataRowField field) {
      if (m_outFields == null) {
         m_outFields = new ArrayList<Object>(20);
      }

      m_outFields.add(field);
   }

   public void addParameter(Parameter parameter) {
      if (m_parameters == null) {
         m_parameters = new ArrayList<Object>();
      }

      m_parameters.add(parameter);
   }

   public ConnectionPoolDataSource getDataSource() {
      return m_dataSource;
   }

   public Entity getEntity() {
      return m_entity;
   }

   public int getFetchSize() {
      return m_fetchSize;
   }

   public String getLogicalDataSourceName() {
      return m_logicalDataSourceName;
   }

   public List<Object> getOutFields() {
      if (m_outFields == null) {
         return EMPTY;
      } else {
         return m_outFields;
      }
   }

   public List<Object> getParameters() {
      if (m_parameters == null) {
         return EMPTY;
      } else {
         return m_parameters;
      }
   }

   public DataRow getProtoDo() {
      return m_protoDo;
   }

   public QueryType getQueryType() {
      return m_queryType;
   }

   public Readset getReadset() {
      return m_readset;
   }

   public String getSqlStatement() {
      return m_sqlStatement;
   }

   public TokenResolver getTokenResolver() {
      return m_tokenResolver;
   }

   public Updateset getUpdateset() {
      return m_updateset;
   }

   public boolean isNoOutput() {
      return m_noOutput;
   }

   public boolean isQueryDelete() {
      return QueryType.DELETE == m_queryType;
   }

   public boolean isQueryInsert() {
      return QueryType.INSERT == m_queryType;
   }

   public boolean isQuerySelect() {
      return QueryType.SELECT == m_queryType;
   }

   public boolean isQueryUpdate() {
      return QueryType.UPDATE == m_queryType;
   }

   public boolean isStoredProcedure() {
      return m_storedProcedure;
   }

   public boolean isWithinIfTag() {
      return m_withinIfTag;
   }

   public boolean isWithinInTag() {
      return m_withinInTag;
   }

   public void setDataSource(ConnectionPoolDataSource dataSource) {
      m_dataSource = dataSource;
   }

   public void setEntity(Entity entity) {
      m_entity = entity;
   }

   public void setFetchSize(int fetchSize) {
      m_fetchSize = fetchSize;
   }

   public void setLogicalDataSourceName(String logicalDataSourceName) {
      m_logicalDataSourceName = logicalDataSourceName;
   }

   public void setNoOutput(boolean noOutput) {
      m_noOutput = noOutput;
   }

   public void setProtoDo(DataRow protoDo) {
      m_protoDo = protoDo;
   }

   public void setQueryType(QueryType queryType) {
      m_queryType = queryType;
   }

   public void setReadset(Readset readset) {
      this.m_readset = readset;
   }

   public void setSqlStatement(String sqlStatement) {
      m_sqlStatement = sqlStatement;
   }

   public void setStoredProcedure(boolean storedProcedure) {
      m_storedProcedure = storedProcedure;
   }

   public void setTokenResolver(TokenResolver tokenResolver) {
      m_tokenResolver = tokenResolver;
   }

   public void setUpdateset(Updateset updateset) {
      m_updateset = updateset;
   }

   public void setWithinIfTag(boolean withinIfTag) {
      m_withinIfTag = withinIfTag;
   }

   public void setWithinInTag(boolean withinInTag) {
      m_withinInTag = withinInTag;
   }

}
