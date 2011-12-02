package com.site.kernel.dal.model.helpers;

import com.site.kernel.dal.model.common.Event;
import com.site.kernel.dal.model.common.FormatingException;
import com.site.kernel.dal.model.common.Formatter;
import com.site.kernel.dal.model.common.ParsingException;

public class TimeFormatter implements Formatter {
   private static final long[] FACTORS = new long[] {
                                                     Long.MAX_VALUE,
                                                     24,
                                                     60,
                                                     60 };
   private static final char[] UNITS = new char[] {
                                                   'd',
                                                   'h',
                                                   'm',
                                                   's' };

   public static final TimeFormatter DEFAULT = new TimeFormatter();

   private TimeFormatter() {
   }

   public String format(Object value, boolean indented) throws FormatingException {
      if (!(value instanceof Long)) {
         throw new FormatingException("Long type parameter expected: " + value);
      }

      long val = ((Long) value).longValue() / 1000L;
      StringBuffer sb = new StringBuffer();

      synchronized (sb) {
         for (int i = FACTORS.length - 1; val > 0 && i >= 0; i--) {
            long remainder = val % FACTORS[i];

            if (remainder > 0) {
               sb.insert(0, UNITS[i]);
               sb.insert(0, remainder);
            }

            val /= FACTORS[i];
         }

         if (sb.length() == 0) {
            sb.append('0');
         }
      }

      return sb.toString();
   }

   public void handleEnd(FormatterContext context, Event e) throws ParsingException {
      // Do nothing here
   }

   public void handleStart(FormatterContext context, Event e) throws ParsingException {
      // Do nothing here
   }

   public void handleText(FormatterContext context, Event e) throws ParsingException {
      String source = e.getText();
      long time = 0;
      int len = source.length();

      int num = 0;
      for (int i = 0; i < len; i++) {
         char ch = source.charAt(i);

         switch (ch) {
            case 'd':
               time += num * 24 * 60 * 60;
               num = 0;
               break;
            case 'h':
               time += num * 60 * 60;
               num = 0;
               break;
            case 'm':
               time += num * 60;
               num = 0;
               break;
            case 's':
               time += num;
               num = 0;
               break;
            default:
               if (ch >= '0' && ch <= '9') {
                  num = num * 10 + (ch - '0');
               } else {
                  throw new ParsingException("Invalid character found: " + ch + ", should be one of [0-9][dhms]");
               }
         }
      }

      context.setFormatResult(new Long(time * 1000 + num));
   }

   public boolean ignoreWhiteSpaces() {
      return true;
   }
}
