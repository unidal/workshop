package com.site.wdbc.ebay.arch;

import com.site.lookup.annotation.Inject;
import com.site.wdbc.http.Processor;
import com.site.wdbc.http.Session;

public class CourseProcessor implements Processor {
   @Inject
   private ResultHandler m_handler;

   public void execute(Session session) {
      String title = session.getProperties().get("course:title");

      m_handler.handleCourse(title);
   }
}
