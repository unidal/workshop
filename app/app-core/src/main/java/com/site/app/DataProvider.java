package com.site.app;


public interface DataProvider<S extends FieldId> {
   public Object getValue(S fieldId);
}
