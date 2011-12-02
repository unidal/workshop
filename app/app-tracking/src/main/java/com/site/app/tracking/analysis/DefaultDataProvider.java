package com.site.app.tracking.analysis;

import javax.servlet.http.HttpServletRequest;

import com.site.app.DataProvider;

public class DefaultDataProvider implements DataProvider<Field> {
   private HttpServletRequest m_req;

   public DefaultDataProvider(HttpServletRequest req) {
      m_req = req;
   }

   public Object getValue(Field field) {
      return m_req.getParameter(field.getName());
   }
}
