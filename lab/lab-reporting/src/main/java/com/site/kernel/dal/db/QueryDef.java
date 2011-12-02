package com.site.kernel.dal.db;

import com.site.kernel.dal.db.helpers.QueryContext;
import com.site.kernel.dal.db.helpers.Statement;

public class QueryDef {
   private Entity m_entity;

   private QueryType m_type;

   private boolean m_storedProcedure;

   private Statement m_statement;

   public QueryDef(Entity entity, QueryType type, String sqlStatement) {
      this(entity, type, sqlStatement, false);
   }

   public QueryDef(Entity entity, QueryType type, String sqlStatement, boolean isStoredProcedure) {
      m_entity = entity;
      m_type = type;
      m_storedProcedure = isStoredProcedure;
      m_statement = new Statement(sqlStatement);
   }

   public Entity getEntity() {
      return m_entity;
   }

   public String getSqlStatement(QueryContext ctx) {
      return m_statement.getSqlStatement(ctx);
   }

   public QueryType getType() {
      return m_type;
   }

   public boolean isStoredProcedure() {
      return m_storedProcedure;
   }

}