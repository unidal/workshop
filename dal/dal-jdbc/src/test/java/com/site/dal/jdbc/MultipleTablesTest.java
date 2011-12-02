package com.site.dal.jdbc;

import org.junit.Test;

import com.site.dal.jdbc.datasource.DataSourceException;
import com.site.dal.jdbc.entity.EntityInfoManager;
import com.site.lookup.ComponentTestCase;
import com.site.test.user.address.dal.UserAddress;
import com.site.test.user.address.dal.UserAddressEntity;
import com.site.test.user.dal.User;
import com.site.test.user.dal.UserEntity;

public class MultipleTablesTest extends ComponentTestCase {
   private EntityInfoManager m_entityManager;

   protected void deleteUser(int id) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      User proto = new User();

      proto.setKeyUserId(id);

      queryEngine.deleteSingle(UserEntity.DELETE_BY_PK, proto);
   }

   protected void deleteUserAddresses(int id) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      UserAddress proto = new UserAddress();

      proto.setKeyUserId(id);

      queryEngine.deleteSingle(UserAddressEntity.DELETE_ALL_BY_USER_ID, proto);
   }

   protected void deleteUserAddress(int id, String type) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      UserAddress proto = new UserAddress();

      proto.setKeyUserId(id);
      proto.setKeyType(type);

      queryEngine.deleteSingle(UserAddressEntity.DELETE_BY_PK, proto);
   }

   protected void insertUser(int id, String userName) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      User proto = new User();

      proto.setUserId(id);
      proto.setUserName(userName);

      queryEngine.insertSingle(UserEntity.INSERT, proto);
   }

   protected void insertUserAddress(int id, String type, String address) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      UserAddress proto = new UserAddress();

      proto.setUserId(id);
      proto.setType(type);
      proto.setAddress(address);

      queryEngine.insertSingle(UserAddressEntity.INSERT, proto);
   }

   protected void selectUser(int id, String userName) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      User proto = new User();

      proto.setKeyUserId(id);

      User user = queryEngine.querySingle(UserEntity.FIND_BY_PK, proto, UserEntity.READSET_FULL);

      assertNotNull(user);
      assertEquals(id, user.getUserId());
      assertEquals(userName, user.getUserName());
      assertNotNull(user.getCreationDate());
      assertNotNull(user.getLastModifiedDate());
   }

   protected void selectUserAndAddress(int id, String userName) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      User proto = new User();

      proto.setKeyUserId(id);

      User user = queryEngine.querySingle(UserEntity.FIND_WITH_SUBOBJECTS_BY_PK, proto,
            UserEntity.READSET_FULL_WITH_HOME_OFFICE_ADDRESS_FULL);

      assertNotNull(user);
      assertEquals(id, user.getUserId());
      assertEquals(userName, user.getUserName());
      assertNotNull(user.getCreationDate());
      assertNotNull(user.getLastModifiedDate());
      assertNotNull(user.getHomeAddress());
      assertNotNull(user.getOfficeAddress());
      assertNull(user.getBillingAddress());
   }

   protected void selectUserAddress(int id, String type, String address) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      UserAddress proto = new UserAddress();

      proto.setKeyUserId(id);
      proto.setKeyType(type);

      UserAddress userAddress = queryEngine.querySingle(UserAddressEntity.FIND_BY_PK, proto,
            UserAddressEntity.READSET_FULL);

      assertNotNull(userAddress);
      assertEquals(id, userAddress.getUserId());
      assertEquals(type, userAddress.getType());
      assertEquals(address, userAddress.getAddress());
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
         deleteUser(1);
         deleteUserAddresses(1);
         insertUser(1, "user 1");
         insertUserAddress(1, "H", "home address 1");
         insertUserAddress(1, "B", "billing address 1");
         insertUserAddress(1, "O", "office address 1");
         selectUserAndAddress(1, "user 1");
         updateUser(1, "user 11");
         updateUserAddress(1, "H", "home address 11");
         selectUser(1, "user 11");
         selectUserAddress(1, "H", "home address 11");
         selectUserAddress(1, "B", "billing address 1");
         selectUserAddress(1, "O", "office address 1");
         selectUserAndAddress(1, "user 11");
         deleteUser(1);
         deleteUserAddresses(1);
      } catch (DataSourceException e) {
         if (e.isDataSourceDown()) {
            System.out.println("Can't connect to database, gave up");
         } else {
            throw e;
         }
      }
   }

   protected void updateUser(int id, String userName) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      User proto = new User();

      proto.setKeyUserId(id);
      proto.setUserName(userName);

      queryEngine.updateSingle(UserEntity.UPDATE_BY_PK, proto, UserEntity.UPDATESET_FULL);
   }

   protected void updateUserAddress(int id, String type, String newAddress) throws Exception {
      QueryEngine queryEngine = lookup(QueryEngine.class);
      UserAddress proto = new UserAddress();

      proto.setKeyUserId(id);
      proto.setKeyType(type);
      proto.setAddress(newAddress);

      queryEngine.updateSingle(UserAddressEntity.UPDATE_BY_PK, proto, UserAddressEntity.UPDATESET_FULL);
   }
}
