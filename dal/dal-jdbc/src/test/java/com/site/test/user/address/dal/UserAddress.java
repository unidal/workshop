package com.site.test.user.address.dal;

import static com.site.test.user.address.dal.UserAddressEntity.ADDRESS;
import static com.site.test.user.address.dal.UserAddressEntity.KEY_TYPE;
import static com.site.test.user.address.dal.UserAddressEntity.KEY_USER_ID;
import static com.site.test.user.address.dal.UserAddressEntity.TYPE;
import static com.site.test.user.address.dal.UserAddressEntity.USER_ID;

import com.site.dal.jdbc.DataObject;

public class UserAddress extends DataObject {
   private int m_keyUserId;

   private int m_userId;

   private String m_type;

   private String m_keyType;

   private String m_address;

   public UserAddress() {
      super();
   }

   public String getAddress() {
      return m_address;
   }

   public String getType() {
      return m_type;
   }

   public int getUserId() {
      return m_userId;
   }

   public void setAddress(String address) {
      setFieldUsed(ADDRESS, true);
      m_address = address;
   }

   public void setType(String type) {
      setFieldUsed(TYPE, true);
      m_type = type;
   }

   public void setUserId(int userId) {
      setFieldUsed(USER_ID, true);
      m_userId = userId;
   }

   public String getKeyType() {
      return m_keyType;
   }

   public void setKeyType(String keyType) {
      setFieldUsed(KEY_TYPE, true);
      m_keyType = keyType;
   }

   public int getKeyUserId() {
      return m_keyUserId;
   }

   public void setKeyUserId(int keyUserId) {
      setFieldUsed(KEY_USER_ID, true);
      m_keyUserId = keyUserId;
   }

}
