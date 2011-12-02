package com.site.wdbc.linkedin;

import com.site.lookup.ComponentTestCase;

public class DatabaseAccessTest extends ComponentTestCase {
   public void testEmployee() throws Exception {
      DatabaseAccess da = lookup(DatabaseAccess.class);
      String id = "99999";

      assertFalse(da.hasEmployee(id));
      da.insertEmployee(id, "firstName", "lastName", "profile", "12345");
      assertTrue(da.hasEmployee(id));
      da.removeEmployee(id);
   }

   public void testJob() throws Exception {
      DatabaseAccess da = lookup(DatabaseAccess.class);

      da.insertJob("99999", "title", "company", "from data", "to date");
      da.removeJob("99999");
   }

   public void testLocation() throws Exception {
      DatabaseAccess da = lookup(DatabaseAccess.class);

      da.insertLocation("T5125", "TT");
      da.insertLocation("T5126", "TT");
      da.updateLocation("T5125", 1, 0);
      da.updateLocation("T5125", 2, 100);
      da.removeLocation("T5125");
      da.removeLocation("T5126");
   }
}
