package com.site.app.rule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.site.app.FieldId;

public class DateRule<S extends FieldId> extends BaseRule<S, Date> {
   private DateFormat m_format;

   public DateRule(S fieldId) {
      super(fieldId);
   }

   public DateRule(S fieldId, Date defaultValue) {
      super(fieldId, defaultValue);
   }

   public DateRule<S> format(String pattern) {
      m_format = new SimpleDateFormat(pattern);
      return this;
   }

   @Override
   public Date convert(Object value) {
      if (value instanceof Date) {
         return (Date) value;
      } else if (value instanceof String) {
         try {
            if (m_format != null) {
               return m_format.parse((String) value);
            } else {
               return new Date();
            }
         } catch (ParseException e) {
            // ignore it
         }
      }

      throw new RuntimeException(value.getClass() + ": " + value);
   }
}
