package com.site.dal.jdbc.entity;

import com.site.dal.jdbc.DataField;
import com.site.dal.jdbc.DataObject;

public interface DataObjectAccessor {
   public <T extends DataObject> Object getFieldValue(T dataObject, DataField dataField);

   public <T extends DataObject> T newInstance(Class<T> clazz);

   public <T extends DataObject> void setFieldValue(T row, DataField field, Object value);
}
