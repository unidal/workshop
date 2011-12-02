package com.site.wdbc;

import java.util.Date;

public interface WdbcResult {
   public void addValue(int col, Object value);

   public void addValue(String colName, Object value);

   public void applyLastRow();

   public void begin(String[] columns);

   public void clear();

   public boolean getBoolean(int row, String colName, boolean defaultValue);

   public Object getCell(int row, int col);

   public Object getCell(int row, String colName);

   public String[] getColumns();

   public int getColumnSize();
   
   public Date getDate(int row, String colName, Date defaultValue, String format);
   
   public double getDouble(int row, String colName, double defaultValue);

   public int getInt(int row, String colName, int defaultValue);

   public long getLong(int row, String colName, long defaultValue);

   public int getRowSize();

   public String getString(int row, String colName);

   public void insertRow(int row, Object[] values);

   public void removeRow(int row);

   public void setValue(int row, int col, Object value);

   public void setValue(int row, String colName, Object value);
}
