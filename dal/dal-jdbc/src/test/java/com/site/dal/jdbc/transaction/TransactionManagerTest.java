package com.site.dal.jdbc.transaction;

import org.junit.Test;

import com.site.dal.jdbc.QueryEngine;
import com.site.dal.jdbc.datasource.DataSourceException;
import com.site.dal.jdbc.entity.EntityInfoManager;
import com.site.lookup.ComponentTestCase;
import com.site.test.user.dal.User;
import com.site.test.user.dal.UserEntity;

public class TransactionManagerTest extends ComponentTestCase {
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

   @Test
   public void testNoTransaction() throws Exception {
      try {
         delete(1);
         insert(1, "user 1");
         select(1, "user 1");
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
   public void testTransactionCommit() throws Exception {
      try {
         delete(1);
         startTransaction(1);
         insert(1, "user 1");
         select(1, "user 1");
         update(1, "user 11");
         commitTransaction(1);
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
   public void testTransactionRollback() throws Exception {
      try {
         delete(1);
         insert(1, "user 1");
         startTransaction(1);
         select(1, "user 1");
         update(1, "user 11");
         rollbackTransaction(1);
         select(1, "user 1");
         delete(1);
      } catch (DataSourceException e) {
         if (e.isDataSourceDown()) {
            System.out.println("Can't connect to database, gave up");
         } else {
            throw e;
         }
      }
   }

   private void startTransaction(int id) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      User proto = new User();

      proto.setKeyUserId(id);

      queryEngine.startTransaction(UserEntity.UPDATE_BY_PK, proto);
   }

   private void commitTransaction(int id) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      User proto = new User();

      proto.setKeyUserId(id);

      queryEngine.commitTransaction(UserEntity.UPDATE_BY_PK, proto);
   }

   private void rollbackTransaction(int id) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      User proto = new User();

      proto.setKeyUserId(id);

      queryEngine.rollbackTransaction(UserEntity.UPDATE_BY_PK, proto);
   }

   protected void update(int id, String userName) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      User proto = new User();

      proto.setKeyUserId(id);
      proto.setUserName(userName);

      queryEngine.updateSingle(UserEntity.UPDATE_BY_PK, proto, UserEntity.UPDATESET_FULL);
   }

   @Override
   public void setUp() throws Exception {
      super.setUp();

      EntityInfoManager entityManager = lookup(EntityInfoManager.class);

      entityManager.register(UserEntity.class);
   }

}
