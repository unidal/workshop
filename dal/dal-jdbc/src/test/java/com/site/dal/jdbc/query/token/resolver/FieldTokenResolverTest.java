package com.site.dal.jdbc.query.token.resolver;

import org.junit.Test;

import com.site.dal.jdbc.DalRuntimeException;
import com.site.dal.jdbc.QueryDef;
import com.site.dal.jdbc.QueryType;
import com.site.dal.jdbc.Readset;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.test.user.dal.User;
import com.site.test.user.dal.UserEntity;

public class FieldTokenResolverTest extends AbstractTokenResolverTest {
   @Test
   public void testField() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<FIELD name='user-id'/>");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals("u.user_id", ctx.getSqlStatement());
   }

   @Test
   public void testFieldOfSelectExpr() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<FIELD name='upper-user-name'/>");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);
      
      m_queryResolver.resolve(ctx);
      assertEquals("upper(full_name)", ctx.getSqlStatement());
   }

   @Test
   public void testFieldWithAnotherTable() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<FIELD name='user-id' table='home-address'/>");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals("hua.user_id", ctx.getSqlStatement());
   }
   
   @Test
   public void testFieldWithAnotherTableNonrelated() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<FIELD name='user-id' table='user2'/>");
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
   public void testFieldWithSameTable() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<FIELD name='user-id' table='user'/>");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals("u.user_id", ctx.getSqlStatement());
   }

   @Test
   public void testNonExistField() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<FIELD name='unknown'/>");
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
}
