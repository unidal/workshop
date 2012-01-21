<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="../../common.xsl"/>
<xsl:output method="html" indent="no" media-type="text/plain" encoding="utf-8"/>
<xsl:param name="package"/>
<xsl:variable name="space" select="' '"/>
<xsl:variable name="empty" select="''"/>
<xsl:variable name="empty-line" select="'&#x0A;'"/>

<xsl:template match="/">
   <xsl:apply-templates select="/model"/>
</xsl:template>

<xsl:template match="model">
   <xsl:value-of select="$empty"/>package <xsl:value-of select="$package"/>;<xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty-line"/>
   <xsl:call-template name='import-list'/>
   <xsl:value-of select="$empty"/>public class CompactJsonBuilder implements IVisitor {<xsl:value-of select="$empty-line"/>
   <xsl:call-template name='method-commons'/>
   <xsl:call-template name='method-visit'/>
   <xsl:value-of select="$empty"/>}<xsl:value-of select="$empty-line"/>
</xsl:template>

<xsl:template name="import-list">
   <xsl:for-each select="entity/attribute[not(@render='false')]">
      <xsl:sort select="@upper-name"/>

      <xsl:variable name="upper-name" select="@upper-name"/>
      <xsl:if test="generate-id(//entity/attribute[@upper-name=$upper-name][1])=generate-id()">
         <xsl:value-of select="$empty"/>import static <xsl:value-of select="/model/@model-package"/>.Constants.<xsl:value-of select="@upper-name"/>;<xsl:value-of select="$empty-line"/>
      </xsl:if>
   </xsl:for-each>
   <xsl:for-each select="entity/element[not(@render='false')]">
      <xsl:sort select="@upper-name"/>

      <xsl:variable name="upper-name" select="@upper-name"/>
      <xsl:if test="generate-id(//entity/element[@upper-name=$upper-name][1])=generate-id()">
         <xsl:choose>
            <xsl:when test="@list='true' or @set='true'">
               <xsl:value-of select="$empty"/>import static <xsl:value-of select="/model/@model-package"/>.Constants.<xsl:value-of select="@upper-name"/>;<xsl:value-of select="$empty-line"/>
            </xsl:when>
            <xsl:otherwise>
               <xsl:value-of select="$empty"/>import static <xsl:value-of select="/model/@model-package"/>.Constants.<xsl:value-of select="@upper-name-element"/>;<xsl:value-of select="$empty-line"/>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:if>
   </xsl:for-each>
   <xsl:for-each select="entity/entity-ref[not(@render='false')]">
      <xsl:sort select="@upper-name"/>

      <xsl:variable name="upper-name" select="@upper-name"/>
      <xsl:if test="generate-id((//entity/entity-ref[not(@render='false')])[@upper-name=$upper-name][1])=generate-id()">
         <xsl:value-of select="$empty"/>import static <xsl:value-of select="/model/@model-package"/>.Constants.<xsl:value-of select="@upper-name"/>;<xsl:value-of select="$empty-line"/>
      </xsl:if>
   </xsl:for-each>
   <xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty"/>import java.util.List;<xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty"/>import java.util.Map;<xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty-line"/>
   <xsl:if test="//entity[@all-children-in-sequence='true']">
      <xsl:value-of select="$empty"/>import <xsl:value-of select="/model/@model-package"/>.BaseEntity;<xsl:value-of select="$empty-line"/>
   </xsl:if>
   <xsl:value-of select="$empty"/>import <xsl:value-of select="/model/@model-package"/>.IVisitor;<xsl:value-of select="$empty-line"/>
   <xsl:for-each select="entity">
      <xsl:sort select="@entity-class"/>

      <xsl:value-of select="$empty"/>import <xsl:value-of select="@entity-package"/>.<xsl:value-of select='@entity-class'/>;<xsl:value-of select="$empty-line"/>
   </xsl:for-each>
   <xsl:value-of select="$empty-line"/>
</xsl:template>

<xsl:template name="method-commons">
   private StringBuilder m_sb = new StringBuilder(2048);

   protected void arrayBegin(String name) {
      m_sb.append('"').append(name).append("\":[");
   }

   protected void arrayEnd(String name) {
      trimComma();
      m_sb.append("],");
   }

   protected void attributes(Map<xsl:value-of select="'&lt;String, String&gt;'" disable-output-escaping="yes"/> dynamicAttributes, Object... nameValues) {
      int len = nameValues.length;

      for (int i = 0; i + 1 <xsl:value-of select="'&lt;'" disable-output-escaping="yes"/> len; i += 2) {
         Object attrName = nameValues[i];
         Object attrValue = nameValues[i + 1];

         if (attrValue != null) {
            if (attrValue instanceof List) {
               @SuppressWarnings("unchecked")
               List<xsl:value-of select="'&lt;Object&gt;'" disable-output-escaping="yes"/> list = (List<xsl:value-of select="'&lt;Object&gt;'" disable-output-escaping="yes"/>) attrValue;

               if (!list.isEmpty()) {
                  m_sb.append('"').append(attrName).append("\":[");

                  for (Object item : list) {
                     toString(m_sb, item);
                     m_sb.append(',');
                  }

                  m_sb.setLength(m_sb.length() - 1);
                  m_sb.append("],");
               }
            } else {
               m_sb.append('"').append(attrName).append("\":");
               toString(m_sb, attrValue);
               m_sb.append(",");
            }
         }
      }

      if (dynamicAttributes != null) {
         for (Map.Entry<xsl:value-of select="'&lt;String, String&gt;'" disable-output-escaping="yes"/> e : dynamicAttributes.entrySet()) {
            m_sb.append('"').append(e.getKey()).append("\":");
            toString(m_sb, e.getValue());
            m_sb.append(",");
         }
      }
   }

   public String getString() {
      return m_sb.toString();
   }

   protected void objectBegin(String name) {
      if (name == null) {
         m_sb.append("{");
      } else {
         m_sb.append('"').append(name).append("\":{");
      }
   }

   protected void objectEnd(String name) {
      trimComma();
      m_sb.append("},");
   }
<xsl:if test="(//entity/attribute | //entity/element)[@value-type='java.util.Date'][not(@render='false')]">
   protected String toString(java.util.Date date, String format) {
      if (date != null) {
         return new java.text.SimpleDateFormat(format).format(date);
      } else {
         return null;
      }
   }
</xsl:if>
   protected void toString(StringBuilder sb, Object value) {
      if (value instanceof String) {
         sb.append('"').append(value).append('"');
      } else if (value instanceof Boolean || value instanceof Number) {
         sb.append(value);
      } else {
         sb.append('"').append(value).append('"');
      }
   }

   protected void trimComma() {
      int len = m_sb.length();

      if (len <xsl:value-of select="'&gt;'" disable-output-escaping="yes"/> 1 <xsl:value-of select="'&amp;&amp;'" disable-output-escaping="yes"/> m_sb.charAt(len - 1) == ',') {
         m_sb.replace(len - 1, len, "");
      }
   }
</xsl:template>

<xsl:template name="method-visit">
   <xsl:for-each select="entity">
      <xsl:sort select="@visit-method"/>

      <xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>   @Override<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>   public void <xsl:value-of select="@visit-method"/>(<xsl:value-of select="@entity-class"/><xsl:value-of select="$space"/><xsl:value-of select="@param-name"/>) {<xsl:value-of select="$empty-line"/>
      <xsl:if test="@root='true'">
         <xsl:value-of select="$empty"/>      objectBegin(null);<xsl:value-of select="$empty-line"/>
      </xsl:if>
      <xsl:choose>
         <xsl:when test="@all-children-in-sequence='true'">
            <xsl:value-of select="$empty"/>      objectBegin(<xsl:value-of select="@upper-name"/><xsl:call-template name="tag-fields"/>);<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>      for (BaseEntity<xsl:value-of select="'&lt;?&gt;'" disable-output-escaping="yes"/> child : <xsl:value-of select="@param-name"/>.<xsl:value-of select="@method-get-all-children-in-sequence"/>()) {<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>         child.accept(this);<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>      }<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>      objectEnd(<xsl:value-of select="@upper-name"/>);<xsl:value-of select="$empty-line"/>
         </xsl:when>
         <xsl:when test="element[not(@render='false') and (@list='true' or @set='true')]">
         <xsl:variable name="entity" select="."/>
         <xsl:for-each select="element[not(@render='false') and (@list='true' or @set='true')]">
            <xsl:value-of select="$empty"/>      if (!<xsl:value-of select="$entity/@param-name"/>.<xsl:value-of select="@get-method"/>().isEmpty()) {<xsl:value-of select="$empty-line"/>
             <xsl:value-of select="$empty"/>         arrayBegin(<xsl:value-of select="@upper-name"/>);<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>         for (<xsl:value-of select="@value-type-element"/><xsl:value-of select="$space"/><xsl:value-of select="@param-name-element"/> : <xsl:value-of select="$entity/@param-name"/>.<xsl:value-of select="@get-method"/>()) {<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>            m_sb.append('"').append(<xsl:value-of select="@param-name-element"/>).append("\",");<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>         }<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>         arrayEnd(<xsl:value-of select="@upper-name"/>);<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>      }<xsl:value-of select="$empty-line"/>
            <xsl:if test="position()!=last()">
               <xsl:value-of select="$empty-line"/>
            </xsl:if>
         </xsl:for-each>
         </xsl:when>
         <xsl:when test="entity-ref[not(@render='false')]">
            <xsl:value-of select="$empty"/>      attributes(<xsl:call-template name="get-dynamic-attributes"/><xsl:call-template name="tag-fields"/>);<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty-line"/>
            <xsl:call-template name="visit-children"/>
         </xsl:when>
         <xsl:otherwise>
            <xsl:value-of select="$empty"/>      attributes(<xsl:call-template name="get-dynamic-attributes"/><xsl:call-template name="tag-fields"/>);<xsl:value-of select="$empty-line"/>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:if test="@root='true'">
         <xsl:value-of select="$empty-line"/>
         <xsl:value-of select="$empty"/>      objectEnd(null);<xsl:value-of select="$empty-line"/>
         <xsl:value-of select="$empty"/>      trimComma();<xsl:value-of select="$empty-line"/>
      </xsl:if>
      <xsl:value-of select="$empty"/>   }<xsl:value-of select="$empty-line"/>
   </xsl:for-each>
</xsl:template>

<xsl:template name="visit-children">
   <xsl:variable name="current" select="."/>
   <xsl:for-each select="entity-ref[not(@render='false')]">
      <xsl:variable name="name" select="@name"/>
      <xsl:variable name="entity" select="//entity[@name=$name]"/>
      <xsl:choose>
         <xsl:when test="@map='true'">
            <xsl:value-of select="$empty"/>      if (!<xsl:value-of select="$current/@param-name"/>.<xsl:value-of select="@get-method"/>().isEmpty()) {<xsl:value-of select="$empty-line"/>
            <xsl:choose>
               <xsl:when test="@names">
                  <xsl:value-of select="$empty"/>         objectBegin(<xsl:value-of select="@upper-name"/>);<xsl:value-of select="$empty-line"/>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:value-of select="$empty"/>         objectBegin(<xsl:value-of select="$entity/@upper-name"/>);<xsl:value-of select="$empty-line"/>
               </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>         for (<xsl:value-of select="@value-type-entry" disable-output-escaping="yes"/> e : <xsl:value-of select="$current/@param-name"/>.<xsl:value-of select="@get-method"/>().entrySet()) {<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>            objectBegin(e.getKey());<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>            <xsl:value-of select="'            '"/><xsl:value-of select="$entity/@visit-method"/>(e.getValue());<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>            objectEnd(e.getKey());<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>         }<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty-line"/>
            <xsl:choose>
               <xsl:when test="@names">
                  <xsl:value-of select="$empty"/>         objectEnd(<xsl:value-of select="@upper-name"/>);<xsl:value-of select="$empty-line"/>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:value-of select="$empty"/>         objectEnd(<xsl:value-of select="$entity/@upper-name"/>);<xsl:value-of select="$empty-line"/>
               </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="$empty"/>      }<xsl:value-of select="$empty-line"/>
         </xsl:when>
         <xsl:when test="@list='true'">
            <xsl:value-of select="$empty"/>      if (!<xsl:value-of select="$current/@param-name"/>.<xsl:value-of select="@get-method"/>().isEmpty()) {<xsl:value-of select="$empty-line"/>
            <xsl:choose>
               <xsl:when test="@names">
                  <xsl:value-of select="$empty"/>         arrayBegin(<xsl:value-of select="@upper-name"/>);<xsl:value-of select="$empty-line"/>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:value-of select="$empty"/>         arrayBegin(<xsl:value-of select="$entity/@upper-name"/>);<xsl:value-of select="$empty-line"/>
               </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>         for (<xsl:value-of select="$entity/@entity-class"/><xsl:value-of select="$space"/><xsl:value-of select="@param-name-element"/> : <xsl:value-of select="$current/@param-name"/>.<xsl:value-of select="@get-method"/>()) {<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>            objectBegin(null);<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>            <xsl:value-of select="'            '"/><xsl:value-of select="$entity/@visit-method"/>(<xsl:value-of select="@param-name-element"/>);<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>            objectEnd(null);<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>         }<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty-line"/>
            <xsl:choose>
               <xsl:when test="@names">
                  <xsl:value-of select="$empty"/>         arrayEnd(<xsl:value-of select="@upper-name"/>);<xsl:value-of select="$empty-line"/>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:value-of select="$empty"/>         arrayEnd(<xsl:value-of select="$entity/@upper-name"/>);<xsl:value-of select="$empty-line"/>
               </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="$empty"/>      }<xsl:value-of select="$empty-line"/>
         </xsl:when>
         <xsl:otherwise>
            <xsl:value-of select="$empty"/>      if (<xsl:value-of select="$current/@param-name"/>.<xsl:value-of select="@get-method"/>() != null) {<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>         objectBegin(<xsl:value-of select="@upper-name"/>);<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>         <xsl:value-of select="'         '"/><xsl:value-of select="$entity/@visit-method"/>(<xsl:value-of select="$current/@param-name"/>.<xsl:value-of select="@get-method"/>());<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>         objectEnd(<xsl:value-of select="@upper-name"/>);<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>      }<xsl:value-of select="$empty-line"/>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:if test="position()!=last()">
         <xsl:value-of select="$empty-line"/>
      </xsl:if>
   </xsl:for-each>
</xsl:template>

<xsl:template name="tag-fields">
   <xsl:param name="entity" select="."/>
   
   <xsl:for-each select="(attribute | element)[not(@render='false' or @list='true' or @set='true')]">
      <xsl:choose>
         <xsl:when test="@value-type='Class&lt;?&gt;'">
            <xsl:value-of select="$empty"/>, <xsl:value-of select="@upper-name"/>, <xsl:value-of select="$entity/@param-name"/>.<xsl:value-of select="@get-method"/>() == null ? null : <xsl:value-of select="$entity/@param-name"/>.<xsl:value-of select="@get-method"/>().getName()<xsl:value-of select="$empty"/>
         </xsl:when>
         <xsl:when test="@value-type='boolean'">
            <xsl:value-of select="$empty"/>, <xsl:value-of select="@upper-name"/>, <xsl:value-of select="$entity/@param-name"/>.<xsl:value-of select="@is-method"/>()<xsl:value-of select="$empty"/>
         </xsl:when>
         <xsl:when test="@value-type='java.util.Date'">
            <xsl:value-of select="$empty"/>, <xsl:value-of select="@upper-name"/>, toString(<xsl:value-of select="$entity/@param-name"/>.<xsl:value-of select="@get-method"/>(), "<xsl:value-of select="@format"/>")<xsl:value-of select="$empty"/>
         </xsl:when>
         <xsl:otherwise>
            <xsl:value-of select="$empty"/>, <xsl:value-of select="@upper-name"/>, <xsl:value-of select="$entity/@param-name"/>.<xsl:value-of select="@get-method"/>()<xsl:value-of select="$empty"/>
         </xsl:otherwise>
      </xsl:choose>
   </xsl:for-each>
</xsl:template>

<xsl:template name="get-dynamic-attributes">
   <xsl:param name="entity" select="."/>
   
   <xsl:choose>
      <xsl:when test="@dynamic-attributes='true'">
         <xsl:value-of select="$entity/@param-name"/>.getDynamicAttributes()<xsl:value-of select="$empty"/>
      </xsl:when>
      <xsl:otherwise>null</xsl:otherwise>
   </xsl:choose>
</xsl:template>

</xsl:stylesheet>
