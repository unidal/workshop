package com.site.kernel.dal;

import java.lang.reflect.Method;

public interface DataObjectNaming {
   public Method getGetterMethod(Class doClass, DataObjectField field);

   public Method getSetterMethod(Class doClass, DataObjectField field);
}
