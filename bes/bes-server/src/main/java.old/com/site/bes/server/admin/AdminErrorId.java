package com.site.bes.server.admin;

import com.site.web.DefaultErrorId;

public class AdminErrorId extends DefaultErrorId {
   public static final AdminErrorId EVENT_NOT_FOUND = new AdminErrorId(201, "BIZ.EVENT_NOT_FOUND");

   public static final AdminErrorId DASHBOARD_NOT_FOUND = new AdminErrorId(202, "BIZ.DASHBOARD_NOT_FOUND");

   public static final AdminErrorId DAL_EXCEPTION = new AdminErrorId(203, "BIZ.DAL_EXCEPTION");

   public static final AdminErrorId RUNTIME_EXCEPTION = new AdminErrorId(204, "BIZ.RUNTIME_EXCEPTION");

   protected AdminErrorId(int id, String name) {
      super(id, name);
   }
}
