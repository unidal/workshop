package com.site.dal.xml.dynamic;

import java.util.Map;

public interface DynamicAttributes {
   public void setDynamicAttribute(String name, String value);

   public Map<String, String> getDynamicAttributes();
}
