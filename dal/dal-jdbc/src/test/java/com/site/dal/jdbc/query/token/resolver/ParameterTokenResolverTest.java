package com.site.dal.jdbc.query.token.resolver;

import org.junit.Test;

import com.site.dal.jdbc.DalRuntimeException;
import com.site.dal.jdbc.QueryDef;
import com.site.dal.jdbc.QueryType;
import com.site.dal.jdbc.Readset;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.test.user.dal.User;
import com.site.test.user.dal.UserEntity;

public class ParameterTokenResolverTest extends AbstractTokenResolverTest {
   @Test
   public void testNonExistField() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "${unknown}");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      try {
         m_queryResolver.resolve(ctx);

         fail("DalRuntimeException expected");
      } catch (DalRuntimeException e) {
         // expected
      }
   }

   @Test
   public void testAttribute() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "${user-id}");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      user.setUserId(1234L);
      m_queryResolver.resolve(ctx);
      assertEquals("?", ctx.getSqlStatement());
      assertEquals(1234L, getParameterValue(ctx, 0));
   }
   
   @Test
   public void testAttributeWithDefaultValue() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "${user-id}");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals("?", ctx.getSqlStatement());
      assertEquals(0L, getParameterValue(ctx, 0));
   }

   @Test
   public void testVariable() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "${key-user-id}");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      user.setKeyUserId(1234L);
      m_queryResolver.resolve(ctx);
      assertEquals("?", ctx.getSqlStatement());
      assertEquals(1234L, getParameterValue(ctx, 0));
   }

}
