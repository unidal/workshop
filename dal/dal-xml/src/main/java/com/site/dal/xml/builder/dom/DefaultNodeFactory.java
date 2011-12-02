package com.site.dal.xml.builder.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;

public class DefaultNodeFactory implements NodeFactory {
   public Text createText(Node parent, String data) {
      Text text = parent.getOwnerDocument().createTextNode(data);

      parent.appendChild(text);
      return text;
   }

   public Element createElement(Node parent, String name, Attributes attributes, Node beforeChild) {
      Document doc = (parent instanceof Document ? (Document) parent : parent.getOwnerDocument());
      Element node = doc.createElement(name);

      if (attributes != null) {
         int len = attributes.getLength();
         
         for (int i = 0; i < len; i++) {
            node.setAttribute(attributes.getQName(i), attributes.getValue(i));
         }
      }

      if (beforeChild == null) {
         parent.appendChild(node);
      } else {
         parent.insertBefore(node, beforeChild);
      }
      return node;
   }

   public Document createDocument() {
      return new DocumentImpl();
   }
}
