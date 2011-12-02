package com.site.dal.jdbc.query.token.resolver;

import com.site.dal.jdbc.DalRuntimeException;
import com.site.dal.jdbc.DataField;
import com.site.dal.jdbc.DataObject;
import com.site.dal.jdbc.annotation.Attribute;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.dal.jdbc.entity.EntityInfo;
import com.site.dal.jdbc.query.Parameter;
import com.site.dal.jdbc.query.token.SimpleTagToken;
import com.site.dal.jdbc.query.token.Token;
import com.site.dal.jdbc.query.token.TokenType;
import com.site.lookup.annotation.Inject;

/**
 * &lt;value name="<i>field-name</i>" /&gt;
 */
public class ValueTokenResolver implements TokenResolver {
   @Inject
   private ExpressionResolver m_expressionResolver;
   
   public String resolve(Token token, QueryContext ctx) {
      if (token.getType() != TokenType.VALUE) {
         throw new DalRuntimeException("Internal error: only VALUE token is supported by " + getClass());
      }

      EntityInfo entityInfo = ctx.getEntityInfo();
      StringBuilder sb = new StringBuilder(1024);
      String fieldName = ((SimpleTagToken) token).getAttribute("name", null);
      DataField field = ctx.getEntityInfo().getFieldByName(fieldName);

      if (field != null) {
         switch (ctx.getQuery().getType()) {
         case SELECT:
            throw new DalRuntimeException("VALUE token does not support query type: " + ctx.getQuery().getType());
         case INSERT:
            DataObject proto = ctx.getProto();
            Attribute attribute = entityInfo.getAttribute(field);

            if (!proto.isFieldUsed(field) && attribute.insertExpr().length() > 0) {
               sb.append(m_expressionResolver.resolve(ctx, attribute.insertExpr()));
            } else {
               ctx.addParameter(new Parameter(field));
               sb.append('?');
            }

            break;
         case UPDATE:
            throw new DalRuntimeException("VALUES token does not support query type: " + ctx.getQuery().getType());
         case DELETE:
            throw new DalRuntimeException("VALUES token does not support query type: " + ctx.getQuery().getType());
         default:
            throw new DalRuntimeException("VALUES token does not support query type: " + ctx.getQuery().getType());
         }
      } else {
         throw new DalRuntimeException("DataField(" + fieldName + ") is not defined in "
               + ctx.getQuery().getEntityClass() + ". Query: " + ctx.getQuery());
      }

      return sb.toString();
   }
}
