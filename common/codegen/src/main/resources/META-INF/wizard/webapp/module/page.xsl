<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="../../common.xsl"/>
<xsl:output method="html" indent="no" media-type="text/plain" encoding="utf-8"/>
<xsl:param name="package"/>
<xsl:param name="name"/>
<xsl:variable name="space" select="' '"/>
<xsl:variable name="empty" select="''"/>
<xsl:variable name="empty-line" select="'&#x0A;'"/>

<xsl:template match="/">
   <xsl:apply-templates select="/wizard/module[@name=$name]"/>
</xsl:template>

<xsl:template match="module">
<xsl:value-of select="$empty"/>package <xsl:value-of select="$package"/>;

import com.site.web.mvc.Page;
import com.site.web.mvc.annotation.ModuleMeta;

public enum <xsl:value-of select="@page-class"/> implements Page {
<xsl:for-each select="page">
   <xsl:value-of select="$empty-line"/>
   <xsl:value-of select="'   '"/><xsl:value-of select="@upper-name"/>("<xsl:value-of select="@name"/>", "<xsl:value-of select="@description"/>", true),
</xsl:for-each>
	;

	private String m_name;

	private String m_description;

	private boolean m_realPage;

	private <xsl:value-of select="@page-class" />(String name, String description, boolean realPage) {
		m_name = name;
		m_description = description;
		m_realPage = realPage;
	}

	public static <xsl:value-of select="@page-class"/> getByName(String name, <xsl:value-of select="@page-class"/> defaultPage) {
		for (<xsl:value-of select="@page-class"/> action : <xsl:value-of select="@page-class"/>.values()) {
			if (action.getName().equals(name)) {
				return action;
			}
		}

		return defaultPage;
	}

	public String getName() {
		return m_name;
	}

	public String getDescription() {
		return m_description;
	}

	public String getModuleName() {
		ModuleMeta meta = <xsl:value-of select="@module-class"/>.class.getAnnotation(ModuleMeta.class);

		if (meta != null) {
			return meta.name();
		} else {
			return null;
		}
	}

	public boolean isRealPage() {
		return m_realPage;
	}

	public <xsl:value-of select="@page-class"/>[] getValues() {
		return <xsl:value-of select="@page-class"/>.values();
	}
}
</xsl:template>

</xsl:stylesheet>
