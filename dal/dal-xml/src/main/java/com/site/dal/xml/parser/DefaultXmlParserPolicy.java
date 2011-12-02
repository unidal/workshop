package com.site.dal.xml.parser;

public class DefaultXmlParserPolicy implements XmlParserPolicy {
   private UnknownElement m_forUnknownElement = UnknownElement.WARNING;

   private InvalidValue m_forInvalidValue = InvalidValue.WARNING;

   public UnknownElement forUnknownElement() {
      return m_forUnknownElement;
   }

   public InvalidValue forInvalidValue() {
      return m_forInvalidValue;
   }

   public void setForUnknownElement(UnknownElement forUnknownElement) {
      m_forUnknownElement = forUnknownElement;
   }
   
   public void setForInvalidValue(InvalidValue forInvalidValue) {
      m_forInvalidValue = forInvalidValue;
   }
}
