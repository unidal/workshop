package com.site.dal.jdbc.query.token.resolver;

import com.site.dal.jdbc.DalRuntimeException;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.dal.jdbc.query.token.EndTagToken;
import com.site.dal.jdbc.query.token.StartTagToken;
import com.site.dal.jdbc.query.token.Token;
import com.site.dal.jdbc.query.token.TokenType;

/**
 * &lt;IN&gt;...&lt/IN&gt;
 */
public class InTokenResolver implements TokenResolver {
   public String resolve(Token token, QueryContext ctx) {
      if (token.getType() != TokenType.IN) {
         throw new DalRuntimeException("Internal error: only IN token is supported by " + getClass());
      }

      if (token instanceof StartTagToken) {
         if (ctx.isWithinInToken()) {
            throw new DalRuntimeException("IN token can't be nested");
         }

         ctx.setWithinInToken(true);
         return "(";
      } else if (token instanceof EndTagToken) {
         ctx.setWithinInToken(false);
         return ")";
      } else {
         throw new DalRuntimeException("Internal error: IN token can only be used as <IN> or </IN>");
      }
   }
}
