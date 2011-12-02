package com.site.app.rule;

import com.site.app.FieldId;

public class IntRule<S extends FieldId> extends BaseRule<S, Integer> {
   public IntRule(S fieldId) {
      super(fieldId);
   }

   public IntRule(S fieldId, Integer defaultValue) {
      super(fieldId, defaultValue);
   }

   @Override
   public Integer convert(Object value) {
      if (value instanceof Number) {
         return Integer.valueOf(((Number) value).intValue());
      } else if (value instanceof String) {
         return Integer.valueOf((String) value);
      }

      throw new RuntimeException(value.getClass() + ": " + value);
   }
}
