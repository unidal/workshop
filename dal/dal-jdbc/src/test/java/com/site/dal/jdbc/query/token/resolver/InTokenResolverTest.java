package com.site.dal.jdbc.query.token.resolver;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.site.dal.jdbc.QueryDef;
import com.site.dal.jdbc.QueryType;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.test.user.dal.User;
import com.site.test.user.dal.UserEntity;

public class InTokenResolverTest extends AbstractTokenResolverTest {
   @Test
   public void testInConstant() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<IN>...</IN>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      assertEquals(false, ctx.isWithinInToken());
      m_queryResolver.resolve(ctx);
      assertEquals("(...)", ctx.getSqlStatement());
      assertEquals(0, ctx.getParameters().size());
      assertEquals(false, ctx.isWithinInToken());
   }

   @Test
   public void testInNormalParameter() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<IN>${user-id}</IN>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      user.setUserId(1234L);
      assertEquals(false, ctx.isWithinInToken());
      m_queryResolver.resolve(ctx);
      assertEquals("(?)", ctx.getSqlStatement());
      assertEquals(1, ctx.getParameters().size());
      assertEquals(1234L, getParameterValue(ctx, 0));
      assertEquals(false, ctx.isWithinInToken());
   }

   @Test
   public void testInArrayParameter() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<IN>${user-id-array}</IN>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);
      long[] userIdArray = new long[] { 1L, 2L, 3L, 4L };

      user.setUserIdArray(userIdArray);
      assertEquals(false, ctx.isWithinInToken());
      m_queryResolver.resolve(ctx);
      assertEquals("(?,?,?,?)", ctx.getSqlStatement());
      assertEquals(1, ctx.getParameters().size());
      assertEquals(userIdArray, getParameterValue(ctx, 0));
      assertEquals(false, ctx.isWithinInToken());
   }

   @Test
   public void testInEmptyArrayParameter() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<IN>${user-id-array}</IN>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      user.setUserIdArray(new long[0]);
      assertEquals(false, ctx.isWithinInToken());
      m_queryResolver.resolve(ctx);
      assertEquals("(null)", ctx.getSqlStatement());
      assertEquals(0, ctx.getParameters().size());
      assertEquals(false, ctx.isWithinInToken());
   }

   @Test
   public void testInListParameter() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<IN>${user-id-list}</IN>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);

      user.setUserIdList(Arrays.asList(1L, 2L, 3L, 4L));
      assertEquals(false, ctx.isWithinInToken());
      m_queryResolver.resolve(ctx);
      assertEquals("(?,?,?,?)", ctx.getSqlStatement());
      assertEquals(1, ctx.getParameters().size());
      assertEquals("[1, 2, 3, 4]", getParameterValue(ctx, 0).toString());
      assertEquals(false, ctx.isWithinInToken());
   }

   @Test
   public void testInEmptyListParameter() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<IN>${user-id-list}</IN>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);
      List<Long> emptyList = Collections.emptyList();

      user.setUserIdList(emptyList);
      assertEquals(false, ctx.isWithinInToken());
      m_queryResolver.resolve(ctx);
      assertEquals("(null)", ctx.getSqlStatement());
      assertEquals(0, ctx.getParameters().size());
      assertEquals(false, ctx.isWithinInToken());
   }

   @Test
   public void testInBothParameters() throws Exception {
      QueryDef query = new QueryDef(UserEntity.class, QueryType.SELECT, "<IN>${user-id},${user-id-array}</IN>");
      User user = new User();
      QueryContext ctx = getSelectContext(query, user, null);
      long[] userIdArray = new long[] { 1, 2, 3, 4 };

      user.setUserId(1234L);
      user.setUserIdArray(userIdArray);
      assertEquals(false, ctx.isWithinInToken());
      m_queryResolver.resolve(ctx);
      assertEquals("(?,?,?,?,?)", ctx.getSqlStatement());
      assertEquals(2, ctx.getParameters().size());
      assertEquals(1234L, getParameterValue(ctx, 0));
      assertEquals(userIdArray, getParameterValue(ctx, 1));
      assertEquals(false, ctx.isWithinInToken());
   }
}
