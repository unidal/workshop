package com.site.bes.server.admin;

import com.site.web.page.PageResponseStatus;

public class AdminResponseCode extends PageResponseStatus {
   public static final AdminResponseCode LIST = new AdminResponseCode(1, "list");

   public static final AdminResponseCode EVENT = new AdminResponseCode(2, "event");

   public static final AdminResponseCode DASHBOARD = new AdminResponseCode(3, "dashboard");;

   protected AdminResponseCode(int id, String name) {
      super(id, name);
   }
}
