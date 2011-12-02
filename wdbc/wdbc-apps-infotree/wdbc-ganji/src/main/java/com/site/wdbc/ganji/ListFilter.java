package com.site.wdbc.ganji;

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
   
   private SimpleDateFormat m_date = new SimpleDateFormat("MM-dd HH:mm");

   private SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   private MessageFormat m_linkFormat = new MessageFormat("http://{0}.ganji.com{1}");

   private int m_maxDays;

   private String m_city;

   private Date getTime(String str) {
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
      m_city = m_configuration.getCityId();
   }

   public void setMaxDays(int maxDays) {
      m_maxDays = maxDays;
   }

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String date = (String) result.getCell(row, "date");
      String title = (String) result.getCell(row, "title");
      String link = (String) result.getCell(row, "link");

      if (link == null || title == null || date == null) {
         return true;
      } else {
         Date time = getTime(date.trim());

         if (time == null) {
            return true;
         }

         Calendar cal = Calendar.getInstance();

         cal.add(Calendar.DATE, -m_maxDays);
         result.setValue(row, "date", m_format.format(time));

         if (link.startsWith("/")) {
            result.setValue(row, "link", m_linkFormat.format(new Object[] { m_city, link }));
         }

         if (time.after(cal.getTime())) {
            return false;
         } else {
            return true;
         }
      }
   }
}
