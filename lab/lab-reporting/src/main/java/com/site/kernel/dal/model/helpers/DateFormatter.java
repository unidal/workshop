package com.site.kernel.dal.model.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.site.kernel.dal.model.common.Event;
import com.site.kernel.dal.model.common.FormatingException;
import com.site.kernel.dal.model.common.Formatter;
import com.site.kernel.dal.model.common.ParsingException;

public class DateFormatter implements Formatter {
   public static final DateFormatter DEFAULT = new DateFormatter("yyyy-MM-dd hh:mm:ss");

   private SimpleDateFormat m_format;

   private StringBuffer m_text;

   private DateFormatter(String format) {
      m_format = new SimpleDateFormat(format);
      m_text = new StringBuffer(32);
   }

   public String format(Object value, boolean indented) throws FormatingException {
      if (!(value instanceof Date)) {
         throw new FormatingException("Date type parameter expected: " + value);
      }

      return m_format.format((Date) value);
   }

   public void handleEnd(FormatterContext context, Event e) throws ParsingException {
      String text = m_text.toString().trim();

      try {
         context.setFormatResult(m_format.parse(text));
      } catch (ParseException pe) {
         throw new ParsingException("Can't parse using format: " + m_format.toPattern() + " for " + text, pe);
      }
   }

   public void handleStart(FormatterContext context, Event e) throws ParsingException {
      m_text.setLength(0);
   }

   public void handleText(FormatterContext context, Event e) throws ParsingException {
      m_text.append(e.getText());
   }

   public boolean ignoreWhiteSpaces() {
      return true;
   }
}
