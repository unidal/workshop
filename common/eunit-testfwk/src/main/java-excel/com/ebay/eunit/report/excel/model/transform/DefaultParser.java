package com.ebay.eunit.report.excel.model.transform;

import static com.ebay.eunit.report.excel.model.Constants.ENTITY_COL;
import static com.ebay.eunit.report.excel.model.Constants.ENTITY_DATE_TIME;
import static com.ebay.eunit.report.excel.model.Constants.ENTITY_EMPTY;
import static com.ebay.eunit.report.excel.model.Constants.ENTITY_FONT;
import static com.ebay.eunit.report.excel.model.Constants.ENTITY_FORMAT;
import static com.ebay.eunit.report.excel.model.Constants.ENTITY_LABEL;
import static com.ebay.eunit.report.excel.model.Constants.ENTITY_NUMBER;
import static com.ebay.eunit.report.excel.model.Constants.ENTITY_PATTERN;
import static com.ebay.eunit.report.excel.model.Constants.ENTITY_ROW;
import static com.ebay.eunit.report.excel.model.Constants.ENTITY_SHEET;
import static com.ebay.eunit.report.excel.model.Constants.ENTITY_WORKBOOK;

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
import com.ebay.eunit.report.excel.model.entity.ColEntity;
import com.ebay.eunit.report.excel.model.entity.DateTimeEntity;
import com.ebay.eunit.report.excel.model.entity.EmptyEntity;
import com.ebay.eunit.report.excel.model.entity.FontEntity;
import com.ebay.eunit.report.excel.model.entity.FormatEntity;
import com.ebay.eunit.report.excel.model.entity.LabelEntity;
import com.ebay.eunit.report.excel.model.entity.NumberEntity;
import com.ebay.eunit.report.excel.model.entity.PatternEntity;
import com.ebay.eunit.report.excel.model.entity.RowEntity;
import com.ebay.eunit.report.excel.model.entity.SheetEntity;
import com.ebay.eunit.report.excel.model.entity.WorkbookEntity;

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

   public WorkbookEntity parse(Node node) {
      return parse(new DefaultMaker(), new DefaultLinker(), node);
   }

   public WorkbookEntity parse(String xml) throws SAXException, IOException {
      Node doc = getDocument(xml);
      Node rootNode = getChildTagNode(doc, ENTITY_WORKBOOK);

      if (rootNode == null) {
         throw new RuntimeException(String.format("workbook element(%s) is expected!", ENTITY_WORKBOOK));
      }

      return new DefaultParser().parse(new DefaultMaker(), new DefaultLinker(), rootNode);
   }

   public WorkbookEntity parse(IMaker<Node> maker, ILinker linker, Node node) {
      WorkbookEntity workbook = maker.buildWorkbook(node);

      if (node != null) {
         WorkbookEntity parent = workbook;

         for (Node child : getChildTagNodes(node, ENTITY_SHEET)) {
            SheetEntity sheet = maker.buildSheet(child);

            if (linker.onSheet(parent, sheet)) {
               parseForSheetEntity(maker, linker, sheet, child);
            }
         }
      }

      return workbook;
   }

   public void parseForColEntity(IMaker<Node> maker, ILinker linker, ColEntity parent, Node node) {
      for (Node child : getChildTagNodes(node, null)) {
         if (child.getNodeName().equals(ENTITY_LABEL)) {
            LabelEntity label = maker.buildLabel(child);

            if (linker.onLabel(parent, label)) {
               parseForLabelEntity(maker, linker, label, child);
            }
         } else if (child.getNodeName().equals(ENTITY_NUMBER)) {
            NumberEntity number = maker.buildNumber(child);

            if (linker.onNumber(parent, number)) {
               parseForNumberEntity(maker, linker, number, child);
            }
         } else if (child.getNodeName().equals(ENTITY_EMPTY)) {
            EmptyEntity empty = maker.buildEmpty(child);

            if (linker.onEmpty(parent, empty)) {
               parseForEmptyEntity(maker, linker, empty, child);
            }
         } else if (child.getNodeName().equals(ENTITY_DATE_TIME)) {
            DateTimeEntity dateTime = maker.buildDateTime(child);

            if (linker.onDateTime(parent, dateTime)) {
               parseForDateTimeEntity(maker, linker, dateTime, child);
            }
         }
      }
   }

   public void parseForDateTimeEntity(IMaker<Node> maker, ILinker linker, DateTimeEntity parent, Node node) {
   }

   public void parseForEmptyEntity(IMaker<Node> maker, ILinker linker, EmptyEntity parent, Node node) {
   }

   public void parseForFontEntity(IMaker<Node> maker, ILinker linker, FontEntity parent, Node node) {
   }

   public void parseForFormatEntity(IMaker<Node> maker, ILinker linker, FormatEntity parent, Node node) {
      Node fontNode = getChildTagNode(node, ENTITY_FONT);

      if (fontNode != null) {
         FontEntity font = maker.buildFont(fontNode);

         if (linker.onFont(parent, font)) {
            parseForFontEntity(maker, linker, font, fontNode);
         }
      }

      Node patternNode = getChildTagNode(node, ENTITY_PATTERN);

      if (patternNode != null) {
         PatternEntity pattern = maker.buildPattern(patternNode);

         if (linker.onPattern(parent, pattern)) {
            parseForPatternEntity(maker, linker, pattern, patternNode);
         }
      }
   }

   public void parseForLabelEntity(IMaker<Node> maker, ILinker linker, LabelEntity parent, Node node) {
   }

   public void parseForNumberEntity(IMaker<Node> maker, ILinker linker, NumberEntity parent, Node node) {
   }

   public void parseForPatternEntity(IMaker<Node> maker, ILinker linker, PatternEntity parent, Node node) {
   }

   public void parseForRowEntity(IMaker<Node> maker, ILinker linker, RowEntity parent, Node node) {
      for (Node child : getChildTagNodes(node, null)) {
         if (child.getNodeName().equals(ENTITY_LABEL)) {
            LabelEntity label = maker.buildLabel(child);

            if (linker.onLabel(parent, label)) {
               parseForLabelEntity(maker, linker, label, child);
            }
         } else if (child.getNodeName().equals(ENTITY_NUMBER)) {
            NumberEntity number = maker.buildNumber(child);

            if (linker.onNumber(parent, number)) {
               parseForNumberEntity(maker, linker, number, child);
            }
         } else if (child.getNodeName().equals(ENTITY_EMPTY)) {
            EmptyEntity empty = maker.buildEmpty(child);

            if (linker.onEmpty(parent, empty)) {
               parseForEmptyEntity(maker, linker, empty, child);
            }
         } else if (child.getNodeName().equals(ENTITY_DATE_TIME)) {
            DateTimeEntity dateTime = maker.buildDateTime(child);

            if (linker.onDateTime(parent, dateTime)) {
               parseForDateTimeEntity(maker, linker, dateTime, child);
            }
         }
      }
   }

   public void parseForSheetEntity(IMaker<Node> maker, ILinker linker, SheetEntity parent, Node node) {
      for (Node child : getChildTagNodes(node, null)) {
         if (child.getNodeName().equals(ENTITY_FORMAT)) {
            FormatEntity format = maker.buildFormat(child);

            if (linker.onFormat(parent, format)) {
               parseForFormatEntity(maker, linker, format, child);
            }
         } else if (child.getNodeName().equals(ENTITY_ROW)) {
            RowEntity row = maker.buildRow(child);

            if (linker.onRow(parent, row)) {
               parseForRowEntity(maker, linker, row, child);
            }
         } else if (child.getNodeName().equals(ENTITY_EMPTY)) {
            EmptyEntity empty = maker.buildEmpty(child);

            if (linker.onEmpty(parent, empty)) {
               parseForEmptyEntity(maker, linker, empty, child);
            }
         } else if (child.getNodeName().equals(ENTITY_COL)) {
            ColEntity col = maker.buildCol(child);

            if (linker.onCol(parent, col)) {
               parseForColEntity(maker, linker, col, child);
            }
         }
      }
   }
}
