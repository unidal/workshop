package com.site.dal.jdbc.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.PooledConnection;

import com.site.dal.jdbc.DataField;
import com.site.dal.jdbc.DataObject;
import com.site.dal.jdbc.QueryDef;
import com.site.dal.jdbc.Readset;
import com.site.dal.jdbc.Updateset;
import com.site.dal.jdbc.entity.EntityInfo;
import com.site.dal.jdbc.query.Parameter;

public class DefaultQueryContext implements QueryContext {
   private QueryDef m_query;

   private DataObject m_proto;

   private Readset<?> m_readset;

   private Updateset<?> m_updateset;

   private EntityInfo m_entityInfo;

   private PooledConnection m_connection;

   private String m_sqlStatement;

   private List<Parameter> m_parameters = new ArrayList<Parameter>();

   private List<DataField> m_outFields = new ArrayList<DataField>();

   private List<String> m_outSubObjectNames = new ArrayList<String>();

   private int m_fetchSize;

   private boolean m_withinInToken;

   private boolean m_withinIfToken;

   private boolean m_sqlResolveEnabled;

   private boolean m_rawSql;

   private Map<String, Object> m_queryHints;

   public void addOutField(DataField dataField) {
      if (m_outFields == null) {
         m_outFields = new ArrayList<DataField>();
      }

      m_outFields.add(dataField);
   }

   public void addOutSubObjectName(String subObjectName) {
      m_outSubObjectNames.add(subObjectName);
   }

   public void addParameter(Parameter value) {
      m_parameters.add(value);
   }

   public PooledConnection getConnection() {
      return m_connection;
   }

   public EntityInfo getEntityInfo() {
      return m_entityInfo;
   }

   public int getFetchSize() {
      return m_fetchSize;
   }

   public List<DataField> getOutFields() {
      return m_outFields;
   }

   public List<String> getOutSubObjectNames() {
      return m_outSubObjectNames;
   }

   public List<Parameter> getParameters() {
      return m_parameters;
   }

   public DataObject getProto() {
      return m_proto;
   }

   public QueryDef getQuery() {
      return m_query;
   }

   public Map<String, Object> getQueryHints() {
      return m_queryHints;
   }

   public Readset<?> getReadset() {
      return m_readset;
   }

   public String getSqlStatement() {
      return m_sqlStatement;
   }

   public Updateset<?> getUpdateset() {
      return m_updateset;
   }

   public boolean isRawSql() {
      return m_rawSql;
   }

   public boolean isSqlResolveDisabled() {
      return m_sqlResolveEnabled;
   }

   public boolean isWithinIfToken() {
      return m_withinIfToken;
   }

   public boolean isWithinInToken() {
      return m_withinInToken;
   }

   public void setConnection(PooledConnection connection) {
      m_connection = connection;
   }

   public void setEntityInfo(EntityInfo entityInfo) {
      m_entityInfo = entityInfo;
   }

   public void setFetchSize(int fetchSize) {
      m_fetchSize = fetchSize;
   }

   public void setProto(DataObject proto) {
      m_proto = proto;
   }

   public void setQuery(QueryDef query) {
      m_query = query;
   }

   public void setQueryHints(Map<String, Object> queryHints) {
      m_queryHints = queryHints;
   }

   public void setRawSql(boolean rawSql) {
      m_rawSql = rawSql;
   }

   public void setReadset(Readset<?> readset) {
      m_readset = readset;
   }

   public void setSqlResolveDisabled(boolean sqlResolveDisabled) {
      m_sqlResolveEnabled = sqlResolveDisabled;
   }

   public void setSqlStatement(String sqlStatement) {
      m_sqlStatement = sqlStatement;
   }

   public void setUpdateset(Updateset<?> updateset) {
      m_updateset = updateset;
   }

   public void setWithinIfToken(boolean withinIfToken) {
      m_withinIfToken = withinIfToken;
   }

   public void setWithinInToken(boolean withinInToken) {
      m_withinInToken = withinInToken;
   }
}
