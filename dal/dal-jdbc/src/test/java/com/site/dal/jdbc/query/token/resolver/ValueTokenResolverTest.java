package com.site.dal.jdbc.query.token.resolver;

import org.junit.Test;

import com.site.dal.jdbc.QueryDef;
import com.site.dal.jdbc.QueryType;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.test.user.dal.User;
import com.site.test.user.dal.UserEntity;

public class ValueTokenResolverTest extends AbstractTokenResolverTest {
   @Test
   public void testUserName() throws Exception {
      QueryDef query = new QueryDef("test", UserEntity.class, QueryType.INSERT, "<value name='user-name'/>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      user.setUserName("test");
      m_queryResolver.resolve(ctx);
      assertEquals("?", ctx.getSqlStatement());
      assertEquals("[${user-name}]", ctx.getParameters().toString());
   }
   
   @Test
   public void testEncryptedPassword() throws Exception {
      QueryDef query = new QueryDef("test", UserEntity.class, QueryType.INSERT, "<value name='encrypted-password'/>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      user.setPassword("test");
      m_queryResolver.resolve(ctx);
      assertEquals("password(?)", ctx.getSqlStatement());
      assertEquals("[${password}]", ctx.getParameters().toString());
   }
}
