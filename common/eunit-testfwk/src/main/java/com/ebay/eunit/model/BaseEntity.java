package com.ebay.eunit.model;

import com.ebay.eunit.model.transform.DefaultXmlBuilder;

public abstract class BaseEntity<T> implements IEntity<T> {
   protected void assertAttributeEquals(Object instance, String entityName, String name, Object expectedValue, Object actualValue) {
      if (!expectedValue.equals(actualValue)) {
         throw new IllegalArgumentException(String.format("Mismatched entity(%s) found! Same %s attribute is expected! %s: %s.", entityName, name, entityName, instance));
      }
   }

   @Override
   public String toString() {
      DefaultXmlBuilder builder = new DefaultXmlBuilder();

      accept(builder);
      return builder.getString();
   }
}
