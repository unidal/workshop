<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" indent="no" media-type="text/plain" encoding="utf-8"/>
<xsl:import href="naming.xsl"/>
<xsl:variable name="space" select="' '"/>
<xsl:variable name="empty" select="''"/>
<xsl:variable name="empty-line" select="'&#x0A;'"/>

<xsl:template match="/">
   <xsl:apply-templates select="/root"/>
</xsl:template>

<xsl:template match="root">
<xsl:value-of select="$empty"/>package <xsl:value-of select="@package"/>;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ebay.esf.resource.testfwk.annotation.Tag;
import com.ebay.esf.resource.testfwk.annotation.TagInit;
import com.ebay.esf.resource.testfwk.junit.ResourceJsfTagRunner;
import com.ebay.junitnexgen.category.Description;

@RunWith(ResourceJsfTagRunner.class)
@TagInit(warRoot = "./WebContent")
public class <xsl:value-of select="@class"/> {<xsl:value-of select="$empty-line"/>
   <xsl:call-template name="field-variables"/>
   <xsl:call-template name="method-test-cases"/>
</xsl:template>

<xsl:template name="field-variables">
   <xsl:for-each select="properties/property">
      <xsl:sort select="@name"/>
      
      <xsl:value-of select="$empty"/>   private String <xsl:value-of select="@name"/> = "<xsl:value-of select="$empty"/>
      <xsl:call-template name="escape-string">
         <xsl:with-param name="source" select="text()"/>
      </xsl:call-template>
      <xsl:value-of select="$empty"/>";<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty-line"/>
   </xsl:for-each>
</xsl:template>

<xsl:template name="method-test-cases">
   <xsl:for-each select="case">
      <xsl:variable name="capital-id">
         <xsl:call-template name="capital-name">
            <xsl:with-param name="name">
               <xsl:call-template name="normalize">
                  <xsl:with-param name="source" select="@id"/>
               </xsl:call-template>
            </xsl:with-param>
         </xsl:call-template>
      </xsl:variable>
      <xsl:value-of select="$empty"/>   @Test<xsl:value-of select="$empty-line"/>
      <xsl:if test="description">   @Description("<xsl:value-of select="description"/>")<xsl:value-of select="$empty-line"/></xsl:if>
      <xsl:value-of select="$empty"/>   @Tag(type = <xsl:value-of select="tag/@type"/>.class, attrs = "<xsl:value-of select="$empty"/>
      <xsl:for-each select="tag/*">
         <xsl:if test="position()>1"><xsl:value-of select="$space"/></xsl:if>
         <xsl:value-of select="name()"/>='<xsl:value-of select="text()"/>'<xsl:value-of select="$empty"/>
      </xsl:for-each>
      <xsl:value-of select="$empty"/>")<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>   public void test<xsl:value-of select="$capital-id"/>(String result) {<xsl:value-of select="$empty-line"/>
      <xsl:choose>
         <xsl:when test="expected/@type='code'">
            <xsl:value-of select="$empty"/>      Assert.assertEquals(<xsl:value-of select="expected"/>, result);<xsl:value-of select="$empty-line"/>
         </xsl:when>
         <xsl:otherwise>
            <xsl:value-of select="$empty"/>      Assert.assertEquals("<xsl:value-of select="expected"/>", result);<xsl:value-of select="$empty-line"/>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:value-of select="$empty"/>   }<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty-line"/>
   </xsl:for-each>
   <xsl:value-of select="$empty"/>}<xsl:value-of select="$empty-line"/>
</xsl:template>

</xsl:stylesheet>
