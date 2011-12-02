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

public class FieldsTokenResolverTest extends AbstractTokenResolverTest {
   @Test
   public void testSelect() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<fields/>");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals("u.user_id,u.full_name,u.creation_date,u.last_modified_date", ctx.getSqlStatement());
      assertEquals(null, ctx.getOutSubObjectNames().get(0));
   }

   @Test
   public void testSelect2() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<fields/>");
      Readset<?> readset = UserEntity.READSET_FULL_U;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);
      
      m_queryResolver.resolve(ctx);
      assertEquals("u.user_id,u.full_name,u.creation_date,u.last_modified_date,upper(full_name)", ctx.getSqlStatement());
      assertEquals(null, ctx.getOutSubObjectNames().get(0));
      assertEquals(null, ctx.getOutSubObjectNames().get(4));
   }
   
   @Test
   public void testSelect3() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<fields excludes='user-name,last-modified-date'/>");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals("u.user_id,u.creation_date", ctx.getSqlStatement());
      assertEquals(2, ctx.getOutSubObjectNames().size());
      assertEquals(null, ctx.getOutSubObjectNames().get(0));
      assertEquals(2, ctx.getOutFields().size());
      assertEquals(UserEntity.USER_ID, ctx.getOutFields().get(0));
      assertEquals(UserEntity.CREATION_DATE, ctx.getOutFields().get(1));
   }

   @Test
   public void testSelect4() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<fields/> <fields excludes='user-name,last-modified-date' output='false'/>");
      Readset<?> readset = UserEntity.READSET_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);
      
      m_queryResolver.resolve(ctx);
      assertEquals("u.user_id,u.full_name,u.creation_date,u.last_modified_date u.user_id,u.creation_date", ctx.getSqlStatement());
      assertEquals(4, ctx.getOutSubObjectNames().size());
      assertEquals(4, ctx.getOutFields().size());
   }
   
   @Test
   public void testSelectOnTwoTables() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<fields/>");
      Readset<?> readset = UserEntity.READSET_FULL_WITH_HOME_ADDRESS_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals("u.user_id,u.full_name,u.creation_date,u.last_modified_date,hua.user_id,hua.type,hua.address",
            ctx.getSqlStatement());
      assertEquals(null, ctx.getOutSubObjectNames().get(0));
      assertEquals("home-address", ctx.getOutSubObjectNames().get(4));
   }

   @Test
   public void testSelectOnThreeTables() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<fields/>");
      Readset<?> readset = UserEntity.READSET_FULL_WITH_HOME_OFFICE_ADDRESS_FULL;
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, readset);

      m_queryResolver.resolve(ctx);
      assertEquals(
            "u.user_id,u.full_name,u.creation_date,u.last_modified_date,hua.user_id,hua.type,hua.address,oua.user_id,oua.type,oua.address",
            ctx.getSqlStatement());
      assertEquals(null, ctx.getOutSubObjectNames().get(0));
      assertEquals("home-address", ctx.getOutSubObjectNames().get(4));
   }

   @Test
   public void testInsert1() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.INSERT, "<fields/>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      user.setUserName("test");
      m_queryResolver.resolve(ctx);
      assertEquals("full_name,creation_date,last_modified_date,encrypted_password", ctx.getSqlStatement());
   }
   
   @Test
   public void testInsert2() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.INSERT, "<fields/>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);
      
      user.setUserId(1234);
      user.setUserName("test");
      m_queryResolver.resolve(ctx);
      assertEquals("user_id,full_name,creation_date,last_modified_date,encrypted_password", ctx.getSqlStatement());
   }

   @Test
   public void testUpdateWithoutFieldUsed() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.UPDATE, "<fields/>");
      Updateset<User> updateset = UserEntity.UPDATESET_FULL;
      User user = new User();
      QueryContext ctx = getUpdateContext(query, user, updateset);

      m_queryResolver.resolve(ctx);
      assertEquals("last_modified_date=NOW()", ctx.getSqlStatement());
      assertEquals(0, ctx.getParameters().size());
   }
   
   @Test
   public void testUpdateWithoutFieldUsed2() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.UPDATE, "<fields/>");
      Updateset<User> updateset = UserEntity.UPDATESET_PASS;
      User user = new User();
      QueryContext ctx = getUpdateContext(query, user, updateset);
      
      m_queryResolver.resolve(ctx);
      assertEquals("encrypted_password=password(?)", ctx.getSqlStatement());
      assertEquals("[${password}]", ctx.getParameters().toString());
   }

   @Test
   public void testUpdateWithFieldUsed() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.UPDATE, "<fields/>");
      Updateset<User> updateset = UserEntity.UPDATESET_FULL;
      User user = new User();
      QueryContext ctx = getUpdateContext(query, user, updateset);

      user.setUserName("test");
      m_queryResolver.resolve(ctx);
      assertEquals("full_name=?,last_modified_date=NOW()", ctx.getSqlStatement());
      assertEquals(1, ctx.getParameters().size());

      assertEquals("test", getParameterValue(ctx, 0));
   }

   @Test
   public void testDelete() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.DELETE, "<fields/>");
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
