package com.site.dal.jdbc;

import org.junit.Test;

import com.site.dal.jdbc.datasource.DataSourceException;
import com.site.dal.jdbc.entity.EntityInfoManager;
import com.site.lookup.ComponentTestCase;
import com.site.test.user.address.dal.UserAddress;
import com.site.test.user.address.dal.UserAddressDao;
import com.site.test.user.address.dal.UserAddressEntity;
import com.site.test.user.dal.User;
import com.site.test.user.dal.UserDao;
import com.site.test.user.dal.UserEntity;

public class UserDaoTest extends ComponentTestCase {
   private EntityInfoManager m_entityManager;

   @Override
   public void setUp() throws Exception {
      super.setUp();

      m_entityManager = lookup(EntityInfoManager.class);

      m_entityManager.register(UserEntity.class);
      m_entityManager.register(UserAddressEntity.class);
   }

   @Test
   public void testFindByPK() throws Exception {
      try {
         UserDao userDao = lookup(UserDao.class);
         UserAddressDao userAddressDao = lookup(UserAddressDao.class);
         User user1 = userDao.createLocal();

         user1.setKeyUserId(1);
         userDao.delete(user1);
         userAddressDao.deleteAllByUserId(1);

         user1.setUserId(1);
         user1.setUserName("user name");
         userDao.insert(user1);

         User user2 = userDao.findByPK(1, UserEntity.READSET_FULL);

         assertEquals(user2.getUserId(), user1.getUserId());
         assertEquals(user2.getUserName(), user1.getUserName());

         UserAddress userAddress1 = userAddressDao.createLocal();

         userAddress1.setUserId(1);
         userAddress1.setType("H");
         userAddress1.setAddress("Home Address");
         userAddressDao.insert(userAddress1);

         userAddress1.setUserId(1);
         userAddress1.setType("O");
         userAddress1.setAddress("Office Address");
         userAddressDao.insert(userAddress1);

         userAddress1.setUserId(1);
         userAddress1.setType("B");
         userAddress1.setAddress("Billing Address");
         userAddressDao.insert(userAddress1);

         User user3 = userDao.findWithSubObjectsByPK(1, UserEntity.READSET_FULL_WITH_ALL_ADDRESSES_FULL);
         assertEquals("H", user3.getHomeAddress().getType());
         assertEquals("Home Address", user3.getHomeAddress().getAddress());
         assertEquals("O", user3.getOfficeAddress().getType());
         assertEquals("Office Address", user3.getOfficeAddress().getAddress());
         assertEquals("B", user3.getBillingAddress().getType());
         assertEquals("Billing Address", user3.getBillingAddress().getAddress());

         userDao.delete(user1);
         userAddressDao.deleteAllByUserId(1);
      } catch (DataSourceException e) {
         if (e.isDataSourceDown()) {
            System.out.println("Can't connect to database, gave up");
         } else {
            throw e;
         }
      }
   }
}
