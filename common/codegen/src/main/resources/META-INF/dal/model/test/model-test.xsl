<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="../../common.xsl"/>
<xsl:output method="html" indent="no" media-type="text/plain" encoding="utf-8"/>
<xsl:param name="package"/>
<xsl:param name="class"/>
<xsl:variable name="space" select="' '"/>
<xsl:variable name="empty" select="''"/>
<xsl:variable name="empty-line" select="'&#x0A;'"/>

<xsl:template match="/">
   <xsl:apply-templates select="/model"/>
</xsl:template>

<xsl:template match="model">
<xsl:variable name="entity" select="entity[@root='true']" />
<xsl:value-of select="$empty"/>package <xsl:value-of select="$package"/>;

import org.junit.Assert;
import org.junit.Test;

import <xsl:value-of select="$package"/>.entity.<xsl:value-of select="$entity/@entity-class"/>;
import <xsl:value-of select="$package"/>.transform.DefaultJsonBuilder;
import <xsl:value-of select="$package"/>.transform.DefaultXmlBuilder;
import <xsl:value-of select="$package"/>.transform.DefaultXmlParser;
import com.site.helper.Files;

public class <xsl:value-of select="$class"/> {
   @Test
   public void testXml() throws Exception {
      DefaultXmlParser parser = new DefaultXmlParser();
      String source = Files.forIO().readFrom(getClass().getResourceAsStream("<xsl:value-of select="$entity/@name"/>.xml"), "utf-8");
      <xsl:value-of select="$entity/@entity-class"/> root = parser.parse(source);
      String xml = new DefaultXmlBuilder().buildXml(root);
      String expected = source;

      Assert.assertEquals("XML is not well parsed!", expected.replace("\r", ""), xml.replace("\r", ""));
   }
<xsl:if test="/model/@enable-json-builder='true'">
   @Test
   public void testJson() throws Exception {
      DefaultXmlParser parser = new DefaultXmlParser();
      String source = Files.forIO().readFrom(getClass().getResourceAsStream("<xsl:value-of select="$entity/@name"/>.xml"), "utf-8");
      <xsl:value-of select="$entity/@entity-class"/> root = parser.parse(source);
      String json = new DefaultJsonBuilder().buildJson(root);
      String expected = Files.forIO().readFrom(getClass().getResourceAsStream("<xsl:value-of select="$entity/@name"/>.json"), "utf-8");

      Assert.assertEquals("XML is not well parsed or JSON is not well built!", expected.replace("\r", ""), json.replace("\r", ""));
   }
</xsl:if>
}
</xsl:template>

</xsl:stylesheet>
