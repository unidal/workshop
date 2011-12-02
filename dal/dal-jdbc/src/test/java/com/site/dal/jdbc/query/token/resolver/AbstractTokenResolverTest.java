package com.site.dal.jdbc.query.token.resolver;

import com.site.dal.jdbc.DataObject;
import com.site.dal.jdbc.QueryDef;
import com.site.dal.jdbc.Readset;
import com.site.dal.jdbc.Updateset;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.dal.jdbc.entity.DataObjectAccessor;
import com.site.dal.jdbc.entity.EntityInfo;
import com.site.dal.jdbc.entity.EntityInfoManager;
import com.site.dal.jdbc.query.Parameter;
import com.site.dal.jdbc.query.QueryResolver;
import com.site.lookup.ComponentTestCase;
import com.site.test.user.address.dal.UserAddressEntity;
import com.site.test.user.dal.UserEntity;
import com.site.test.user.dal.invalid.User2Entity;

public abstract class AbstractTokenResolverTest extends ComponentTestCase {
   protected EntityInfoManager m_entityManager;

   protected QueryResolver m_queryResolver;

   private DataObjectAccessor m_dataObjectAccessor;

   @Override
   public void setUp() throws Exception {
      super.setUp();

      m_entityManager = lookup(EntityInfoManager.class);
      m_queryResolver = lookup(QueryResolver.class, "MySql");
      m_dataObjectAccessor = lookup(DataObjectAccessor.class);

      m_entityManager.register(UserEntity.class);
      m_entityManager.register(User2Entity.class);
      m_entityManager.register(UserAddressEntity.class);
   }

   protected <T extends DataObject> QueryContext getSelectContext(QueryDef query, T proto, Readset<?> readset)
         throws Exception {
      QueryContext ctx = lookup(QueryContext.class);
      EntityInfo enityInfo = m_entityManager.getEntityInfo(query.getEntityClass());

      ctx.setQuery(query);
      ctx.setProto(proto);
      ctx.setReadset(readset);
      ctx.setEntityInfo(enityInfo);

      return ctx;
   }

   protected <T extends DataObject> QueryContext getUpdateContext(QueryDef query, T proto, Updateset<?> updateset)
         throws Exception {
      QueryContext ctx = lookup(QueryContext.class);
      EntityInfo enityInfo = m_entityManager.getEntityInfo(query.getEntityClass());

      ctx.setQuery(query);
      ctx.setProto(proto);
      ctx.setUpdateset(updateset);
      ctx.setEntityInfo(enityInfo);

      return ctx;
   }

   protected Object getParameterValue(QueryContext ctx, int index) {
      Parameter parameter = ctx.getParameters().get(index);

      return m_dataObjectAccessor.getFieldValue(ctx.getProto(), parameter.getField());
   }
}
