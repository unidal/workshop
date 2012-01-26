<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="../common.xsl"/>
<xsl:output method="html" indent="no" media-type="text/plain" encoding="utf-8"/>
<xsl:param name="package"/>
<xsl:variable name="space" select="' '"/>
<xsl:variable name="empty" select="''"/>
<xsl:variable name="empty-line" select="'&#x0A;'"/>

<xsl:template match="/">
   <xsl:apply-templates select="/model"/>
</xsl:template>

<xsl:template match="model">
<xsl:value-of select="$empty"/>package <xsl:value-of select="$package"/>;

import <xsl:value-of select="@transform-package"/>.DefaultXmlBuilder;

public abstract class BaseEntity<xsl:call-template name="generic-type"><xsl:with-param name="type" select="'T'"/></xsl:call-template> implements IEntity<xsl:call-template name="generic-type"><xsl:with-param name="type" select="'T'"/></xsl:call-template> {
   protected void assertAttributeEquals(Object instance, String entityName, String name, Object expectedValue, Object actualValue) {
      if (expectedValue == null <xsl:value-of select="'&amp;&amp;'" disable-output-escaping="yes"/> actualValue != null || expectedValue != null <xsl:value-of select="'&amp;&amp;'" disable-output-escaping="yes"/> !expectedValue.equals(actualValue)) {
         throw new IllegalArgumentException(String.format("Mismatched entity(%s) found! Same %s attribute is expected! %s: %s.", entityName, name, entityName, instance));
      }
   }
<xsl:if test="//entity/attribute[@value-type='java.util.Date' and @default-value] | //entity/element[@value-type='java.util.Date' and @default-value]">
   protected java.util.Date toDate(String format, String text) {
      try {
         return new java.text.SimpleDateFormat(format).parse(text);
      } catch (java.text.ParseException e) {
         throw new IllegalArgumentException(
               String.format("Error when parsing date(%s) with format(%s)!", text, format), e);
      }
   }
</xsl:if>
   @Override
   public String toString() {
      DefaultXmlBuilder builder = new DefaultXmlBuilder();

      accept(builder);
      return builder.getString();
   }
}
</xsl:template>

</xsl:stylesheet>
