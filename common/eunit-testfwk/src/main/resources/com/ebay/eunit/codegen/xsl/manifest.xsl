<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="yes" media-type="text/xml" encoding="utf-8"/>

<xsl:variable name="space" select="' '"/>
<xsl:variable name="empty" select="''"/>
<xsl:variable name="empty-line" select="'&#x0A;'"/>

<xsl:template match="/">
   <xsl:call-template name="manifest">
      <xsl:with-param name="root" select="child::node()"/>
   </xsl:call-template>
</xsl:template>

<xsl:template name="manifest">
   <xsl:param name="root"/>
   
   <xsl:call-template name="generate-java">
     <xsl:param name="src-dir" select="$root/@src-dir" />
     <xsl:with-param name="class" select="$root/@class"/>
     <xsl:with-param name="package" select="$root/@package"/>
   </xsl:call-template>
</xsl:template>

<xsl:template name="generate-java">
   <xsl:param name="src-dir" select="'target/generated-test-java'" />
   <xsl:param name="name" select="''" />
   <xsl:param name="package"/>
   <xsl:param name="class" select="''"/>
   <xsl:param name="template" select="''"/>
   <xsl:param name="mode" select="'create_or_overwrite'"/>

    <xsl:value-of select="$empty-line"/>
    <xsl:element name="file">
       <xsl:attribute name="path">
          <xsl:value-of select="$src-dir"/>/<xsl:value-of select="translate($package,'.','/')"/>/<xsl:value-of select="$empty"/>
          <xsl:value-of select="$class"/>.java<xsl:value-of select="$empty"/>
       </xsl:attribute>
       
       <xsl:attribute name="template"><xsl:value-of select="$template"/></xsl:attribute>
       
       <xsl:attribute name="mode"><xsl:value-of select="$mode"/></xsl:attribute>
       
       <xsl:value-of select="$empty-line"/>
       <xsl:element name="property">
          <xsl:attribute name="name">package</xsl:attribute>
          
          <xsl:value-of select="$package"/>
       </xsl:element>
       
       <xsl:if test="$name">
          <xsl:value-of select="$empty-line"/>
          <xsl:element name="property">
             <xsl:attribute name="name">name</xsl:attribute>
          
             <xsl:value-of select="$name"/>
          </xsl:element>
       </xsl:if>
       
       <xsl:value-of select="$empty-line"/>
    </xsl:element>
</xsl:template>

</xsl:stylesheet>