package com.site.bes.common.helpers;

import java.util.Date;

import com.site.bes.EventPayload;
import com.site.bes.EventPayloadException;
import com.site.bes.EventPayloadFormat;
import com.site.kernel.dal.DataObjectField;
import com.site.kernel.dal.DataObjectMetaData;
import com.site.kernel.dal.ValueType;

public abstract class EventPayloadFormatter {
   public abstract String format(EventPayload payload);

   protected String formatFieldValue(DataObjectField field, Object value) {
      String strValue;
      
      if (value == null) {
         strValue = "";
      } else if (field.getValueType() == ValueType.DATE) {
         strValue = String.valueOf(((Date) value).getTime());
      } else {
         strValue = String.valueOf(value);
      }

      return strValue;
   }

   public abstract EventPayloadFormat getPayloadType();

   protected void injectFieldValue(EventPayload payload, String name, String value) {
      DataObjectMetaData metadata = payload.getMetaData();
      DataObjectField field = metadata.getField(name);

      if (field == null) {
         throw new EventPayloadException("No relevant field(" + name + ") defined in " + payload.getClass());
      }

      if (field.getValueType() == ValueType.DATE) {
         try {
            long val = Long.parseLong(value);

            payload.setFieldValue(field, new Date(val));
         } catch (NumberFormatException e) {
            throw new EventPayloadException("Numeric vlaue expected for field(" + name + ") of " + payload.getClass(), e);
         }
      } else {
         payload.setFieldValue(field, value);
      }
   }

   public abstract void parse(EventPayload payload, String data);

   protected Object parseFieldValue(DataObjectField field, String strValue) {
      Object value = strValue;

      if (field.getValueType() == ValueType.DATE) {
         try {
            long val = Long.parseLong(strValue);

            value = new Date(val);
         } catch (NumberFormatException e) {
            // ignore it
         }
      }

      return value;
   }
}
