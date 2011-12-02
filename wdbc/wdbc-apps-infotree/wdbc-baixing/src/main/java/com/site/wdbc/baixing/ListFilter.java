package com.site.wdbc.baixing;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.http.Session;
import com.site.wdbc.query.DefaultWdbcFilter;

public class ListFilter extends DefaultWdbcFilter {
   private Session m_session;

   private SimpleDateFormat m_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

      if (str.endsWith("前")) {
         if (hour != 0) {
            cal.add(Calendar.HOUR_OF_DAY, -hour);
         }

         if (minute != 0) {
            cal.add(Calendar.MINUTE, -minute);
         }

         if (second != 0) {
            cal.add(Calendar.SECOND, -second);
         }
      } else {
         if (year > 0) {
            cal.set(Calendar.YEAR, year > 100 ? year : 2000 + year);
         }

         if (month > 0) {
            cal.set(Calendar.MONTH, month - 1);
         }

         if (day > 0) {
            cal.set(Calendar.DAY_OF_MONTH, day);
         }

         cal.set(Calendar.HOUR_OF_DAY, 0);
         cal.set(Calendar.MINUTE, 0);
         cal.set(Calendar.SECOND, 0);
      }

      return cal.getTime();
   }

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String title = (String) result.getCell(row, "title");
      String link = (String) result.getCell(row, "link");
      String date = (String) result.getCell(row, "date");

      if (link == null || title == null || date == null) {
         return true;
      } else {
         Date time = getTime(date);

         result.setValue(row, "date", m_date.format(time));

         try {
            if (m_session != null) {
               link = new URL(m_session.getLastUrl(), link).toExternalForm();
            }
         } catch (Exception e) {
            // ignore it
         }

         int pos1 = link.lastIndexOf('/');
         int pos2 = link.lastIndexOf('.');

         if (pos1 > 0 && pos2 > pos1) {
            result.setValue(row, "id", link.substring(pos1 + 1, pos2));
         } else {
            return true;
         }

         result.setValue(row, "link", link);
         return false;
      }
   }
}
