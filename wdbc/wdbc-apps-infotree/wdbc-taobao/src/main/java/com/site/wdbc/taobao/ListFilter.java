package com.site.wdbc.taobao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class ListFilter extends DefaultWdbcFilter implements Initializable {
   private Configuration m_configuration;

   private SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   private int m_minHours;

   private int m_maxDays;

   @Override
   public void doFilter(WdbcResult result) {
      doPreFilter(result);

      super.doFilter(result);
   }

   private void doPreFilter(WdbcResult result) {
      int rowSize = result.getRowSize();
      String date = null;
      String title = null;
      String link = null;
      int dateIndex = 0;
      int titleIndex = 0;
      int linkIndex = 0;

      for (int row = 0; row < rowSize; row++) {
         date = (String) result.getCell(row, "date");
         title = (String) result.getCell(row, "title");
         link = (String) result.getCell(row, "link");

         if (date != null && date.startsWith("剩余")) {
            result.setValue(dateIndex++, "date", date.substring(2));
         }

         if (title != null && title.length() > 0) {
            result.setValue(titleIndex++, "title", title);
         }

         if (title != null && link.startsWith("http://auction1.taobao.com/auction")) {
            result.setValue(linkIndex++, "link", link);
         }
      }

      int rows = Math.max(Math.max(dateIndex, titleIndex), linkIndex);
      for (int row = rowSize - 1; row >= rows; row--) {
         result.removeRow(row);
      }
   }

   private Date getTime(String str) {
      int year = 0;
      int month = 0;
      int day = 0;
      int hour = 0;
      int minute = 0;
      int second = 0;

      StringBuffer nums = new StringBuffer();
      StringBuffer unit = new StringBuffer();
      boolean inNumber = false;

      int len = str.length();
      for (int i = 0; i <= len; i++) {
         char ch = (i < len ? str.charAt(i) : '0');

         if (Character.isDigit(ch)) {
            if (!inNumber && nums.length() > 0 && unit.length() > 0) {
               String data = unit.toString();
               int value = Integer.parseInt(nums.toString());

               if (data.startsWith("年")) {
                  year = value;
               } else if (data.startsWith("月")) {
                  month = value;
               } else if (data.startsWith("日")) {
                  day = value;
               } else if (data.startsWith("小时")) {
                  hour = value;
               } else if (data.startsWith("分钟")) {
                  minute = value;
               } else if (data.startsWith("秒")) {
                  second = value;
               } else {
                  System.err.println("Unknown data unit: " + data);
               }

               nums.setLength(0);
               unit.setLength(0);
            }

            if (i < len) {
               nums.append(ch);
            }

            inNumber = true;
         } else {
            inNumber = false;
            unit.append(ch);
         }
      }

      Calendar cal = Calendar.getInstance();

      if (year != 0) {
         cal.add(Calendar.YEAR, -year);
      }

      if (month != 0) {
         cal.add(Calendar.MONTH, -month);
      }

      if (day != 0) {
         cal.add(Calendar.DATE, -day);
      }

      if (hour != 0) {
         cal.add(Calendar.HOUR_OF_DAY, -hour);
      }

      if (minute != 0) {
         cal.add(Calendar.MINUTE, -minute);
      }

      if (second != 0) {
         cal.add(Calendar.SECOND, -second);
      }

      return cal.getTime();
   }

   public void initialize() throws InitializationException {
      m_minHours = m_minHours > 0 ? m_minHours : m_configuration.getMinHours();
      m_maxDays = m_maxDays > 0 ? m_maxDays : m_configuration.getMaxDays();
   }

   public void setMinHours(int minHours) {
      m_minHours = minHours;
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

         Calendar min = Calendar.getInstance();
         Calendar max = Calendar.getInstance();

         min.add(Calendar.HOUR, -m_minHours);
         max.add(Calendar.DATE, -m_maxDays);
         result.setValue(row, "date", m_format.format(time));
         result.setValue(row, "type", "1");

         if (time.before(min.getTime()) && time.after(max.getTime())) {
            return false;
         } else {
            return true;
         }
      }
   }
}
