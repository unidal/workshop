package com.site.web.jsp.function;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Date;

public class Format {
   public static String format(Object data, String pattern) {
      if (pattern == null || pattern.length() == 0) {
         return String.valueOf(data);
      } else if (data == null) {
         return "";
      }

      StringBuilder sb = new StringBuilder(32);

      sb.append("{0");

      if (pattern.startsWith("number") || pattern.startsWith("date") || pattern.startsWith("choice")) {
         sb.append(',').append(pattern);
      } else if (data instanceof Date) {
         sb.append(",date,").append(pattern);
      } else if (data instanceof Number) {
         sb.append(",number,").append(pattern);
      }

      sb.append('}');

      MessageFormat format = new MessageFormat(sb.toString());

      return format.format(new Object[] { data });
   }

   public static String percentage(int divisor, int divident, int precision) {
      if (divident == 0) {
         return "N/A";
      } else {
         StringBuilder sb = new StringBuilder();

         sb.append('0');

         if (precision > 0) {
            sb.append('.');
            for (int i = 0; i < precision; i++) {
               sb.append('0');
            }
         }

         sb.append('%');

         return new DecimalFormat(sb.toString()).format(1.0d * divisor / divident);
      }
   }

   public static String shorten(String str, int maxLength) {
      int len = (str == null ? 0 : str.length());

      if (len < maxLength || len <= 3) {
         return str;
      }

      StringBuilder sb = new StringBuilder(maxLength);
      String dots = "...";
      int dotsLen = dots.length();
      int left = (maxLength - dotsLen) / 2;

      sb.append(str.substring(0, left));
      sb.append(dots);
      sb.append(str.substring(len - maxLength + left + dotsLen));

      return sb.toString();
   }
}
