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
   <xsl:value-of select="$empty"/>public class DefaultSaxParser extends DefaultHandler {<xsl:value-of select="$empty-line"/>
   <xsl:call-template name="declare-field-variables"/>
   <xsl:call-template name='method-parse'/>
   <xsl:call-template name='method-characters'/>
   <xsl:call-template name='method-end-element'/>
   <xsl:call-template name='method-get-root'/>
   <xsl:call-template name='method-get-text'/>
   <xsl:call-template name='method-parse-children'/>
   <xsl:call-template name='method-start-element'/>
   <xsl:value-of select="$empty"/>}<xsl:value-of select="$empty-line"/>
</xsl:template>

<xsl:template name="import-list">
   <xsl:if test="entity/element[not(@render='false')]">
      <xsl:for-each select="entity/element[not(@text='true') and not(@render='false')]">
         <xsl:sort select="@upper-name"/>
   
         <xsl:variable name="upper-name-element" select="@upper-name-element"/>
         <xsl:variable name="upper-name" select="@upper-name"/>
         <xsl:if test="generate-id(//entity/element[@upper-name-element=$upper-name-element][1])=generate-id()">
            <xsl:value-of select="$empty"/>import static <xsl:value-of select="/model/@model-package"/>.Constants.<xsl:value-of select="@upper-name-element"/>;<xsl:value-of select="$empty-line"/>
         </xsl:if>
         <xsl:if test="@list='true' or @set='true'">
            <xsl:if test="generate-id(//entity/element[(@list='true' or @set='true') and @upper-name=$upper-name][1])=generate-id()">
               <xsl:value-of select="$empty"/>import static <xsl:value-of select="/model/@model-package"/>.Constants.<xsl:value-of select="@upper-name"/>;<xsl:value-of select="$empty-line"/>
            </xsl:if>
         </xsl:if>
      </xsl:for-each>
      <xsl:value-of select="$empty-line"/>
   </xsl:if>
   <xsl:for-each select="entity">
      <xsl:sort select="@upper-name"/>

      <xsl:value-of select="$empty"/>import static <xsl:value-of select="/model/@model-package"/>.Constants.<xsl:value-of select="@upper-name"/>;<xsl:value-of select="$empty-line"/>
   </xsl:for-each>
   <xsl:for-each select="entity/entity-ref[(@list='true' or @map='true') and @xml-indent='true' and not(@render='false')]">
      <xsl:sort select="@upper-name"/>

      <xsl:variable name="upper-name" select="@upper-name"/>
      <xsl:if test="generate-id(//entity/entity-ref[(@list='true' or @map='true') and @xml-indent='true' and not(@render='false') and @upper-name=$upper-name][1])=generate-id()">
         <xsl:variable name="upper-name">
            <xsl:choose>
               <xsl:when test="@xml-indent='true' or @alias">
                  <xsl:value-of select="@upper-name"/>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:variable name="name" select="@name"/>
                  <xsl:value-of select="//entity[@name=$name]/@upper-name"/>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:variable>
         <xsl:value-of select="$empty"/>import static <xsl:value-of select="/model/@model-package"/>.Constants.<xsl:value-of select="$upper-name"/>;<xsl:value-of select="$empty-line"/>
      </xsl:if>
   </xsl:for-each>
   <xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty"/>import java.io.IOException;<xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty"/>import java.io.InputStream;<xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty"/>import java.io.Reader;<xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty"/>import java.io.StringReader;<xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty"/>import java.util.Stack;<xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty"/>import javax.xml.parsers.ParserConfigurationException;<xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty"/>import javax.xml.parsers.SAXParser;<xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty"/>import javax.xml.parsers.SAXParserFactory;<xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty"/>import org.xml.sax.Attributes;<xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty"/>import org.xml.sax.InputSource;<xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty"/>import org.xml.sax.SAXException;<xsl:value-of select="$empty-line"/>
   <xsl:value-of select="$empty"/>import org.xml.sax.helpers.DefaultHandler;<xsl:value-of select="$empty-line"/>
   <xsl:for-each select="entity">
      <xsl:sort select="@entity-class"/>

      <xsl:value-of select="$empty"/>import <xsl:value-of select="@entity-package"/>.<xsl:value-of select='@entity-class'/>;<xsl:value-of select="$empty-line"/>
   </xsl:for-each>
   <xsl:value-of select="$empty-line"/>
</xsl:template>

<xsl:template name="declare-field-variables">
   private ClientConfig m_root = new ClientConfig();

   private DefaultLinker m_linker = new DefaultLinker();

   private DefaultSaxMaker m_maker = new DefaultSaxMaker();

   private Stack<xsl:value-of select="'&lt;String&gt;'" disable-output-escaping="yes"/> m_tags = new Stack<xsl:value-of select="'&lt;String&gt;'" disable-output-escaping="yes"/>();

   private Stack<xsl:value-of select="'&lt;Object&gt;'" disable-output-escaping="yes"/> m_objs = new Stack<xsl:value-of select="'&lt;Object&gt;'" disable-output-escaping="yes"/>();

   private StringBuilder m_text = new StringBuilder();
</xsl:template>

<xsl:template name="method-get-document">
   protected Node getDocument(String xml) {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

      dbf.setIgnoringElementContentWhitespace(true);
      dbf.setIgnoringComments(true);

      try {
         DocumentBuilder db = dbf.newDocumentBuilder();

         return db.parse(new InputSource(new StringReader(xml)));
      } catch (Exception x) {
         throw new RuntimeException(x);
      }
   }
</xsl:template>

<xsl:template name="method-parse">
   <xsl:variable name="current" select="."/>
   <xsl:value-of select="$empty-line"/>
   <xsl:for-each select="entity[@root='true']">
      <xsl:value-of select="$empty"/>   public static <xsl:value-of select="@entity-class"/> parse(InputSource is) throws SAXException, IOException {<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>      try {<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>         SAXParser parser = SAXParserFactory.newInstance().newSAXParser();<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>         DefaultSaxParser handler = new DefaultSaxParser();<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>         parser.parse(is, handler);<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>         return handler.getRoot();<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>      } catch (ParserConfigurationException e) {<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>         throw new IllegalStateException("Unable to get SAX parser instance!", e);<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>      }<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>   }<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>   public static <xsl:value-of select="@entity-class"/> parse(InputStream in) throws SAXException, IOException {<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>      return parse(new InputSource(in));<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>   }<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>   public static <xsl:value-of select="@entity-class"/> parse(Reader reader) throws SAXException, IOException {<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>      return parse(new InputSource(reader));<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>   }<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>   public static <xsl:value-of select="@entity-class"/> parse(String xml) throws SAXException, IOException {<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>      return parse(new InputSource(new StringReader(xml)));<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>   }<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty-line"/>
   </xsl:for-each>
</xsl:template>

<xsl:template name="method-characters">
   @Override
   public void characters(char[] ch, int start, int length) throws SAXException {
      m_text.append(ch, start, length);
   }
</xsl:template>

<xsl:template name="method-end-element">
   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      if (uri == null || uri.length() == 0) {<xsl:value-of select="$empty-line"/>
            <xsl:choose>
               <xsl:when test="//entity[element]">
                  <xsl:value-of select="$empty"/>         Object currentObj = m_objs.pop();<xsl:value-of select="$empty-line"/>
                  <xsl:value-of select="$empty"/>         String currentTag = m_tags.pop();<xsl:value-of select="$empty-line"/>
                  <xsl:value-of select="$empty-line"/>
                  <xsl:for-each select="//entity[element]">
                     <xsl:variable name="entity" select="."/>
                     
                     <xsl:choose>
                        <xsl:when test="position()=1"><xsl:value-of select="'         '"/></xsl:when>
                        <xsl:otherwise> else </xsl:otherwise>
                     </xsl:choose>
                     <xsl:value-of select="$empty"/>if (currentObj instanceof <xsl:value-of select="@entity-class"/>) {<xsl:value-of select="$empty-line"/>
                     <xsl:value-of select="$empty"/>            <xsl:value-of select="'            '"/><xsl:value-of select="@entity-class"/><xsl:value-of select="$space"/><xsl:value-of select="@local-name"/> = (<xsl:value-of select="@entity-class"/>) currentObj;<xsl:value-of select="$empty-line"/>
                     <xsl:value-of select="$empty-line"/>
                     <xsl:for-each select="element">
                        <xsl:choose>
                           <xsl:when test="@text='true'">
                              <xsl:value-of select="$empty"/>            <xsl:value-of select="'            '"/><xsl:value-of select="$entity/@local-name"/>.<xsl:value-of select="@set-method"/>(getText());<xsl:value-of select="$empty-line"/>
                           </xsl:when>
                           <xsl:otherwise>
                              <xsl:value-of select="$empty"/>            if (<xsl:value-of select="@upper-name-element"/>.equals(currentTag)) {<xsl:value-of select="$empty-line"/>
                              <xsl:value-of select="$empty"/>               <xsl:value-of select="'               '"/><xsl:value-of select="$entity/@local-name"/>.<xsl:value-of select="@set-method"/>(getText());<xsl:value-of select="$empty-line"/>
                              <xsl:value-of select="$empty"/>            }<xsl:value-of select="$empty-line"/>
                           </xsl:otherwise>
                        </xsl:choose>
                     </xsl:for-each>
                     <xsl:value-of select="$empty"/>         }<xsl:value-of select="$empty"/>
                  </xsl:for-each>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:value-of select="$empty"/>         m_objs.pop();<xsl:value-of select="$empty-line"/>
                  <xsl:value-of select="$empty"/>         m_tags.pop();<xsl:value-of select="$empty-line"/>
               </xsl:otherwise>
            </xsl:choose>
      }

      m_text.setLength(0);
   }
</xsl:template>

<xsl:template name="method-get-root">
   public <xsl:value-of select="entity[@root='true']/@entity-class"/> getRoot() {
      return m_root;
   }
</xsl:template>

<xsl:template name="method-get-text">
   protected String getText() {
      return m_text.toString().trim();
   }
</xsl:template>

<xsl:template name="method-parse-children">
   <xsl:for-each select="entity[@root='true']">
      <xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>   public void parse(String qName, Attributes attributes) throws SAXException {<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>      if (<xsl:value-of select="@upper-name"/>.equals(qName)) {<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>         <xsl:value-of select="'         '"/><xsl:value-of select="@entity-class"/><xsl:value-of select="$space"/><xsl:value-of select="@param-name"/> = m_maker.<xsl:value-of select="@build-method"/>(attributes);<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>         m_root = <xsl:value-of select="@param-name"/>;<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>         m_objs.push(<xsl:value-of select="@param-name"/>);<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>         m_tags.push(qName);<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>      } else {<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>         throw new SAXException("Root element(<xsl:value-of select="@param-name"/>) expected");<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>      }<xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>   }<xsl:value-of select="$empty-line"/>
   </xsl:for-each>
   <xsl:for-each select="entity">
      <xsl:sort select="@build-method"/>

      <xsl:value-of select="$empty-line"/>
      <xsl:value-of select="$empty"/>   public void <xsl:value-of select="@parse-for-method"/>(<xsl:value-of select="@entity-class"/> parentObj, String parentTag, String qName, Attributes attributes) throws SAXException {<xsl:value-of select="$empty-line"/>
      <xsl:call-template name="parse-children">
         <xsl:with-param name="indent" select="'      '"/>
      </xsl:call-template>
      <xsl:value-of select="$empty"/>   }<xsl:value-of select="$empty-line"/>
   </xsl:for-each>
</xsl:template>

<xsl:template name="parse-children">
   <xsl:param name="indent"/>
   
   <xsl:variable name="current" select="."/>
   <xsl:choose>
      <xsl:when test="@all-children-in-sequence='true'">
         <xsl:value-of select="$indent"/>for (Node child : getChildTagNodes(node, null)) {<xsl:value-of select="$empty-line"/>
         <xsl:for-each select="entity-ref[not(@render='false')]">
            <xsl:variable name="name" select="@name"/>
            <xsl:variable name="entity" select="//entity[@name=$name]"/>
            <xsl:choose>
               <xsl:when test="position()=1"><xsl:value-of select="$indent"/><xsl:value-of select="'   '"/></xsl:when>
               <xsl:otherwise> else </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
               <xsl:when test="@list='true' or @map='true'">
                  <xsl:value-of select="$empty"/>if (child.getNodeName().equals(<xsl:value-of select="$entity/@upper-name"/>)) {<xsl:value-of select="$empty-line"/>
                  <xsl:value-of select="$indent"/>   <xsl:value-of select="'      '"/><xsl:value-of select="$entity/@entity-class"/><xsl:value-of select="$space"/><xsl:value-of select="@local-name-element"/> = maker.<xsl:value-of select="$entity/@build-method"/>(child);<xsl:value-of select="$empty-line"/>
                  <xsl:value-of select="$empty-line"/>
                  <xsl:value-of select="$indent"/>      if (linker.<xsl:value-of select="@on-event-method"/>(parent, <xsl:value-of select="@local-name-element"/>)) {<xsl:value-of select="$empty-line"/>
                  <xsl:value-of select="$indent"/>         parseFor<xsl:value-of select="$entity/@entity-class"/>(maker, linker, <xsl:value-of select="@local-name-element"/>, child);<xsl:value-of select="$empty-line"/>
                  <xsl:value-of select="$indent"/>      }<xsl:value-of select="$empty-line"/>
                  <xsl:value-of select="$indent"/>   }<xsl:value-of select="$empty"/>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:value-of select="$empty"/>if (child.getNodeName().equals(<xsl:value-of select="$entity/@upper-name"/>)) {<xsl:value-of select="$empty-line"/>
                  <xsl:value-of select="$indent"/>   <xsl:value-of select="'      '"/><xsl:value-of select="$entity/@entity-class"/><xsl:value-of select="$space"/><xsl:value-of select="@local-name-element"/> = maker.<xsl:value-of select="$entity/@build-method"/>(child);<xsl:value-of select="$empty-line"/>
                  <xsl:value-of select="$empty-line"/>
                  <xsl:value-of select="$indent"/>      if (linker.<xsl:value-of select="@on-event-method"/>(parent, <xsl:value-of select="@local-name-element"/>)) {<xsl:value-of select="$empty-line"/>
                  <xsl:value-of select="$indent"/>         parseFor<xsl:value-of select="$entity/@entity-class"/>(maker, linker, <xsl:value-of select="@local-name-element"/>, child);<xsl:value-of select="$empty-line"/>
                  <xsl:value-of select="$indent"/>      }<xsl:value-of select="$empty-line"/>
                  <xsl:value-of select="$indent"/>   }<xsl:value-of select="$empty"/>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:for-each>
         <xsl:value-of select="$empty-line"/><xsl:value-of select="$indent"/>}<xsl:value-of select="$empty-line"/>
      </xsl:when>
      <xsl:otherwise>
         <xsl:variable name="refs" select="entity-ref[(@list='true' or @map='true') and @xml-indent='true' and not(@render='false')] | element[not(@text='true') and not(@render='false')]"/>
         <xsl:if test="$refs">
            <xsl:value-of select="$indent"/>if (<xsl:value-of select="$empty"/>
            <xsl:for-each select="entity-ref[(@list='true' or @map='true') and @xml-indent='true' and not(@render='false')] | element[not(@text='true') and not(@render='false')]">
               <xsl:value-of select="@upper-name"/>.equals(qName)<xsl:value-of select="$empty"/>
               <xsl:if test="position()!=last()"> || </xsl:if>
            </xsl:for-each>
            <xsl:value-of select="$empty"/>) {<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$indent"/>   m_objs.push(parentObj);<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$indent"/>} else <xsl:value-of select="$empty"/>
         </xsl:if>
         <xsl:for-each select="entity-ref[not(@render='false')]">
            <xsl:variable name="name" select="@name"/>
            <xsl:variable name="entity" select="//entity[@name=$name]"/>
            <xsl:if test="position()=1 and not($refs)">
               <xsl:value-of select="$indent"/>
            </xsl:if>
            <xsl:value-of select="$empty"/>if (<xsl:value-of select="$entity/@upper-name"/>.equals(qName)) {<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$indent"/>   <xsl:value-of select="'   '"/><xsl:value-of select="$entity/@entity-class"/><xsl:value-of select="$space"/><xsl:value-of select="@local-name-element"/> = m_maker.<xsl:value-of select="$entity/@build-method"/>(attributes);<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$indent"/>   m_linker.<xsl:value-of select="@on-event-method"/>(parentObj, <xsl:value-of select="@local-name-element"/>);<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$indent"/>   m_objs.push(<xsl:value-of select="@local-name-element"/>);<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$indent"/>} else <xsl:value-of select="$empty"/>
         </xsl:for-each>
         <xsl:choose>
            <xsl:when test="entity-ref[not(@render='false')] | element[not(@text='true') and not(@render='false')]">
               <xsl:value-of select="$empty"/>{<xsl:value-of select="$empty-line"/>
               <xsl:value-of select="$indent"/>   throw new SAXException(String.format("Element(%s) is not expected under <xsl:value-of select="$current/@name"/>!", qName));<xsl:value-of select="$empty-line"/>
               <xsl:value-of select="$indent"/>}<xsl:value-of select="$empty-line"/>
               <xsl:value-of select="$empty-line"/>
               <xsl:value-of select="$indent"/>m_tags.push(qName);<xsl:value-of select="$empty-line"/>
            </xsl:when>
            <xsl:otherwise>
               <xsl:value-of select="$indent"/>m_objs.push(parentObj);<xsl:value-of select="$empty-line"/>
               <xsl:value-of select="$indent"/>m_tags.push(qName);<xsl:value-of select="$empty-line"/>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:otherwise>
   </xsl:choose>
</xsl:template>

<xsl:template name="method-start-element">
   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      if (uri == null || uri.length() == 0) {
         if (m_objs.isEmpty()) { // root
            parse(qName, attributes);
         } else {
            Object parent = m_objs.peek();
            String tag = m_tags.peek();<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty-line"/>
            <xsl:for-each select="entity">
               <xsl:if test="position()=1"><xsl:value-of select="'            '"/></xsl:if>
               <xsl:value-of select="$empty"/>if (parent instanceof <xsl:value-of select="@entity-class"/>) {<xsl:value-of select="$empty-line"/>
               <xsl:value-of select="$empty"/>               <xsl:value-of select="'               '"/><xsl:value-of select="@parse-for-method"/>((<xsl:value-of select="@entity-class"/>) parent, tag, qName, attributes);<xsl:value-of select="$empty-line"/>
               <xsl:value-of select="$empty"/>            } else <xsl:value-of select="$empty"/>
            </xsl:for-each>
            <xsl:value-of select="$empty"/>{<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>               throw new RuntimeException(String.format("Unknown entity(%s) under %s!", qName, parent.getClass().getName()));<xsl:value-of select="$empty-line"/>
            <xsl:value-of select="$empty"/>            }<xsl:value-of select="$empty"/>
         }
      } else {
         throw new SAXException(String.format("Namespace(%s) is not supported by " + this.getClass().getName(), uri));
      }
   }
</xsl:template>

</xsl:stylesheet>
