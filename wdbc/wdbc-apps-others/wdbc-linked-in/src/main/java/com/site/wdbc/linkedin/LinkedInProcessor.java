package com.site.wdbc.linkedin;

import java.sql.SQLException;
import java.util.Map;

import com.site.wdbc.http.Processor;
import com.site.wdbc.http.Session;

public class LinkedInProcessor implements Processor {
   private DatabaseAccess m_databaseAccess;

   private String m_lastId;

   public void execute(Session session) {
      Map<String, String> prop = session.getProperties();

      String zipcode = prop.get("state-title:zipcode");
      String id = prop.get("result-set:id");
      String givenName = prop.get("result-set:given-name");
      String familyName = prop.get("result-set:family-name");
      String profile = prop.get("profile:link");
      String title = prop.get("jobs:title");
      String company = prop.get("jobs:company");
      String from = prop.get("jobs:from");
      String to = prop.get("jobs:to");

      try {
         if (id != null && id.equals(m_lastId)) {
            if (company.length() > 0) {
               m_databaseAccess.insertJob(id, title, company, from, to);
            }
         } else {
            m_databaseAccess.insertEmployee(id, givenName, familyName, profile, zipcode);
            m_databaseAccess.insertJob(id, title, company, from, to);
         }

         m_lastId = id;
      } catch (SQLException e) {
         System.err.println(e.toString());
      }
   }
}
