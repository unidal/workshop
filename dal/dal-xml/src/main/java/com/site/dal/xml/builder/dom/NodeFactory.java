package com.site.dal.xml.builder.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;

public interface NodeFactory {
   public Text createText(Node parent, String data);

   public Element createElement(Node parent, String name, Attributes attributes, Node beforeChild);

   public Document createDocument();
}
