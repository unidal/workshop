package com.site.dal.jdbc.query.token.resolver;

import com.site.dal.jdbc.DalRuntimeException;
import com.site.dal.jdbc.DataField;
import com.site.dal.jdbc.DataObject;
import com.site.dal.jdbc.annotation.Attribute;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.dal.jdbc.entity.EntityInfo;
import com.site.dal.jdbc.query.Parameter;
import com.site.dal.jdbc.query.token.Token;
import com.site.dal.jdbc.query.token.TokenType;
import com.site.lookup.annotation.Inject;

/**
 * &lt;values /&gt;
 */
public class ValuesTokenResolver implements TokenResolver {
   @Inject
   private ExpressionResolver m_expressionResolver;
   
   public String resolve(Token token, QueryContext ctx) {
      if (token.getType() != TokenType.VALUES) {
         throw new DalRuntimeException("Internal error: only VALUES token is supported by " + getClass());
      }

      EntityInfo entityInfo = ctx.getEntityInfo();
      StringBuilder sb = new StringBuilder(1024);

      switch (ctx.getQuery().getType()) {
      case SELECT:
         throw new DalRuntimeException("VALUES token does not support query type: " + ctx.getQuery().getType());
      case INSERT:
         DataObject proto = ctx.getProto();
         
         for (DataField field : entityInfo.getAttributeFields()) {
            Attribute attribute = entityInfo.getAttribute(field);

            if (attribute != null) {
               if (attribute.field().length() > 0 && !(attribute.autoIncrement() && !proto.isFieldUsed(field))) {
                  if (sb.length() > 0) {
                     sb.append(',');
                  }

                  if (!proto.isFieldUsed(field) && attribute.insertExpr().length() > 0) {
                     sb.append(m_expressionResolver.resolve(ctx, attribute.insertExpr()));
                  } else {
                     sb.append('?');
                     ctx.addParameter(new Parameter(field));
                  }
               }
            } else {
               throw new DalRuntimeException("Internal error: No Attribute annotation defined for field: " + field);
            }
         }

         break;
      case UPDATE:
         throw new DalRuntimeException("VALUES token does not support query type: " + ctx.getQuery().getType());
      case DELETE:
         throw new DalRuntimeException("VALUES token does not support query type: " + ctx.getQuery().getType());
      default:
         throw new DalRuntimeException("VALUES token does not support query type: " + ctx.getQuery().getType());
      }

      return sb.toString();
   }
}
