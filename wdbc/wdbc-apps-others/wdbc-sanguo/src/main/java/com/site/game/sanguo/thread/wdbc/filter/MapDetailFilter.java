package com.site.game.sanguo.thread.wdbc.filter;

import java.text.MessageFormat;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class MapDetailFilter extends DefaultWdbcFilter {
   private MessageFormat m_format = new MessageFormat("{0}势　力：{1} 联　盟： 君　主： 居　民：{2} 爵　位：{3}");

   @Override
   public void doFilter(WdbcResult result) {
      aggregate(result, " ");

      super.doFilter(result);
   }

   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String village = (String) result.getCell(row, "village");
      String tribe = (String) result.getCell(row, "tribe");

      if (village != null && tribe != null) {
         try {
            Object[] parts = m_format.parse(tribe);

            result.setValue(row, "village", village.split(" ")[0]);
            result.setValue(row, "tribe", parts[1]);
            result.setValue(row, "population", parts[2]);

            return false;
         } catch (Exception e) {
            // ignore it
         }
      }

      return true;
   }
}
