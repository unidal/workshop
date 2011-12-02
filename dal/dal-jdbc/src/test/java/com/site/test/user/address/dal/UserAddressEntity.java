package com.site.test.user.address.dal;

import com.site.dal.jdbc.DataField;
import com.site.dal.jdbc.QueryDef;
import com.site.dal.jdbc.QueryType;
import com.site.dal.jdbc.Readset;
import com.site.dal.jdbc.Updateset;
import com.site.dal.jdbc.annotation.Attribute;
import com.site.dal.jdbc.annotation.Entity;
import com.site.dal.jdbc.annotation.Variable;

@Entity(logicalName = "user-address", alias = "ua")
public class UserAddressEntity {

   @Attribute(field = "user_id", nullable = false, primaryKey = true)
   public static DataField USER_ID = new DataField("user-id");

   @Attribute(field = "type", nullable = false, primaryKey = true)
   public static DataField TYPE = new DataField("type");

   @Attribute(field = "address", nullable = false)
   public static DataField ADDRESS = new DataField("address");

   @Variable
   public static DataField KEY_USER_ID = new DataField("key-user-id");

   @Variable
   public static DataField KEY_TYPE = new DataField("key-type");

   public static Readset<UserAddress> READSET_FULL = new Readset<UserAddress>(USER_ID, TYPE, ADDRESS);

   public static Updateset<UserAddress> UPDATESET_FULL = new Updateset<UserAddress>(TYPE, ADDRESS);

   public static QueryDef FIND_BY_PK = new QueryDef(UserAddressEntity.class, QueryType.SELECT,
         "SELECT <FIELDS/> FROM <TABLE/> WHERE <FIELD name='user-id'/> = ${key-user-id} AND <FIELD name='type'/> = ${key-type}");

   public static final QueryDef INSERT = new QueryDef(UserAddressEntity.class, QueryType.INSERT,
         "INSERT INTO <TABLE/> (<FIELDS/>) VALUES (<VALUES/>)");

   public static final QueryDef UPDATE_BY_PK = new QueryDef(UserAddressEntity.class, QueryType.UPDATE,
         "UPDATE <TABLE/> SET <FIELDS/> WHERE <FIELD name='user-id'/> = ${key-user-id} AND <FIELD name='type'/> = ${key-type}");

   public static final QueryDef DELETE_BY_PK = new QueryDef(UserAddressEntity.class, QueryType.DELETE,
         "DELETE FROM <TABLE/> WHERE <FIELD name='user-id'/> = ${key-user-id} AND <FIELD name='type'/> = ${key-type}");

   public static final QueryDef DELETE_ALL_BY_USER_ID = new QueryDef(UserAddressEntity.class, QueryType.DELETE,
         "DELETE FROM <TABLE/> WHERE <FIELD name='user-id'/> = ${key-user-id}");

}
