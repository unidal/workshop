package org.unidal.ezsell.api.paypal;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class PaypalApiResult {
   private Map<String, String> m_properties = new LinkedHashMap<String, String>();

   private Map<Integer, Map<String, String>> m_table = new LinkedHashMap<Integer, Map<String, String>>();

   private Date m_timestamp;

   private String m_ack;

   private String m_corelationId;

   private String m_version;

   private int m_build;

   public PaypalApiResult(String content) {
      parse(content);
   }

   public String getAck() {
      return m_ack;
   }

   public boolean getBoolean(int row, String colName, boolean defaultValue) {
      String cell = getCell(row, colName);

      if (cell != null) {
         if ("false".equalsIgnoreCase(cell)) {
            return false;
         }

         return true;
      }

      return defaultValue;
   }

   public boolean getBooleanProperty(String name, boolean defaultValue) {
      String cell = getProperty(name);

      if (cell != null) {
         if ("false".equalsIgnoreCase(cell)) {
            return false;
         }

         return true;
      }

      return defaultValue;
   }

   public int getBuild() {
      return m_build;
   }

   private String getCell(int row, String colName) {
      if (row >= 0 && row < m_table.size()) {
         String cell = m_table.get(row).get(colName.toUpperCase());

         if (cell == null) {
            throw new RuntimeException("Column(" + colName + ") is not found.");
         }

         return cell;
      }

      throw new IndexOutOfBoundsException("Row: " + row + ", expected: [0," + m_table.size() + ").");
   }

   public String getCorelationId() {
      return m_corelationId;
   }

   public Date getDate(int row, String colName, Date defaultValue, String format) {
      String cell = getCell(row, colName);

      try {
         if (cell != null) {
            return new SimpleDateFormat(format).parse(cell);
         }
      } catch (Exception e) {
         // ignore it
      }

      return defaultValue;
   }

   public Date getDateProperty(String name, Date defaultValue, String format) {
      String cell = getProperty(name);

      try {
         if (cell != null) {
            return new SimpleDateFormat(format).parse(cell);
         }
      } catch (Exception e) {
         // ignore it
      }

      return defaultValue;
   }

   public double getDouble(int row, String colName, double defaultValue) {
      String cell = getCell(row, colName);

      try {
         if (cell != null) {
            return Double.parseDouble(cell);
         }
      } catch (Exception e) {
         // ignore it
      }

      return defaultValue;
   }

   public double getDoubleProperty(String name, double defaultValue) {
      String cell = getProperty(name);

      try {
         if (cell != null) {
            return Double.parseDouble(cell);
         }
      } catch (Exception e) {
         // ignore it
      }

      return defaultValue;
   }

   public int getInt(int row, String colName, int defaultValue) {
      String cell = getCell(row, colName);

      try {
         if (cell != null) {
            return Integer.parseInt(cell);
         }
      } catch (Exception e) {
         // ignore it
      }

      return defaultValue;
   }

   public int getIntProperty(String name, int defaultValue) {
      String cell = getProperty(name);

      try {
         if (cell != null) {
            return Integer.parseInt(cell);
         }
      } catch (Exception e) {
         // ignore it
      }

      return defaultValue;
   }

   public long getLong(int row, String colName, long defaultValue) {
      String cell = getCell(row, colName);

      try {
         if (cell != null) {
            return Long.parseLong(cell);
         }
      } catch (Exception e) {
         // ignore it
      }

      return defaultValue;
   }

   public long getLongProperty(String name, long defaultValue) {
      String cell = getProperty(name);

      try {
         if (cell != null) {
            return Long.parseLong(cell);
         }
      } catch (Exception e) {
         // ignore it
      }

      return defaultValue;
   }

   public Map<String, String> getProperties() {
      return m_properties;
   }

   public List<String> getColumnNames() {
      List<String> names = new ArrayList<String>();

      for (Map<String, String> value : m_table.values()) {
         names.addAll(value.keySet());
         break;
      }

      return names;
   }

   public String getProperty(String name) {
      String upperName = name.toUpperCase();

      return m_properties.get(upperName);
   }

   public int getRowSize() {
      return m_table.size();
   }

   public String getString(int row, String colName) {
      String cell = getCell(row, colName);

      return cell;
   }

   public String getStringProperty(String name) {
      return getProperty(name);
   }

   public Date getTimestamp() {
      return m_timestamp;
   }

   public String getVersion() {
      return m_version;
   }

   private void parse(String content) {
      String[] parts = content.split(Pattern.quote("&"));

      for (String part : parts) {
         int index = part.indexOf('=');

         if (index < 0) {
            throw new IllegalArgumentException("Unparsable API result: " + content);
         }

         String name = part.substring(0, index);
         String value = part.substring(index + 1);

         if (name.startsWith("L_")) {
            int len = name.length();
            char ch1 = name.charAt(len - 1);
            char ch2 = name.charAt(len - 2);

            if (Character.isDigit(ch1) && Character.isDigit(ch2)) {
               int row = (ch2 - '0') * 10 + (ch1 - '0');

               setCell(row, name.substring(2, len - 2), urlDecode(value));
            } else if (Character.isDigit(ch1)) {
               int row = (ch1 - '0');

               setCell(row, name.substring(2, len - 1), urlDecode(value));
            } else {
               throw new RuntimeException("Invalid parameter name: " + name);
            }
         } else {
            setProperty(name, urlDecode(value));
         }
      }
   }

   private void setCell(int row, String colName, String value) {
      Map<String, String> map = m_table.get(row);

      if (map == null) {
         map = new HashMap<String, String>();
         m_table.put(row, map);
      }

      map.put(colName, value);
   }

   private void setProperty(String name, String value) {
      if ("Ack".equalsIgnoreCase(name)) {
         m_ack = value;
      } else if ("Version".equalsIgnoreCase(name)) {
         m_version = value;
      } else if ("CorrelationId".equalsIgnoreCase(name)) {
         m_corelationId = value;
      } else if ("Build".equalsIgnoreCase(name)) {
         m_build = Integer.parseInt(value);
      } else if ("Timestamp".equalsIgnoreCase(name)) {
         try {
            m_timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(value);
         } catch (ParseException e) {
            throw new RuntimeException("Unable to parse timestamp: " + value, e);
         }
      }

      m_properties.put(name, value);
   }

   private String urlDecode(String value) {
      try {
         return URLDecoder.decode(value, "utf-8");
      } catch (UnsupportedEncodingException e) {
         throw new RuntimeException("Error when doing url decode for: " + value, e);
      }
   }
}
