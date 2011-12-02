package com.site.dal.jdbc.query.token.resolver;

import com.site.dal.jdbc.DalRuntimeException;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.dal.jdbc.mapping.TableProvider;
import com.site.dal.jdbc.mapping.TableProviderManager;
import com.site.dal.jdbc.query.token.SimpleTagToken;
import com.site.dal.jdbc.query.token.Token;
import com.site.dal.jdbc.query.token.TokenType;

/**
 * &lt;table [name="<i>table-name</i>"] [alias="<i>new-table-alias</i>"] /&gt;
 */
public class TableTokenResolver implements TokenResolver {
   private TableProviderManager m_manager;

   public String resolve(Token token, QueryContext ctx) {
      if (token.getType() != TokenType.TABLE) {
         throw new DalRuntimeException("Internal error: only TABLE token is supported by " + getClass());
      }

      SimpleTagToken table = (SimpleTagToken) token;
      String tableName = table.getAttribute("name", ctx.getEntityInfo().getLogicalName());
      String[] logicalNameAndAlias = ctx.getEntityInfo().getLogicalNameAndAlias(tableName);
      TableProvider tableProvider = m_manager.getTableProvider(logicalNameAndAlias[0]);
      String physicalTableName = tableProvider.getPhysicalTableName(ctx.getQueryHints());

      switch (ctx.getQuery().getType()) {
      case SELECT:
         String alias = table.getAttribute("alias", logicalNameAndAlias[1]);

         return physicalTableName + " " + alias;
      case INSERT:
         return physicalTableName;
      case UPDATE:
         return physicalTableName;
      case DELETE:
         return physicalTableName;
      default:
         throw new DalRuntimeException("TABLE token does not support query type: " + ctx.getQuery().getType());
      }
   }
}
