package com.site.dal.jdbc.query.token.resolver;

import com.site.dal.jdbc.DalRuntimeException;
import com.site.dal.jdbc.annotation.Relation;
import com.site.dal.jdbc.annotation.SubObjects;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.dal.jdbc.entity.EntityInfo;
import com.site.dal.jdbc.query.token.Token;
import com.site.dal.jdbc.query.token.TokenType;

/**
 * &lt;joins /&gt;
 */
public class JoinsTokenResolver implements TokenResolver {
   public String resolve(Token token, QueryContext ctx) {
      if (token.getType() != TokenType.JOINS) {
         throw new DalRuntimeException("Internal error: only JOINS token is supported by " + getClass());
      }

      switch (ctx.getQuery().getType()) {
      case SELECT:
         EntityInfo entityInfo = ctx.getEntityInfo();
         SubObjects subobject = entityInfo.getSubobjects(ctx.getReadset());
         StringBuilder sb = new StringBuilder(256);

         if (subobject != null) {
            String[] names = subobject.value();

            for (String name : names) {
               if (name != null && name.length() > 0) {
                  Relation relation = entityInfo.getRelation(name);

                  if (sb.length() > 0) {
                     sb.append(" and ");
                  }

                  sb.append(relation.join());
               }
            }
         }

         if (sb.length() == 0) {
            sb.append("1=1");
         }

         return sb.toString();
      case INSERT:
         throw new DalRuntimeException("TABLES token does not support query type: " + ctx.getQuery().getType());
      case UPDATE:
         throw new DalRuntimeException("TABLES token does not support query type: " + ctx.getQuery().getType());
      case DELETE:
         throw new DalRuntimeException("TABLES token does not support query type: " + ctx.getQuery().getType());
      default:
         throw new DalRuntimeException("TABLES token does not support query type: " + ctx.getQuery().getType());
      }
   }
}
