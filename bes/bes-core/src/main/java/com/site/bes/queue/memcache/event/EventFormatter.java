package com.site.bes.queue.memcache.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.site.bes.Event;
import com.site.bes.queue.memcache.Formatter;
import com.site.bes.queue.memcache.FormatterException;
import com.site.lookup.ContainerHolder;

public class EventFormatter extends ContainerHolder implements Formatter<Event, Integer> {
   private static final char TAB = '\t';

   private static final char QUOTE = '"';

   public String getId(Integer eventId) throws FormatterException {
      return "e-" + eventId;
   }

   public String getValue(Event event) throws FormatterException {
      try {
         StringBuilder sb = new StringBuilder(4096);

         sb.append(event.getId()).append(TAB);
         sb.append(quote(event.getType())).append(TAB);
         sb.append(quote(event.getRefId())).append(TAB);
         sb.append(event.getPayloadFormat()).append(TAB);
         sb.append(quote(event.getPayload())).append(TAB);
         sb.append(quote(event.getProducer())).append(TAB);
         sb.append(event.getMaxRetryTimes()).append(TAB);

         // schedule date
         if (event.getScheduleDate() != null) {
            sb.append(event.getScheduleDate().getTime());
         }

         sb.append(TAB);

         // create date
         if (event.getCreationDate() != null) {
            sb.append(event.getCreationDate().getTime());
         } else {
            sb.append(System.currentTimeMillis());
         }

         sb.append(TAB);

         return sb.toString();
      } catch (Exception e) {
         throw new FormatterException("Error when getting value of event(" + event.getId() + ")", e);
      }
   }

   private String quote(String value) {
      if (value == null) {
         return "";
      }

      int len = value.length();
      StringBuilder sb = new StringBuilder(len + 16);

      sb.append(QUOTE);

      for (int i = 0; i < len; i++) {
         char ch = value.charAt(i);

         if (ch == QUOTE) {
            sb.append(ch).append(ch);
         } else {
            sb.append(ch);
         }
      }

      sb.append(QUOTE);
      return sb.toString();
   }

   private String unquote(String value) {
      int len = value == null ? 0 : value.length();

      if (len > 2 && value!=null && value.charAt(0) == QUOTE && value.charAt(len - 1) == QUOTE) {
         return value.substring(1, len - 1);
      } else {
         return value;
      }
   }

   public Event parseValue(String value) throws FormatterException {
      Event event = new Event();
      List<String> parts = split(value);
      int expectedSize = 9;

      try {
         if (parts.size() == expectedSize) {
            int index = 0;

            event.setId(Integer.parseInt(parts.get(index++)));
            event.setType(unquote(parts.get(index++)));
            event.setRefId(unquote(parts.get(index++)));
            event.setPayloadFormat(Integer.parseInt(parts.get(index++)));
            event.setPayload(unquote(parts.get(index++)));
            event.setProducer(unquote(parts.get(index++)));
            event.setMaxRetryTimes(Integer.parseInt(parts.get(index++)));

            String scheduleDate = parts.get(index++);
            event.setScheduleDate(scheduleDate.length() == 0 ? null : new Date(Long.parseLong(scheduleDate)));
            
            String creationDate = parts.get(index++);
            event.setCreationDate(creationDate.length() == 0 ? null : new Date(Long.parseLong(creationDate)));
         } else {
            throw new RuntimeException("Compatiable issue found in EventFormatter. parts: " + parts.size()
                  + ", expected: " + expectedSize);
         }
      } catch (Exception e) {
         throw new FormatterException("Error when parsing event: " + value, e);
      }

      return event;
   }

   @SuppressWarnings("null")
   private List<String> split(String value) {
      List<String> parts = new ArrayList<String>();
      int len = (value == null ? 0 : value.length());
      StringBuilder sb = new StringBuilder(4096);

      for (int i = 0; i < len; i++) {
         char ch = value.charAt(i);

         switch (ch) {
         case TAB:
            parts.add(sb.toString());
            sb.setLength(0);
            break;
         case QUOTE:
            if (i + 1 < len && value.charAt(i + 1) == QUOTE) {
               i++;
            }

            // break through
         default:
            sb.append(ch);
            break;
         }
      }

      if (sb.length() > 0) {
         parts.add(sb.toString());
      }

      return parts;
   }
}
