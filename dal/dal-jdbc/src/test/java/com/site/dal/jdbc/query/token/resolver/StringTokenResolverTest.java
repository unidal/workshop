package com.site.dal.jdbc.query.token.resolver;

import org.junit.Test;

import com.site.dal.jdbc.QueryDef;
import com.site.dal.jdbc.QueryType;
import com.site.dal.jdbc.Readset;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.test.user.dal.User;
import com.site.test.user.dal.UserEntity;

public class StringTokenResolverTest extends AbstractTokenResolverTest {
   @Test
   public void testString() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "anything");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals("anything", ctx.getSqlStatement());
   }
}
