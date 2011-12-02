package com.site.bes.server.admin;

import com.site.kernel.common.BaseEnum;

public class AdminPageMode extends BaseEnum {
   public static final AdminPageMode DISPLAY = new AdminPageMode(1, "display");

   public static final AdminPageMode SUBMIT = new AdminPageMode(2, "submit");

   public static final AdminPageMode XML = new AdminPageMode(3, "xml");

   public static final AdminPageMode START = new AdminPageMode(4, "start");

   public static final AdminPageMode STOP = new AdminPageMode(5, "stop");

   protected AdminPageMode(int id, String name) {
      super(id, name);
   }

   public static AdminPageMode getByName(String name, AdminPageMode defaultValue) {
      BaseEnum e = BaseEnum.getByName(AdminPageMode.class, name);

      return (e == null ? defaultValue : (AdminPageMode) e);
   }
}
