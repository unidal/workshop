package com.site.wdbc.kijiji;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.WdbcFilter;

public class DetailsFilter implements WdbcFilter {
   private SimpleDateFormat m_date = new SimpleDateFormat("发布时间：yyyy年M月d日 HH:mm");

   private SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   public void doFilter(WdbcResult result) {
      int rows = result.getRowSize();
      StringBuffer category = new StringBuffer();
      StringBuffer district = new StringBuffer();
      StringBuffer body = new StringBuffer(4096);
      String categoryId = null;
      String districtId = null;
      Date date = null;

      for (int i = 0; i < rows; i++) {
         String label = (String) result.getCell(i, "label");
         String text = (String) result.getCell(i, "text");
         String link = (String) result.getCell(i, "link");
         String content = (String) result.getCell(i, "content");

         if (link != null) {
            if (link.startsWith("/c")) {
               categoryId = link.substring(2);

               if (category.length() > 0) {
                  category.append(':');
               }

               category.append(text);
            } else if (link.startsWith("/d")) {
               districtId = link.substring(2);

               if (district.length() > 0) {
                  district.append(':');
               }

               district.append(text);
            }
         } else {
            if (label != null && label.length() > 0) {
               try {
                  date = m_date.parse(label);
               } catch (ParseException e) {
                  System.err.println(e.toString());
               }
            }

            if (content != null && !content.startsWith("联络时请说明来自客齐集，以获得更好效果。")
                  && !content.startsWith("#viewRightAdsense")) {
               body.append(content).append("\r\n");
            }
         }
      }

      result.clear();
      result.addValue("categoryId", categoryId);
      result.addValue("categoryName", category.toString());
      result.addValue("districtId", districtId);
      result.addValue("districtName", district.toString());
      result.addValue("date", date != null ? m_format.format(date) : null);
      result.addValue("body", body.toString());
      result.applyLastRow();
   }
}
