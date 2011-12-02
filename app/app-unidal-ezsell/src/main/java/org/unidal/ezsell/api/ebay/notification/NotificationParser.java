package org.unidal.ezsell.api.ebay.notification;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.site.dal.xml.XmlException;
import com.site.dal.xml.parser.XmlParser;

public class NotificationParser implements XmlParser {
   public List<NotificationEvent> parse(InputSource source) throws XmlException {
      List<NotificationEvent> events = new ArrayList<NotificationEvent>();

      try {
         DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         XPath xpath = XPathFactory.newInstance().newXPath();
         Document doc = builder.parse(source);
         NodeList responses = (NodeList) xpath.evaluate("Envelope/Body/node()", doc, XPathConstants.NODESET);
         int size = responses.getLength();

         for (int i = 0; i < size; i++) {
            Node response = responses.item(i);
            String eventName = xpath.evaluate("NotificationEventName/text()", response);
            String userId = xpath.evaluate("RecipientUserID/text()", response);

            if (eventName.length() > 0) {
               NotificationEvent event = new NotificationEvent(eventName, userId, toXmlString(response));

               events.add(event);
            }
         }

         return events;
      } catch (Exception e) {
         throw new XmlException("Error when parsing XML data. " + e.getMessage(), e);
      }
   }

   private String toXmlString(Node node) throws TransformerException {
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer t = tf.newTransformer();
      ByteArrayOutputStream bos = new ByteArrayOutputStream();

      t.setOutputProperty("encoding", "utf-8");
      t.transform(new DOMSource(node), new StreamResult(bos));

      return bos.toString();
   }
}
