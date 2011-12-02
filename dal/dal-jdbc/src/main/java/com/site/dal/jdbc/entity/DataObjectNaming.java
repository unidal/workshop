package com.site.dal.jdbc.entity;

import java.lang.reflect.Method;

import com.site.dal.jdbc.DataObject;

public interface DataObjectNaming {
   public Method getGetMethod(Class<? extends DataObject> clazz, String name);

   public Method getSetMethod(Class<? extends DataObject> clazz, String name);
   
}
