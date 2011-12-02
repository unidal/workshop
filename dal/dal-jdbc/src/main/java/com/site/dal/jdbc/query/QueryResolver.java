package com.site.dal.jdbc.query;

import com.site.dal.jdbc.engine.QueryContext;

public interface QueryResolver {
   public void resolve(QueryContext ctx);
}
