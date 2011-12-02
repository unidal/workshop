package com.site.game.sanguo.thread.wdbc.filter;

import java.text.MessageFormat;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class BuildingDetailFilter extends DefaultWdbcFilter {
   private MessageFormat m_format = new MessageFormat("{0} +{1}    商业值 {2} / 天");

   @Override
   public void doFilter(WdbcResult result) {
      aggregate(result, " ");

      super.doFilter(result);
   }

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String lumber = (String) result.getCell(row, "lumber");
      String population = (String) result.getCell(row, "population");

      if (lumber == null || population == null) {
         return true;
      } else {
         try {
            String[] parts = lumber.split(" ");
            Object[] items = m_format.parse(population);

            result.setValue(row, "lumber", parts[0]);
            result.setValue(row, "clay", parts[1]);
            result.setValue(row, "iron", parts[2]);
            result.setValue(row, "crop", parts[3]);
            result.setValue(row, "population", items[1]);

            return false;
         } catch (Exception e) {
            // ignore it
         }

         return true;
      }
   }
}
