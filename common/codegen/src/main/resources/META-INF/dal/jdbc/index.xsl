<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="../common.xsl"/>
<xsl:output method="html" indent="no" media-type="text/plain" encoding="utf-8"/>
<xsl:variable name="space" select="' '"/>
<xsl:variable name="empty" select="''"/>
<xsl:variable name="empty-line" select="'&#x0A;'"/>

<xsl:template match="/">
   <xsl:value-of select="$empty"/>package <xsl:value-of select="//entity/@do-package"/>;<xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty"/>public class _INDEX {<xsl:value-of select="$empty-line"/>
   <xsl:call-template name="get-classes">
      <xsl:with-param name="method" select="'getEntityClasses'"/>
      <xsl:with-param name="suffix" select="'Entity'"/>
   </xsl:call-template>
   <xsl:call-template name="get-classes">
      <xsl:with-param name="method" select="'getDaoClasses'"/>
      <xsl:with-param name="suffix" select="'Dao'"/>
   </xsl:call-template>
   <xsl:call-template name="get-classes">
      <xsl:with-param name="method" select="'getDoClasses'"/>
      <xsl:with-param name="suffix" select="''"/>
   </xsl:call-template>
   <xsl:value-of select="$empty"/>}<xsl:value-of select="$empty-line"/>
</xsl:template>

<xsl:template name="get-classes">
   <xsl:param name="method" />
   <xsl:param name="suffix" />

   <xsl:value-of select="$empty"/>   public static Class<xsl:value-of select="$empty"/>
   <xsl:call-template name="generic-type">
      <xsl:with-param name="type">?</xsl:with-param>
   </xsl:call-template>
   <xsl:value-of select="$empty"/>[] <xsl:value-of select="$method"/>() {<xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty"/>      return new Class<xsl:value-of select="$empty"/>
   <xsl:call-template name="generic-type">
      <xsl:with-param name="type">?</xsl:with-param>
   </xsl:call-template>
   <xsl:value-of select="$empty"/>[] { <xsl:value-of select="$empty"/>
   <xsl:for-each select="/entities/entity">
      <xsl:if test="position() != 1">, </xsl:if><xsl:value-of select="@class-name"/><xsl:value-of select="$suffix"/>.class<xsl:value-of select="$empty"/>
   </xsl:for-each>
   <xsl:value-of select="$empty"/> };<xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty"/>   }<xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty-line"/>
</xsl:template>

</xsl:stylesheet>
