package org.unidal.ezsell.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RowSet {
   private Map<String, Integer> m_headers = new HashMap<String, Integer>();

   private List<List<String>> m_rows;

   private int m_current;

   public RowSet(List<List<String>> rows) {
      m_rows = rows;
      m_current = 1;

      if (rows.size() > 1) {
         List<String> headers = rows.get(0);
         int index = 0;

         for (String header : headers) {
            m_headers.put(header, index++);
         }
      }
   }

   public double getDouble(String colName, double defaultValue) {
      Integer index = m_headers.get(colName);

      if (index != null) {
         List<String> row = m_rows.get(m_current);

         try {
            return Double.parseDouble(row.get(index));
         } catch (Exception e) {
            // ignore it
         }
      }

      return defaultValue;
   }

   public int getInt(String colName, int defaultValue) {
      Integer index = m_headers.get(colName);

      if (index != null) {
         List<String> row = m_rows.get(m_current);

         try {
            return Integer.parseInt(row.get(index));
         } catch (Exception e) {
            // ignore it
         }
      }

      return defaultValue;
   }

   public String getString(String colName) {
      return getString(colName, null);
   }

   public String getString(String colName, String defaultValue) {
      Integer index = m_headers.get(colName);

      if (index != null) {
         List<String> row = m_rows.get(m_current);

         return row.get(index);
      }

      return defaultValue;
   }

   public Date getLong(String colName, Date defaultValue, String format) {
      Integer index = m_headers.get(colName);

      if (index != null) {
         List<String> row = m_rows.get(m_current);

         try {
            return new SimpleDateFormat(format).parse(row.get(index));
         } catch (Exception e) {
            // ignore it
         }
      }

      return defaultValue;
   }

   public long getLong(String colName, long defaultValue) {
      Integer index = m_headers.get(colName);

      if (index != null) {
         List<String> row = m_rows.get(m_current);

         try {
            return Long.parseLong(row.get(index));
         } catch (Exception e) {
            // ignore it
         }
      }

      return defaultValue;
   }

   public boolean hasNext() {
      return m_current < m_rows.size();
   }

   public void next() {
      m_current++;
   }
}
