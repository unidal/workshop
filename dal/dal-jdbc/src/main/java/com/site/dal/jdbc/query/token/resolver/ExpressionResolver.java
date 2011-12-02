package com.site.dal.jdbc.query.token.resolver;

import java.util.List;

import com.site.dal.jdbc.DataField;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.dal.jdbc.query.Parameter;
import com.site.dal.jdbc.query.token.ParameterToken;
import com.site.dal.jdbc.query.token.StringToken;
import com.site.dal.jdbc.query.token.Token;
import com.site.dal.jdbc.query.token.TokenParser;
import com.site.lookup.annotation.Inject;

public class ExpressionResolver {
   @Inject
   private TokenParser m_parser;

   public String resolve(QueryContext ctx, String expression) {
      List<Token> tokens = m_parser.parse(expression);
      StringBuilder sb = new StringBuilder(expression.length());

      for (Token token : tokens) {
         switch (token.getType()) {
         case STRING:
            StringToken st = (StringToken) token;

            sb.append(st.toString());
            break;
         case PARAM:
            ParameterToken pt = (ParameterToken) token;
            DataField field = getField(ctx, pt);

            sb.append('?');
            ctx.addParameter(new Parameter(field));
            break;
         default:
            throw new RuntimeException("Unsupported yet.");
         }
      }

      return sb.toString();
   }

   private DataField getField(QueryContext ctx, ParameterToken pt) {
      String name = pt.getParameterName();
      DataField field = ctx.getEntityInfo().getFieldByName(name);

      return field;
   }
}
