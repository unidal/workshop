package com.site.wdbc.n8j;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class DetailsFilter extends DefaultWdbcFilter {
   @Override
   public void doFilter(WdbcResult result) {
      String desc = getDesc(result);
      String pictureLink = getPictureLink(result);
      String contact = aggregate(result, "contact", null);
      String district1 = aggregate(result, "district-1", null);
      String district2 = aggregate(result, "district-2", null);

      result.clear();
      result.addValue("desc", desc);
      result.addValue("contact", contact);
      result.addValue("district", district1 + " " + district2);
      result.addValue("picture-link", pictureLink);
      result.applyLastRow();
   }

   private String getPictureLink(WdbcResult result) {
      int rowSize = result.getRowSize();

      for (int i = 0; i < rowSize; i++) {
         String pictureLink = (String) result.getCell(i, "picture-link");

         if (pictureLink != null && pictureLink.length() > 0) {
            return pictureLink;
         }
      }

      return null;
   }

   private String getDesc(WdbcResult result) {
      int rowSize = result.getRowSize();
      StringBuilder sb = new StringBuilder(1024);
      String header = null;

      for (int i = 0; i < rowSize; i++) {
         String head = (String) result.getCell(i, "head");
         String desc = (String) result.getCell(i, "desc");

         if (head != null && (head.equals("营业时间：") || head.equals("简介：") || head.equals("开业时间："))) {
            header = head;
            sb.append(head).append("\r\n");
         }

         if (desc != null && desc.length() > 0 && header != null) {
            sb.append(desc).append("\r\n");
            header = null;
         }
      }

      return sb.toString();
   }
}
