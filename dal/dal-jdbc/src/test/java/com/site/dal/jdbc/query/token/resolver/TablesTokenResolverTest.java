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

public class TablesTokenResolverTest extends AbstractTokenResolverTest {
   @Test
   public void testSelectOneTable() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<TABLES/>");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals("user u", ctx.getSqlStatement());
   }

   @Test
   public void testSelectTwoTables() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<TABLES/>");
      Readset<?> readset = UserEntity.READSET_FULL_WITH_HOME_ADDRESS_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals("user u, user_address hua", ctx.getSqlStatement());
   }

   @Test
   public void testSelectThreeTables() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<TABLES/>");
      Readset<?> readset = UserEntity.READSET_FULL_WITH_HOME_OFFICE_ADDRESS_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals("user u, user_address hua, user_address oua", ctx.getSqlStatement());
   }

   @Test
   public void testInsert() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.INSERT, "<TABLES/>");
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
      QueryDef query = new QueryDef(UserEntity.class, QueryType.UPDATE, "<TABLES/>");
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
      QueryDef query = new QueryDef(UserEntity.class, QueryType.DELETE, "<TABLES/>");
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
