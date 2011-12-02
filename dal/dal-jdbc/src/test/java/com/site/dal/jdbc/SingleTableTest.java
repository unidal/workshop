package com.site.dal.jdbc;

import java.util.List;

import org.junit.Test;

import com.site.dal.jdbc.datasource.DataSourceException;
import com.site.dal.jdbc.entity.EntityInfoManager;
import com.site.lookup.ComponentTestCase;
import com.site.test.user.address.dal.UserAddressEntity;
import com.site.test.user.dal.User;
import com.site.test.user.dal.UserEntity;

public class SingleTableTest extends ComponentTestCase {
   private EntityInfoManager m_entityManager;

   protected void delete(int id) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      User proto = new User();

      proto.setKeyUserId(id);

      queryEngine.deleteSingle(UserEntity.DELETE_BY_PK, proto);
   }

   protected void insert(int id, String userName) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      User proto = new User();

      proto.setUserId(id);
      proto.setUserName(userName);

      queryEngine.insertSingle(UserEntity.INSERT, proto);
   }

   protected void select(int id, String userName) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      User proto = new User();

      proto.setKeyUserId(id);

      User user = queryEngine.querySingle(UserEntity.FIND_BY_PK, proto, UserEntity.READSET_FULL);

      assertNotNull(user);
      assertEquals(proto.getKeyUserId(), user.getUserId());
      assertEquals(userName, user.getUserName());
      assertNotNull(user.getCreationDate());
      assertNotNull(user.getLastModifiedDate());
   }

   protected void selectWithStoreProcedure(int count) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      User proto = new User();

      proto.setPageSize(count);
      List<User> users = queryEngine.queryMultiple(UserEntity.FIND_ALL_USERS, proto, UserEntity.READSET_COMPACT);

      assertNotNull(users);
      assertEquals(2, users.size());
      assertEquals(3, proto.getPageSize());
   }

   @Override
   public void setUp() throws Exception {
      super.setUp();

      m_entityManager = lookup(EntityInfoManager.class);

      m_entityManager.register(UserEntity.class);
      m_entityManager.register(UserAddressEntity.class);
   }

   @Test
   public void testSingle() throws Exception {
      try {
         delete(1);
         insert(1, "user 1");
         update(1, "user 11");
         select(1, "user 11");
         delete(1);
      } catch (DataSourceException e) {
         if (e.isDataSourceDown()) {
            System.out.println("Can't connect to database, gave up");
         } else {
            throw e;
         }
      }
   }

   @Test
   public void testMultiple() throws Exception {
      try {
         delete(1);
         delete(2);
         delete(3);
         insert(1, "user 1");
         insert(2, "user 2");
         insert(3, "user 3");
         update(1, "user 11");
         update(3, "user 31");
         select(1, "user 11");
         select(2, "user 2");
         select(3, "user 31");
         // TODO remove temporary
         // selectWithStoreProcedure(3);
         delete(1);
         delete(2);
         delete(3);
      } catch (DataSourceException e) {
         if (e.isDataSourceDown()) {
            System.out.println("Can't connect to database, gave up");
         } else {
            throw e;
         }
      }
   }

   protected void update(int id, String userName) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      User proto = new User();

      proto.setKeyUserId(id);
      proto.setUserName(userName);

      queryEngine.updateSingle(UserEntity.UPDATE_BY_PK, proto, UserEntity.UPDATESET_FULL);
   }
}
