package com.site.dal.jdbc.query.token.resolver;

import org.junit.Test;

import com.site.dal.jdbc.QueryDef;
import com.site.dal.jdbc.QueryType;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.test.user.dal.User;
import com.site.test.user.dal.UserEntity;

public class IfTokenResolverTest extends AbstractTokenResolverTest {
   @Test
   public void testEqFalse() throws Exception {
      QueryDef query = new QueryDef("test", UserEntity.class, QueryType.SELECT,
            "<IF type='EQ' field='key-user-id' value='1234'>...</IF>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      user.setKeyUserId(1233);
      assertEquals(false, ctx.isWithinIfToken());
      m_queryResolver.resolve(ctx);
      assertEquals("", ctx.getSqlStatement());
      assertEquals(false, ctx.isWithinIfToken());
   }

   @Test
   public void testEqTrue() throws Exception {
      QueryDef query = new QueryDef("test", UserEntity.class, QueryType.SELECT,
            "<IF type='EQ' field='key-user-id' value='1234'>...</IF>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      user.setKeyUserId(1234);
      assertEquals(false, ctx.isWithinIfToken());
      m_queryResolver.resolve(ctx);
      assertEquals("...", ctx.getSqlStatement());
      assertEquals(false, ctx.isWithinIfToken());
   }
   
   @Test
   public void testEqTrueWithSlash() throws Exception {
      QueryDef query = new QueryDef("test", UserEntity.class, QueryType.SELECT,
      "<IF type='EQ' field='key-user-id' value='1234'>a/b</IF>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);
      
      user.setKeyUserId(1234);
      assertEquals(false, ctx.isWithinIfToken());
      m_queryResolver.resolve(ctx);
      assertEquals("a/b", ctx.getSqlStatement());
      assertEquals(false, ctx.isWithinIfToken());
   }

   @Test
   public void testFalseAndParameter() throws Exception {
      QueryDef query = new QueryDef("test", UserEntity.class, QueryType.SELECT,
            "<IF type='NOT_NULL' field='key-user-id'>${key-user-id}</IF>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      assertEquals(false, ctx.isWithinIfToken());
      m_queryResolver.resolve(ctx);
      assertEquals("", ctx.getSqlStatement());
      assertEquals(false, ctx.isWithinIfToken());
      assertEquals(0, ctx.getParameters().size());
   }

   @Test
   public void testNotNullFalse() throws Exception {
      QueryDef query = new QueryDef("test", UserEntity.class, QueryType.SELECT,
            "<IF type='NOT_NULL' field='key-user-id'>...</IF>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      assertEquals(false, ctx.isWithinIfToken());
      m_queryResolver.resolve(ctx);
      assertEquals("", ctx.getSqlStatement());
      assertEquals(false, ctx.isWithinIfToken());
   }

   @Test
   public void testNotNullTrue() throws Exception {
      QueryDef query = new QueryDef("test", UserEntity.class, QueryType.SELECT,
            "<IF type='NOT_NULL' field='key-user-id'>...</IF>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      user.setKeyUserId(1234);
      assertEquals(false, ctx.isWithinIfToken());
      m_queryResolver.resolve(ctx);
      assertEquals("...", ctx.getSqlStatement());
      assertEquals(false, ctx.isWithinIfToken());
   }

   @Test
   public void testNotZeroFalse() throws Exception {
      QueryDef query = new QueryDef("test", UserEntity.class, QueryType.SELECT,
            "<IF type='NOT_ZERO' field='key-user-id'>...</IF>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      user.setKeyUserId(0);
      assertEquals(false, ctx.isWithinIfToken());
      m_queryResolver.resolve(ctx);
      assertEquals("", ctx.getSqlStatement());
      assertEquals(false, ctx.isWithinIfToken());
   }

   @Test
   public void testNotZeroTrue() throws Exception {
      QueryDef query = new QueryDef("test", UserEntity.class, QueryType.SELECT,
            "<IF type='NOT_ZERO' field='key-user-id'>...</IF>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      user.setKeyUserId(1234);
      assertEquals(false, ctx.isWithinIfToken());
      m_queryResolver.resolve(ctx);
      assertEquals("...", ctx.getSqlStatement());
      assertEquals(false, ctx.isWithinIfToken());
   }

   @Test
   public void testTrueAndParameter() throws Exception {
      QueryDef query = new QueryDef("test", UserEntity.class, QueryType.SELECT,
            "<IF type='NOT_NULL' field='key-user-id'>${key-user-id}</IF>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      user.setKeyUserId(1234L);
      assertEquals(false, ctx.isWithinIfToken());
      m_queryResolver.resolve(ctx);
      assertEquals("?", ctx.getSqlStatement());
      assertEquals(false, ctx.isWithinIfToken());
      assertEquals(1, ctx.getParameters().size());
      assertEquals(1234L, getParameterValue(ctx, 0));
   }

   @Test
   public void testZeroFalse() throws Exception {
      QueryDef query = new QueryDef("test", UserEntity.class, QueryType.SELECT, "<IF type='ZERO' field='key-user-id'>...</IF>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      user.setKeyUserId(1234);
      assertEquals(false, ctx.isWithinIfToken());
      m_queryResolver.resolve(ctx);
      assertEquals("", ctx.getSqlStatement());
      assertEquals(false, ctx.isWithinIfToken());
   }

   @Test
   public void testZeroTrue() throws Exception {
      QueryDef query = new QueryDef("test", UserEntity.class, QueryType.SELECT, "<IF type='ZERO' field='key-user-id'>...</IF>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      user.setKeyUserId(0);
      assertEquals(false, ctx.isWithinIfToken());
      m_queryResolver.resolve(ctx);
      assertEquals("...", ctx.getSqlStatement());
      assertEquals(false, ctx.isWithinIfToken());
   }
}
