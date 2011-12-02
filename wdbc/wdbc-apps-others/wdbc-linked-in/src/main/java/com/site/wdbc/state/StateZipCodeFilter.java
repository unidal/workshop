package com.site.wdbc.state;

import java.util.Map;
import java.util.TreeMap;

import com.site.wdbc.WdbcResult;
import com.site.wdbc.query.DefaultWdbcFilter;

public class StateZipCodeFilter extends DefaultWdbcFilter {
   @Override
   protected boolean shouldRemoveRow(WdbcResult result, int row) {
      String text = (String) result.getCell(row, "text");
      String[] lines = text.split("\n");
      Map<String, String> items = new TreeMap<String, String>();

      result.removeRow(row);

      for (String line : lines) {
         String[] parts = line.split(" ");

         items.put(parts[0], parts[2]);
      }

      for (Map.Entry<String, String> item : items.entrySet()) {
         result.addValue(0, item.getKey());
         result.addValue(1, item.getValue());
      }

      return false;
   }
}
