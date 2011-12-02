package com.site.dal.jdbc.query.token.resolver;

import org.junit.Test;

import com.site.dal.jdbc.DalRuntimeException;
import com.site.dal.jdbc.QueryDef;
import com.site.dal.jdbc.QueryType;
import com.site.dal.jdbc.Readset;
import com.site.dal.jdbc.Updateset;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.test.user.dal.User;
import com.site.test.user.dal.UserEntity;

public class JoinsTokenResolverTest extends AbstractTokenResolverTest {
   @Test
   public void testSelectOneTable() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<JOINS/>");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals("1=1", ctx.getSqlStatement());
   }

   @Test
   public void testSelectOneTable2() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<JOINS/>");
      Readset<?> readset = UserEntity.READSET_FULL_U;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);
      
      m_queryResolver.resolve(ctx);
      assertEquals("1=1", ctx.getSqlStatement());
   }
   
   @Test
   public void testSelectTwoTables() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<JOINS/>");
      Readset<?> readset = UserEntity.READSET_FULL_WITH_HOME_ADDRESS_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals("u.user_id=hua.user_id and hua.type='H'", ctx.getSqlStatement());
   }

   @Test
   public void testInsert() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.INSERT, "<JOINS/>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      try {
         m_queryResolver.resolve(ctx);

         fail("DalRuntimeException expected");
      } catch (DalRuntimeException e) {
         // expected
      }
   }

   @Test
   public void testUpdate() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.UPDATE, "<JOINS/>");
      Updateset<User> updateset = UserEntity.UPDATESET_FULL;
      User user = new User();
      QueryContext ctx = getUpdateContext(query, user, updateset);

      try {
         m_queryResolver.resolve(ctx);

         fail("DalRuntimeException expected");
      } catch (DalRuntimeException e) {
         // expected
      }
   }

   @Test
   public void testDelete() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.DELETE, "<JOINS/>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      try {
         m_queryResolver.resolve(ctx);

         fail("DalRuntimeException expected");
      } catch (DalRuntimeException e) {
         // expected
      }
   }
}
