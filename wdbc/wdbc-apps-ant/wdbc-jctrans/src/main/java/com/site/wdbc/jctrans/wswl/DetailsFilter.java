package com.site.wdbc.jctrans.wswl;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class DetailsFilter extends DefaultWdbcFilter {

   private SimpleDateFormat m_from = new SimpleDateFormat("yyyy-M-d HH:mm:ss");

   private SimpleDateFormat m_output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   @Override
   public void doFilter(WdbcResult result) {
      String date = aggregate(result, "date", null);
      String from = aggregate(result, "from", null);
      String body = aggregate(result, "body", null);

      try {
         date = m_output.format(m_from.parse(date));
      } catch (ParseException e) {
         // ignore it
      }

      result.clear();
      result.addValue("date", date);
      result.addValue("from", from);
      result.addValue("body", body);
      result.applyLastRow();
   }
}
