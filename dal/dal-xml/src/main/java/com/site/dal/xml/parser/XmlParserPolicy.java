package com.site.dal.xml.parser;

public interface XmlParserPolicy {
   public static enum UnknownElement {
      IGNORE, WARNING, ERROR
   }

   public static enum InvalidValue {
      IGNORE, WARNING, ERROR
   }

   public UnknownElement forUnknownElement();

   public InvalidValue forInvalidValue();
}
