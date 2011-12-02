package com.site.wdbc.sosoko;

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
   private Configuration m_configuration;

   private SimpleDateFormat m_date = new SimpleDateFormat("yyyy年MM月dd日");

   private SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

   private int m_maxDays;

   private Session m_session;

   private Date getTime(String str) {
      Date date = null;

      if (str != null && str.startsWith("发布时间：")) {
         String val = str.substring("发布时间：".length());

         if (val.equals("今天")) {
            date = new Date();
         } else if (val.equals("昨天")) {
            date = new Date(System.currentTimeMillis() - 86400 * 1000L);
         } else {
            try {
               date = m_date.parse(val);
            } catch (ParseException e) {
               // ignore it
            }
         }
      }

      return date;
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
            URL lastUri = m_session.getLastUrl();

            if (lastUri != null) {
               try {
                  result.setValue(row, "link", new URL(lastUri, link).toExternalForm());
               } catch (MalformedURLException e) {
                  // ignore it
               }
            }
         }

         if (time.after(cal.getTime())) {
            return false;
         } else {
            return true;
         }
      }
   }
}
