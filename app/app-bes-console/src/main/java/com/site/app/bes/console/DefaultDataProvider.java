package com.site.app.bes.console;

import javax.servlet.http.HttpServletRequest;

import com.site.app.DataProvider;

public class DefaultDataProvider implements DataProvider<Field> {
   private HttpServletRequest m_req;

   public DefaultDataProvider(HttpServletRequest req) {
      m_req = req;
   }

   public Object getValue(Field field) {
      if (m_req != null) {
         return m_req.getParameter(field.getName());
      } else {
         return null;
      }
   }
}
