package org.unidal.weather.biz;

public class Weather {
   private String m_dayInString;

   private int m_current;

   private int m_high;

   private int m_low;

   private int m_code;

   public Weather(String dayInString, int code, int high, int low) {
      m_dayInString = dayInString;
      m_code = code;
      m_high = high;
      m_low = low;
   }

   public String getDayInString() {
      return m_dayInString;
   }

   public int getCurrent() {
      return m_current;
   }

   public int getHigh() {
      return m_high;
   }

   public int getLow() {
      return m_low;
   }

   public int getCode() {
      return m_code;
   }

   public Weather setCurrent(int current) {
      m_current = current;
      return this;
   }
}
