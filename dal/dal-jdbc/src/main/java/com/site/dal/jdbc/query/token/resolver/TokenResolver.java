package com.site.dal.jdbc.query.token.resolver;

import com.site.dal.jdbc.engine.QueryContext;
import com.site.dal.jdbc.query.token.Token;

public interface TokenResolver {
   public String resolve(Token token, QueryContext ctx);
}
