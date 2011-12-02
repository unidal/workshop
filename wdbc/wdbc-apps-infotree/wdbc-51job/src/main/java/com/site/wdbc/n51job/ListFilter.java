package com.site.wdbc.n51job;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class ListFilter extends DefaultWdbcFilter implements Initializable {
   private Configuration m_configuration;

   private SimpleDateFormat m_date = new SimpleDateFormat("yyyy-MM-dd");

   private SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   private int m_maxDays;

   private MessageFormat m_linkPattern = new MessageFormat(
         "http://search.51job.com/jobsearch/show_job_detail.php?id={0}");

   @Override
   public void doFilter(WdbcResult result) {
      int rowSize = result.getRowSize();

      for (int i = rowSize - 1; i >= 0; i -= 2) {
         if (shouldRemoveRow(result, i)) {
            result.removeRow(i);
            result.removeRow(i - 1);
         } else {
            String link = (String) result.getCell(i - 1, "link");
            int index = link.indexOf('(');
            link = (index >= 0 ? link.substring(index) : link);

            result.setValue(i, "link", m_linkPattern.format(new Object[] { link }));
            result.removeRow(i - 1);
         }
      }
   }

   private Date getTime(String str) {
      if (str.charAt(0) == 160) {
         str = str.substring(1);
      }

      try {
         Date date = m_date.parse(str);
         Calendar cal = Calendar.getInstance();
         int year = cal.get(Calendar.YEAR);
         int month1 = cal.get(Calendar.MONTH);

         cal.setTime(date);

         int month2 = cal.get(Calendar.MONTH);

         if (month2 <= month1) {
            cal.set(Calendar.YEAR, year);
         } else {
            cal.set(Calendar.YEAR, year - 1);
         }

         return cal.getTime();
      } catch (ParseException e) {
         // ignore it
      }

      return null;
   }

   public void initialize() throws InitializationException {
      m_maxDays = m_maxDays > 0 ? m_maxDays : m_configuration.getMaxDays();
   }

   public void setMaxDays(int maxDays) {
      m_maxDays = maxDays;
   }

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String date = (String) result.getCell(row, "date");
      String link = (String) result.getCell(row - 1, "link");

      if (link == null || date == null) {
         return true;
      } else {
         Date time = getTime(date.trim());

         if (time == null) {
            return true;
         }

         Calendar cal = Calendar.getInstance();

         cal.add(Calendar.DATE, -m_maxDays);
         result.setValue(row, "date", m_format.format(time));
         result.setValue(row, "type", "1");

         if (time.after(cal.getTime())) {
            return false;
         } else {
            return true;
         }
      }
   }
}
