package com.site.wdbc.scbit;

import java.util.Map;

import com.site.wdbc.http.Session;

public class Processor implements com.site.wdbc.http.Processor {
   public void execute(Session session) {
      Map<String, String> prop = session.getProperties();
      String title = prop.get("summary:title");
      String category = prop.get("summary:category");
      String categoryLink = prop.get("summary:category-link");
      String description = prop.get("summary:description");

      System.out.println("Title: " + title);
      System.out.println("Category: " + category);
      System.out.println("Category link: " + categoryLink);
      System.out.println("Description: " + description);
      System.out.println();
   }
}
