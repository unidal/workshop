package com.site.kernel.dal.db;

import java.util.List;
import java.util.Map;

import javax.sql.ConnectionPoolDataSource;

import com.site.kernel.ComponentManager;
import com.site.kernel.dal.DalException;
import com.site.kernel.dal.DalRuntimeException;
import com.site.kernel.dal.datasource.DataSourceException;
import com.site.kernel.dal.db.helpers.JdbcQueryExecutor;
import com.site.kernel.dal.db.helpers.QueryContext;
import com.site.kernel.dal.db.helpers.TableProviderManager;
import com.site.kernel.dal.db.helpers.TokenResolver;

/**
 * @author qwu
 */
public class QueryEngine {
   private static final QueryEngine s_instance = new QueryEngine();

   public static final String HINT_QUERY_TYPE = "QueryType";

   public static final QueryEngine getInstance() {
      return s_instance;
   }

   private QueryEngine() {
   }

   public int[] insertBatch(QueryDef query, DataRow[] protoDos) throws DalException {
      if (protoDos.length == 0) {
         return new int[0];
      }

      for (int i = 0; i < protoDos.length; i++) {
         if (!protoDos[i].checkConstraints()) {
            throw new DalRuntimeException("Please set all mandatory data for " + protoDos[i] + " at element " + i);
         }
      }

      QueryContext ctx = createContext(query, protoDos[0]);

      ctx.setSqlStatement(query.getSqlStatement(ctx));

      return JdbcQueryExecutor.getInstance().executeUpdateBatch(ctx, protoDos);
   }

   public int insertSingle(QueryDef query, DataRow protoDo) throws DalException {
      if (!protoDo.checkConstraints()) {
         throw new DalRuntimeException("Please set all mandatory data for " + protoDo);
      }

      QueryContext ctx = createContext(query, protoDo);

      ctx.setSqlStatement(query.getSqlStatement(ctx));

      return JdbcQueryExecutor.getInstance().executeUpdate(ctx);
   }

   public int[] deleteBatch(QueryDef query, DataRow[] protoDos) throws DalException {
      QueryContext ctx = createContext(query, protoDos[0]);

      ctx.setSqlStatement(query.getSqlStatement(ctx));

      return JdbcQueryExecutor.getInstance().executeUpdateBatch(ctx, protoDos);
   }

   public int deleteSingle(QueryDef query, DataRow protoDo) throws DalException {
      QueryContext ctx = createContext(query, protoDo);

      ctx.setSqlStatement(query.getSqlStatement(ctx));

      return JdbcQueryExecutor.getInstance().executeUpdate(ctx);
   }

   public <S extends DataRow> List<S> queryMultiple(QueryDef query, S protoDo, Readset readset) {
      readset.validate(query.getEntity());

      QueryContext ctx = createContext(query, protoDo);
      ctx.setReadset(readset);
      ctx.setFetchSize(50);
      ctx.setSqlStatement(query.getSqlStatement(ctx));

      try {
         return JdbcQueryExecutor.getInstance().executeQuery(ctx);
      } catch (DalException e) {
         throw new DalRuntimeException("Execution query failed: " + e, e);
      }
   }

   public <S extends DataRow> S querySingle(QueryDef query, S protoDo, Readset readset) throws DalException {
      readset.validate(query.getEntity());

      QueryContext ctx = createContext(query, protoDo);
      ctx.setReadset(readset);
      ctx.setFetchSize(1);
      ctx.setSqlStatement(query.getSqlStatement(ctx));

      List<S> results = JdbcQueryExecutor.getInstance().executeQuery(ctx);

      if (results.isEmpty()) {
         throw new DalException("No record found for " + protoDo);
      } else {
         return results.get(0);
      }
   }

   private QueryContext createContext(QueryDef query, DataRow protoDo) {
      QueryContext ctx = new QueryContext();

      ctx.setEntity(query.getEntity());
      ctx.setProtoDo(protoDo);
      ctx.setQueryType(query.getType());
      ctx.setStoredProcedure(query.isStoredProcedure());

      setupDataSource(ctx);
      setupTokenResolver(ctx);

      return ctx;
   }

   private void setupDataSource(QueryContext ctx) {
      DatabaseDataSourceManager dsm = (DatabaseDataSourceManager) ComponentManager.lookup(DatabaseDataSourceManager.NAME);
      TableProvider tp = TableProviderManager.getInstance().getTableProvider(ctx.getEntity().getLogicalName());
      String logicalDataSourceName = tp.getLogicalDataSource(getQueryHints(ctx));
      ConnectionPoolDataSource dataSource = dsm.getDataSource(logicalDataSourceName);

      ctx.setLogicalDataSourceName(logicalDataSourceName);

      if (dataSource != null) {
         ctx.setDataSource(dataSource);
      } else {
         throw new DataSourceException("No DataSource registered for " + logicalDataSourceName);
      }
   }

   private Map<String, Object> getQueryHints(QueryContext ctx) {
      Map<String, Object> hints = ctx.getProtoDo().getQueryHints();

      hints.put(HINT_QUERY_TYPE, ctx.getQueryType());
      return hints;
   }

   private void setupTokenResolver(QueryContext ctx) {
      TokenResolver tokenResolver = (TokenResolver) ComponentManager.lookup(TokenResolver.NAME);

      if (tokenResolver != null) {
         ctx.setTokenResolver(tokenResolver);
      } else {
         throw new DalRuntimeException("No TokenResolver found");
      }
   }

   public int[] updateBatch(QueryDef query, DataRow[] protoDos, Updateset updateset) throws DalException {
      updateset.validate(query.getEntity());

      QueryContext ctx = createContext(query, protoDos[0]);
      ctx.setUpdateset(updateset);
      ctx.setSqlStatement(query.getSqlStatement(ctx));

      return JdbcQueryExecutor.getInstance().executeUpdateBatch(ctx, protoDos);
   }

   public int updateSingle(QueryDef query, DataRow protoDo, Updateset updateset) throws DalException {
      updateset.validate(query.getEntity());

      QueryContext ctx = createContext(query, protoDo);
      ctx.setUpdateset(updateset);
      ctx.setSqlStatement(query.getSqlStatement(ctx));

      return JdbcQueryExecutor.getInstance().executeUpdate(ctx);
   }
}