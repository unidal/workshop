package com.site.bes.common.helpers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.site.bes.EventPayload;
import com.site.bes.EventPayloadFormat;
import com.site.kernel.dal.DataObjectField;

public class UrlPayloadFormatter extends EventPayloadFormatter {
   private static final String DEFAULT_ENCODING = "UTF-8";

   private String decode(String text) {
      try {
         return URLDecoder.decode(text, DEFAULT_ENCODING);
      } catch (UnsupportedEncodingException e) {
         return text;
      }
   }

   private String encode(String text) {
      try {
         return URLEncoder.encode(text, DEFAULT_ENCODING);
      } catch (UnsupportedEncodingException e) {
         return text;
      }
   }

   @Override
   public String format(EventPayload payload) {
      List<DataObjectField> fields = payload.getMetaData().getFields();
      StringBuffer sb = new StringBuffer(1024);
      boolean first = true;

      synchronized (sb) {
         for (DataObjectField field : fields) {
            if (payload.isFieldUsed(field)) {
               String name = field.getName();
               Object obj = payload.getFieldValue(field);
               String value = formatFieldValue(field, obj);

               if (!first) {
                  sb.append('&');
               }

               sb.append(name).append('=').append(encode(value));
               first = false;
            }
         }
      }
      
      return sb.toString();
   }

   @Override
   public EventPayloadFormat getPayloadType() {
      return EventPayloadFormat.URL;
   }

   @Override
   public void parse(EventPayload payload, String data) {
      List<String> names = new ArrayList<String>();
      List<String> values = new ArrayList<String>();
      int len = (data == null ? 0 : data.length());
      StringBuffer name = new StringBuffer(64);
      StringBuffer value = new StringBuffer(256);
      int part = 0;

      for (int i = 0; i < len; i++) {
         char ch = data.charAt(i);

         switch (ch) {
         case '=':
            part = 1;
            break;
         case '&':
            if (name.length() > 0) {
               names.add(name.toString().trim());
               values.add(value.toString().trim());
               name.setLength(0);
               value.setLength(0);
            }

            part = 0;
            break;
         default:
            if (part == 0) {
               name.append(ch);
            } else if (part == 1) {
               value.append(ch);
            }

            break;
         }
      }

      if (name.length() > 0) {
         names.add(name.toString().trim());
         values.add(value.toString().trim());
      }

      injectFieldValues(payload, names, values);
   }

   private void injectFieldValues(EventPayload payload, List<String> names, List<String> values) {
      int size = names.size();

      for (int i = 0; i < size; i++) {
         String name = names.get(i);
         String value = values.get(i);

         injectFieldValue(payload, name, decode(value));
      }
   }
}
