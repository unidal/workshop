package com.site.wdbc.state;

import java.sql.SQLException;
import java.util.Map;

import com.site.wdbc.http.Processor;
import com.site.wdbc.http.Session;
import com.site.wdbc.linkedin.DatabaseAccess;

public class StateProcessor implements Processor {
   private DatabaseAccess m_databaseAccess;

   public void execute(Session session) {
      Map<String, String> prop = session.getProperties();
      String state = prop.get("state-links:state");
      String zipcode = prop.get("state-zipcode:zipcode");

      try {
         m_databaseAccess.insertLocation(zipcode, state);
      } catch (SQLException e) {
         System.err.println(e.toString());
      }
   }
}
