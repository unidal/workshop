package com.site.dal.xml.formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.site.dal.xml.XmlException;

public class DateFormatter implements Formatter<Date> {
   private static final InheritableThreadLocal<Manager> MANAGER = new InheritableThreadLocal<Manager>() {
      @Override
      protected Manager initialValue() {
         return new Manager();
      }
   };

   public Date parse(String format, String text) throws XmlException {
      SimpleDateFormat dateFormat = MANAGER.get().getDateFormat(format);

      try {
         return dateFormat.parse(text.trim());
      } catch (ParseException e) {
         throw new XmlException("Error when parsing date(" + dateFormat.toPattern() + ") from " + text, e);
      }
   }

   public String format(String format, Date object) throws XmlException {
      SimpleDateFormat dateFormat = MANAGER.get().getDateFormat(format);

      return dateFormat.format(object);
   }

   static final class Manager {
      private Map<String, SimpleDateFormat> m_map = new HashMap<String, SimpleDateFormat>();

      public SimpleDateFormat getDateFormat(String format) {
         SimpleDateFormat dateFormat = m_map.get(format);

         if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(format, Locale.US);
            m_map.put(format, dateFormat);
         }

         return dateFormat;
      }
   }
}
