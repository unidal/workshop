package com.site.wdbc.http.impl;

import com.site.wdbc.http.Processor;
import com.site.wdbc.http.Session;

public class DefaultProcessor implements Processor {
   public void execute(Session session) {
      System.out.println(session.getProperties());
   }
}
