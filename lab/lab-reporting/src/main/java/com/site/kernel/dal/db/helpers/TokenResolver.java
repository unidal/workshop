package com.site.kernel.dal.db.helpers;

import com.site.kernel.dal.db.Entity;
import com.site.kernel.dal.db.EntityJoin;
import com.site.kernel.dal.db.QueryType;

public abstract class TokenResolver {
   public static final String NAME = TokenResolver.class.getName();

   public abstract String getTablesClause(QueryType type, Entity primary, EntityJoin[] joins);

   public abstract String getJoinsClause(QueryType type, Entity primary, EntityJoin[] joins);

}
