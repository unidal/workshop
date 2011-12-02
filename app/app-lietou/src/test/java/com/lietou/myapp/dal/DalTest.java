package com.lietou.myapp.dal;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.site.lookup.ComponentTestCase;

public class DalTest extends ComponentTestCase {
   @Test
   @Ignore
   public void notestIps() throws Exception {
      IpsDao dao = lookup(IpsDao.class);

      List<Ips> result = dao.findAll(IpsEntity.READSET_FULL);

      System.out.println(result.size());
      System.out.println(result);
   }

   @Test
   public void testWebUser() throws Exception {
      WebUserDao dao = lookup(WebUserDao.class);
      List<WebUser> result = dao.findAll(WebUserEntity.READSET_MIN);

      System.out.println(result.size());
//      System.out.println(result);
   }
}
