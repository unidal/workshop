package com.site.test.user.dal.invalid;

import com.site.dal.jdbc.DataField;
import com.site.dal.jdbc.QueryDef;
import com.site.dal.jdbc.QueryType;
import com.site.dal.jdbc.Readset;
import com.site.dal.jdbc.Updateset;

public final class NoAnnotatedEntity {

   public static DataField USER_ADDRESS = new DataField("user-address");

   public static DataField USER_ID = new DataField("user-id");

   public static DataField USER_NAME = new DataField("user-name");

   public static DataField CREATION_DATE = new DataField("creation-date");

   public static Readset<Object> READSET_FULL = new Readset<Object>(USER_ID, USER_NAME);

   public static Updateset<Object> UPDATESET_FULL = new Updateset<Object>(USER_NAME);

   public static QueryDef FIND_BY_PK = new QueryDef(NoAnnotatedEntity.class, QueryType.SELECT,
         "SELECT <FIELDS/> FROM <TABLE/> WHERE <FIELD name='user-id'/> = ${key-user-id}");
}
