package com.site.maven.plugin.codegen.dbconfig.transform;

import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_EXCLUDE;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ELEMENT_INCLUDE;

import static com.site.maven.plugin.codegen.dbconfig.Constants.ENTITY_CONFIG;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ENTITY_DATA_SOURCES;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ENTITY_EXCLUDES;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ENTITY_INCLUDES;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ENTITY_MAPPINGS;
import static com.site.maven.plugin.codegen.dbconfig.Constants.ENTITY_PROPERTIES;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.site.maven.plugin.codegen.dbconfig.entity.Config;
import com.site.maven.plugin.codegen.dbconfig.entity.DataSource;
import com.site.maven.plugin.codegen.dbconfig.entity.Excludes;
import com.site.maven.plugin.codegen.dbconfig.entity.Includes;
import com.site.maven.plugin.codegen.dbconfig.entity.Mapping;
import com.site.maven.plugin.codegen.dbconfig.entity.Properties;

public class DefaultParser implements IParser<Node> {

   protected Node getChildTagNode(Node parent, String name) {
      NodeList children = parent.getChildNodes();
      int len = children.getLength();

      for (int i = 0; i < len; i++) {
         Node child = children.item(i);

         if (child.getNodeType() == Node.ELEMENT_NODE) {
            if (child.getNodeName().equals(name)) {
               return child;
            }
         }
      }

      return null;
   }

   protected List<Node> getChildTagNodes(Node parent, String name) {
      NodeList children = parent.getChildNodes();
      int len = children.getLength();
      List<Node> nodes = new ArrayList<Node>(len);

      for (int i = 0; i < len; i++) {
         Node child = children.item(i);

         if (child.getNodeType() == Node.ELEMENT_NODE) {
            if (name == null || child.getNodeName().equals(name)) {
               nodes.add(child);
            }
         }
      }

      return nodes;
   }

   protected List<Node> getGrandChildTagNodes(Node parent, String name) {
      Node child = getChildTagNode(parent, name);
      NodeList children = child.getChildNodes();
      int len = children.getLength();
      List<Node> nodes = new ArrayList<Node>(len);

      for (int i = 0; i < len; i++) {
         Node grandChild = children.item(i);

         if (grandChild.getNodeType() == Node.ELEMENT_NODE) {
            nodes.add(child);
         }
      }

      return nodes;
   }

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

   public Config parse(Node node) {
      return parse(new DefaultMaker(), new DefaultLinker(), node);
   }

   public Config parse(String xml) throws SAXException, IOException {
      Node doc = getDocument(xml);
      Node rootNode = getChildTagNode(doc, ENTITY_CONFIG);

      if (rootNode == null) {
         throw new RuntimeException(String.format("config element(%s) is expected!", ENTITY_CONFIG));
      }

      return new DefaultParser().parse(new DefaultMaker(), new DefaultLinker(), rootNode);
   }

   public Config parse(IMaker<Node> maker, ILinker linker, Node node) {
      Config config = maker.buildConfig(node);

      if (node != null) {
         Config parent = config;

         for (Node child : getGrandChildTagNodes(node, ENTITY_MAPPINGS)) {
            Mapping mapping = maker.buildMapping(child);

            if (linker.onMapping(parent, mapping)) {
               parseForMapping(maker, linker, mapping, child);
            }
         }

         Node includesNode = getChildTagNode(node, ENTITY_INCLUDES);

         if (includesNode != null) {
            Includes includes = maker.buildIncludes(includesNode);

            if (linker.onIncludes(parent, includes)) {
               parseForIncludes(maker, linker, includes, includesNode);
            }
         }

         Node excludesNode = getChildTagNode(node, ENTITY_EXCLUDES);

         if (excludesNode != null) {
            Excludes excludes = maker.buildExcludes(excludesNode);

            if (linker.onExcludes(parent, excludes)) {
               parseForExcludes(maker, linker, excludes, excludesNode);
            }
         }

         for (Node child : getGrandChildTagNodes(node, ENTITY_DATA_SOURCES)) {
            DataSource dataSource = maker.buildDataSource(child);

            if (linker.onDataSource(parent, dataSource)) {
               parseForDataSource(maker, linker, dataSource, child);
            }
         }
      }

      return config;
   }

   public void parseForDataSource(IMaker<Node> maker, ILinker linker, DataSource parent, Node node) {
      Node propertiesNode = getChildTagNode(node, ENTITY_PROPERTIES);

      if (propertiesNode != null) {
         Properties properties = maker.buildProperties(propertiesNode);

         if (linker.onProperties(parent, properties)) {
            parseForProperties(maker, linker, properties, propertiesNode);
         }
      }
   }

   public void parseForExcludes(IMaker<Node> maker, ILinker linker, Excludes parent, Node node) {
      for (Node child : getChildTagNodes(node, ELEMENT_EXCLUDE)) {
         String exclude = maker.buildExclude(child);

         parent.addExclude(exclude);
      }
   }

   public void parseForIncludes(IMaker<Node> maker, ILinker linker, Includes parent, Node node) {
      for (Node child : getChildTagNodes(node, ELEMENT_INCLUDE)) {
         String include = maker.buildInclude(child);

         parent.addInclude(include);
      }
   }

   public void parseForMapping(IMaker<Node> maker, ILinker linker, Mapping parent, Node node) {
   }

   public void parseForProperties(IMaker<Node> maker, ILinker linker, Properties parent, Node node) {
   }
}
