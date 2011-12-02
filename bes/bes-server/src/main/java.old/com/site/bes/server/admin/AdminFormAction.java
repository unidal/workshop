package com.site.bes.server.admin;

import com.site.kernel.common.BaseEnum;

public class AdminFormAction extends BaseEnum {
   public static final AdminFormAction LIST = new AdminFormAction(1, "list");

   public static final AdminFormAction DASHBOARD = new AdminFormAction(2, "dashboard");

   public static final AdminFormAction EVENT = new AdminFormAction(3, "event");

   protected AdminFormAction(int id, String name) {
      super(id, name);
   }

   public static AdminFormAction getByName(String name, AdminFormAction defaultValue) {
      BaseEnum e = BaseEnum.getByName(AdminFormAction.class, name);

      return (e == null ? defaultValue : (AdminFormAction) e);
   }
}
