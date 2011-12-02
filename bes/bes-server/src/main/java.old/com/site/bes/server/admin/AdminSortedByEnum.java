package com.site.bes.server.admin;

import com.site.kernel.common.BaseEnum;

public class AdminSortedByEnum extends BaseEnum {
   public static final AdminSortedByEnum CONSUMER = new AdminSortedByEnum(1, "consumer");

   public static final AdminSortedByEnum EVENT = new AdminSortedByEnum(2, "event");

   protected AdminSortedByEnum(int id, String name) {
      super(id, name);
   }

   public static AdminSortedByEnum getByName(String name) {
      return (AdminSortedByEnum) BaseEnum.getByName(AdminSortedByEnum.class, name);
   }
}
