package com.site.wdbc.linkedin;

import java.sql.SQLException;
import java.util.Map;

import com.site.wdbc.http.Processor;
import com.site.wdbc.http.Session;

public class ProgressProcessor implements Processor {
   private DatabaseAccess m_databaseAccess;

   private String m_level1;

   private String m_level2;

   public void execute(Session session) {
      Map<String, String> prop = session.getProperties();
      String l1 = prop.get(m_level1);
      String l2 = prop.get(m_level2);

      try {
         m_databaseAccess.insertProgress(l1, l2);
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public void setLevel1(String level1) {
      m_level1 = level1;
   }

   public void setLevel2(String level2) {
      m_level2 = level2;
   }
}
