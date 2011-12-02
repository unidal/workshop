package com.site.wdbc;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DefaultWdbcResult implements WdbcResult {
   private List<Object[]> m_table = new ArrayList<Object[]>();

   private String[] m_columns;

   private Object[] m_values;

   public void addValue(int col, Object value) {
      if (col < 0 || col > m_columns.length) {
         throw new IllegalArgumentException("Internal error: invalid col: " + col + ", columns: " + m_columns.length);
      }

      if (m_values[col] != null) {
         moveToNext();
      }

      m_values[col] = value;
   }

   public void addValue(String colName, Object value) {
      int col = getColIndex(colName);

      addValue(col, value);
   }

   public void applyLastRow() {
      if (m_values != null) {
         for (Object value : m_values) {
            if (value != null) {
               m_table.add(m_values);
               m_values = new Object[m_columns.length];
               break;
            }
         }
      }
   }

   public void begin(String[] columns) {
      m_columns = columns;
      m_values = new Object[columns.length];
   }

   public void clear() {
      m_table.clear();
      m_values = new Object[m_columns.length];
   }

   private String escape(String value) {
      if (false || value == null || value.length() == 0) {
         return value;
      }

      int len = value.length();
      StringBuilder sb = new StringBuilder(len * 2);

      for (int i = 0; i < len; i++) {
         char ch = value.charAt(i);

         if (ch == '\r') {
            sb.append("\\r");
         } else if (ch == '\n') {
            sb.append("\\n");
         } else if (ch == '\t') {
            sb.append("\\t");
         } else {
            sb.append(ch);
         }
      }

      return sb.toString();
   }

   public boolean getBoolean(int row, String colName, boolean defaultValue) {
      Object cell = getCell(row, colName);

      try {
         if (cell != null) {
            if (cell instanceof Number) {
               Number num = (Number) cell;

               return num.intValue() > 0;
            } else {
               String str = cell.toString();

               if ("false".equals(str)) {
                  return false;
               }
            }

            return true;
         }
      } catch (Exception e) {
         // ignore it
      }

      return defaultValue;
   }

   private int getByteLength(String value) {
      try {
         return value.getBytes("GBK").length;
      } catch (UnsupportedEncodingException e) {
         return value.length();
      }
   }

   public Object getCell(int row, int col) {
      return trim(m_table.get(row)[col]);
   }

   public Object getCell(int row, String colName) {
      int col = getColIndex(colName);

      return getCell(row, col);
   }

   public int getColIndex(String colName) {
      for (int i = 0; i < m_columns.length; i++) {
         String column = m_columns[i];

         if (column.equals(colName)) {
            return i;
         }
      }

      throw new IllegalArgumentException("Column(" + colName + ") is not defined in the field list");
   }

   public String[] getColumns() {
      return m_columns;
   }

   public int getColumnSize() {
      return m_columns.length;
   }

   public Date getDate(int row, String colName, Date defaultValue, String format) {
      Object cell = getCell(row, colName);

      try {
         if (cell != null) {
            return new SimpleDateFormat(format).parse(cell.toString());
         }
      } catch (Exception e) {
         // ignore it
      }

      return defaultValue;
   }

   public double getDouble(int row, String colName, double defaultValue) {
      Object cell = getCell(row, colName);

      try {
         if (cell != null) {
            return Double.parseDouble(cell.toString());
         }
      } catch (Exception e) {
         // ignore it
      }

      return defaultValue;
   }

   public int getInt(int row, String colName, int defaultValue) {
      Object cell = getCell(row, colName);

      try {
         if (cell != null) {
            return Integer.parseInt(cell.toString());
         }
      } catch (Exception e) {
         // ignore it
      }

      return defaultValue;
   }

   public long getLong(int row, String colName, long defaultValue) {
      Object cell = getCell(row, colName);

      try {
         if (cell != null) {
            return Long.parseLong(cell.toString());
         }
      } catch (Exception e) {
         // ignore it
      }

      return defaultValue;
   }

   public int getRowSize() {
      return m_table.size();
   }

   public String getString(int row, String colName) {
      Object cell = getCell(row, colName);

      return cell == null ? null : cell.toString();
   }

   public void insertRow(int row, Object[] values) {
      Object[] data = new Object[m_columns.length];

      System.arraycopy(values, 0, data, 0, Math.min(m_columns.length, values.length));

      m_table.add(row, data);
   }

   private void moveToNext() {
      boolean isEmpty = true;
      for (Object value : m_values) {
         if (value != null) {
            isEmpty = false;
            break;
         }
      }

      if (!isEmpty) {
         m_table.add(m_values);
      }

      m_values = new Object[m_columns.length];
   }

   private void pad(StringBuilder sb, String text, int maxWidth, char fillChar) {
      int len = (text == null ? 0 : getByteLength(text));

      if (text != null) {
         sb.append(text);
      }

      for (int i = len; i < maxWidth; i++) {
         sb.append(fillChar);
      }
   }

   public void removeRow(int row) {
      m_table.remove(row);
   }

   public void setValue(int row, int col, Object value) {
      m_table.get(row)[col] = value;
   }

   public void setValue(int row, String colName, Object value) {
      int col = getColIndex(colName);

      m_table.get(row)[col] = value;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder(4096);
      int cols = m_columns.length;
      int rows = m_table.size();
      int[] maxWidths = new int[cols];

      for (int i = 0; i < cols; i++) {
         int width = m_columns[i].length();

         for (int j = 0; j < rows; j++) {
            Object cell = m_table.get(j)[i];

            if (cell != null) {
               String value = escape(String.valueOf(cell));
               int len = getByteLength(value);

               if (len > width) {
                  width = len;
               }
            }
         }

         maxWidths[i] = width;
      }

      sb.append("DefaultWdbcResult: ").append(rows).append(" rows, ").append(cols).append(" columns\r\n");

      // headers
      for (int i = 0; i < cols; i++) {
         if (i > 0) {
            sb.append(" | ");
         }

         pad(sb, m_columns[i], maxWidths[i], ' ');
      }

      sb.append("\r\n");

      // separate line
      for (int i = 0; i < cols; i++) {
         if (i > 0) {
            sb.append(" + ");
         }

         pad(sb, null, maxWidths[i], '-');
      }

      sb.append("\r\n");

      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < cols; j++) {
            Object cell = m_table.get(i)[j];
            String value = (cell == null ? null : String.valueOf(cell));

            if (j > 0) {
               sb.append(" | ");
            }

            pad(sb, escape(value), maxWidths[j], ' ');
         }

         sb.append("\r\n");
      }

      return sb.toString();
   }

   private Object trim(Object str) {
      return str != null && str instanceof String ? ((String) str).trim() : str;
   }
}
