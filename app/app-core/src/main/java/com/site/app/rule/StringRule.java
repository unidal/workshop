package com.site.app.rule;

import com.site.app.FieldId;

public class StringRule<S extends FieldId> extends BaseRule<S, String> {
   public StringRule(S fieldId) {
      super(fieldId);
   }

   public StringRule(S fieldId, String defaultValue) {
      super(fieldId, defaultValue);
   }

   @Override
   public String convert(Object value) {
      return value == null ? null : value.toString();
   }
}
