package com.site.wdbc.whycools;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.http.Session;
import com.site.wdbc.query.DefaultWdbcFilter;

public class ListFilter extends DefaultWdbcFilter implements Initializable {
   private Session m_session;

   private Configuration m_configuration;

   private SimpleDateFormat m_date = new SimpleDateFormat("(yyyy-MM-dd)");

   private SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   private int m_maxDays;

   private Date getTime(String str) {
      try {
         Date date = m_date.parse(str);

         return date;
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
      String title1 = (String) result.getCell(row, "title1");
      String title2 = (String) result.getCell(row, "title2");
      String link = (String) result.getCell(row, "link");

      if (link == null || title1 == null || title2 == null || date == null) {
         return true;
      } else {
         Date time = getTime(date.trim());

         if (time == null) {
            return true;
         }

         Calendar cal = Calendar.getInstance();
         String type = m_configuration.getType();

         cal.add(Calendar.DATE, -m_maxDays);
         result.setValue(row, "date", m_format.format(time));
         result.setValue(row, "title", "出发:" + title1 + "  到达:" + title2);
         result.setValue(row, "link", getAbsoluteUrl(link));
         result.setValue(row, "type", type);

         if (time.after(cal.getTime())) {
            return false;
         } else {
            return true;
         }
      }
   }

   private String getAbsoluteUrl(String link) {
      URL lastUrl = m_session.getLastUrl();

      try {
         return new URL(lastUrl, link).toExternalForm();
      } catch (MalformedURLException e) {
         return link;
      }
   }
}
