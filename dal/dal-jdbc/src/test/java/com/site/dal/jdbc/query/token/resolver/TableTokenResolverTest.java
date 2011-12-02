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

public class TableTokenResolverTest extends AbstractTokenResolverTest {
   @Test
   public void testAnotherTableRelated() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<TABLE name='home-address'/>");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals("user_address hua", ctx.getSqlStatement());
   }

   @Test
   public void testAnotherTableRelatedWithAlias() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<TABLE name='home-address' alias='address'/>");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals("user_address address", ctx.getSqlStatement());
   }

   @Test
   public void testAnotherTableNonrelated() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<TABLE name='user2'/>");
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
   public void testCurrentTable() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<TABLE/>");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals("user u", ctx.getSqlStatement());
   }

   @Test
   public void testCurrentTable2() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<TABLE name='user'/>");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals("user u", ctx.getSqlStatement());
   }

   @Test
   public void testCurrentTableWithAlias() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<TABLE alias='user'/>");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals("user user", ctx.getSqlStatement());
   }

   @Test
   public void testNonExistTable() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<TABLE name='unknown'/>");
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
   public void testCurrentTableForInsert() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.INSERT, "<TABLE/>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      m_queryResolver.resolve(ctx);
      assertEquals("user", ctx.getSqlStatement());
   }

   @Test
   public void testCurrentTableForUpdate() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.UPDATE, "<TABLE/>");
      Updateset<User> updateset = UserEntity.UPDATESET_FULL;
      User user = new User();
      QueryContext ctx = getUpdateContext(query, user, updateset);

      m_queryResolver.resolve(ctx);
      assertEquals("user", ctx.getSqlStatement());
   }

   @Test
   public void testCurrentTableForDelete() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.DELETE, "<TABLE/>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      m_queryResolver.resolve(ctx);
      assertEquals("user", ctx.getSqlStatement());
   }
}
