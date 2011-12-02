package com.site.wdbc.query;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.site.wdbc.WdbcException;
import com.site.wdbc.WdbcResult;
import com.site.wdbc.WdbcSource;

public class XmlWdbcParser implements WdbcParser {

   public WdbcResult parse(WdbcHandler handler, WdbcSource source) throws WdbcException {
      try {
         XmlParserCallback cb = new XmlParserCallback(handler);
         InputSource inputSource = new InputSource(source.getReader());

         SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
         XMLReader reader = parser.getXMLReader();

         reader.setFeature("http://xml.org/sax/features/namespaces", true);

         String parserClass = parser.getClass().getName();

         if (parserClass.equals("com.sun.org.apache.xerces.internal.jaxp.SAXParserImpl")
               || parserClass.equals("org.apache.xerces.jaxp.SAXParserImpl")) {
            // disable DTD validate
            String feature = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
            reader.setFeature(feature, false);
         }

         reader.setContentHandler(cb);
         reader.setErrorHandler(cb);
         reader.setDTDHandler(cb);
         reader.setEntityResolver(cb);

         reader.parse(inputSource);

         WdbcResult result = handler.getResult();

         return result;
      } catch (Exception e) {
         throw new WdbcException("Error while parsing XML, message: " + e, e);
      }
   }
}
