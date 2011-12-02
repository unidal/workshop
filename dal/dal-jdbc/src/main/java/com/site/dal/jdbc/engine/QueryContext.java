package com.site.dal.jdbc.engine;

import java.util.List;
import java.util.Map;

import com.site.dal.jdbc.DataField;
import com.site.dal.jdbc.DataObject;
import com.site.dal.jdbc.QueryDef;
import com.site.dal.jdbc.Readset;
import com.site.dal.jdbc.Updateset;
import com.site.dal.jdbc.entity.EntityInfo;
import com.site.dal.jdbc.query.Parameter;

public interface QueryContext {
   public void addOutField(DataField field);

   public void addOutSubObjectName(String subObjectName);

   public void addParameter(Parameter value);

   public EntityInfo getEntityInfo();

   public int getFetchSize();

   public List<DataField> getOutFields();

   public List<String> getOutSubObjectNames();

   public List<Parameter> getParameters();

   public DataObject getProto();

   public QueryDef getQuery();

   public Map<String, Object> getQueryHints();

   public Readset<?> getReadset();

   public String getSqlStatement();

   public Updateset<?> getUpdateset();

   public boolean isSqlResolveDisabled();

   public boolean isWithinIfToken();

   public boolean isWithinInToken();

   public void setEntityInfo(EntityInfo entityInfo);

   public void setFetchSize(int fetchSize);

   public void setProto(DataObject proto);

   public void setQuery(QueryDef query);

   public void setQueryHints(Map<String, Object> queryHints);

   public void setReadset(Readset<?> readset);

   public void setSqlResolveDisabled(boolean sqlResolveDisabled);

   public void setSqlStatement(String sqlStatement);

   public void setUpdateset(Updateset<?> updateset);

   public void setWithinIfToken(boolean withinIfToken);

   public void setWithinInToken(boolean withinInToken);
}
