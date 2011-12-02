package com.site.dal.jdbc.query.token.resolver;

import org.junit.Test;

import com.site.dal.jdbc.QueryDef;
import com.site.dal.jdbc.QueryType;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.test.user.dal.User;
import com.site.test.user.dal.UserEntity;

public class ValuesTokenResolverTest extends AbstractTokenResolverTest {
   @Test
   public void testInsert1() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.INSERT, "<values/>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      user.setUserName("test");
      m_queryResolver.resolve(ctx);
      assertEquals("?,NOW(),NOW(),password(?)", ctx.getSqlStatement());
      assertEquals("[${user-name}, ${password}]", ctx.getParameters().toString());
   }
   
   @Test
   public void testInsert2() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.INSERT, "<values/>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);
      
      user.setUserId(1234);
      user.setUserName("test");
      m_queryResolver.resolve(ctx);
      assertEquals("?,?,NOW(),NOW(),password(?)", ctx.getSqlStatement());
      assertEquals("[${user-id}, ${user-name}, ${password}]", ctx.getParameters().toString());
   }
}
