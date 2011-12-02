package com.ebay.eunit.report.excel.model.transform;

import static com.ebay.eunit.report.excel.model.Constants.ATTR_BOLD;
import static com.ebay.eunit.report.excel.model.Constants.ATTR_COLS;
import static com.ebay.eunit.report.excel.model.Constants.ATTR_CREATE;
import static com.ebay.eunit.report.excel.model.Constants.ATTR_FORMAT;
import static com.ebay.eunit.report.excel.model.Constants.ATTR_ID;
import static com.ebay.eunit.report.excel.model.Constants.ATTR_ITALIC;
import static com.ebay.eunit.report.excel.model.Constants.ATTR_NAME;
import static com.ebay.eunit.report.excel.model.Constants.ATTR_OFFSET;
import static com.ebay.eunit.report.excel.model.Constants.ATTR_OUTPUT;
import static com.ebay.eunit.report.excel.model.Constants.ATTR_ROWS;
import static com.ebay.eunit.report.excel.model.Constants.ATTR_SIZE;
import static com.ebay.eunit.report.excel.model.Constants.ATTR_START_COL;
import static com.ebay.eunit.report.excel.model.Constants.ATTR_START_ROW;
import static com.ebay.eunit.report.excel.model.Constants.ATTR_TEMPLATE;
import static com.ebay.eunit.report.excel.model.Constants.ATTR_TYPE;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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

public class DefaultMaker implements IMaker<Node> {

   @Override
   public ColEntity buildCol(Node node) {
      String startRow = getAttribute(node, ATTR_START_ROW);
      String startCol = getAttribute(node, ATTR_START_COL);
      String create = getAttribute(node, ATTR_CREATE);

      ColEntity col = new ColEntity();

      if (startRow != null) {
         col.setStartRow(Integer.valueOf(startRow));
      }

      if (startCol != null) {
         col.setStartCol(Integer.valueOf(startCol));
      }

      if (create != null) {
         col.setCreate(Boolean.valueOf(create));
      }

      return col;
   }

   @Override
   public DateTimeEntity buildDateTime(Node node) {
      String format = getAttribute(node, ATTR_FORMAT);
      String offset = getAttribute(node, ATTR_OFFSET);

      DateTimeEntity dateTime = new DateTimeEntity();

      if (format != null) {
         dateTime.setFormat(format);
      }

      if (offset != null) {
         dateTime.setOffset(Integer.valueOf(offset));
      }

      dateTime.setText(getText(node));

      return dateTime;
   }

   @Override
   public EmptyEntity buildEmpty(Node node) {
      String cols = getAttribute(node, ATTR_COLS);
      String rows = getAttribute(node, ATTR_ROWS);

      EmptyEntity empty = new EmptyEntity();

      if (cols != null) {
         empty.setCols(Integer.valueOf(cols));
      }

      if (rows != null) {
         empty.setRows(Integer.valueOf(rows));
      }

      return empty;
   }

   @Override
   public FontEntity buildFont(Node node) {
      String name = getAttribute(node, ATTR_NAME);
      String size = getAttribute(node, ATTR_SIZE);
      String bold = getAttribute(node, ATTR_BOLD);
      String italic = getAttribute(node, ATTR_ITALIC);

      FontEntity font = new FontEntity();

      if (name != null) {
         font.setName(name);
      }

      if (size != null) {
         font.setSize(Integer.valueOf(size));
      }

      if (bold != null) {
         font.setBold(Boolean.valueOf(bold));
      }

      if (italic != null) {
         font.setItalic(Boolean.valueOf(italic));
      }

      return font;
   }

   @Override
   public FormatEntity buildFormat(Node node) {
      String id = getAttribute(node, ATTR_ID);

      return new FormatEntity(id);
   }

   @Override
   public LabelEntity buildLabel(Node node) {
      String format = getAttribute(node, ATTR_FORMAT);

      LabelEntity label = new LabelEntity();

      if (format != null) {
         label.setFormat(format);
      }

      label.setText(getText(node));

      return label;
   }

   @Override
   public NumberEntity buildNumber(Node node) {
      String format = getAttribute(node, ATTR_FORMAT);

      NumberEntity number = new NumberEntity();

      if (format != null) {
         number.setFormat(format);
      }

      number.setText(Double.valueOf(getText(node)));

      return number;
   }

   @Override
   public PatternEntity buildPattern(Node node) {
      String type = getAttribute(node, ATTR_TYPE);

      PatternEntity pattern = new PatternEntity();

      if (type != null) {
         pattern.setType(type);
      }

      pattern.setText(getText(node));

      return pattern;
   }

   @Override
   public RowEntity buildRow(Node node) {
      String startCol = getAttribute(node, ATTR_START_COL);
      String startRow = getAttribute(node, ATTR_START_ROW);
      String create = getAttribute(node, ATTR_CREATE);

      RowEntity row = new RowEntity();

      if (startCol != null) {
         row.setStartCol(Integer.valueOf(startCol));
      }

      if (startRow != null) {
         row.setStartRow(Integer.valueOf(startRow));
      }

      if (create != null) {
         row.setCreate(Boolean.valueOf(create));
      }

      return row;
   }

   @Override
   public SheetEntity buildSheet(Node node) {
      String id = getAttribute(node, ATTR_ID);
      String name = getAttribute(node, ATTR_NAME);

      SheetEntity sheet = new SheetEntity();

      if (id != null) {
         sheet.setId(Integer.valueOf(id));
      }

      if (name != null) {
         sheet.setName(name);
      }

      return sheet;
   }

   @Override
   public WorkbookEntity buildWorkbook(Node node) {
      String output = getAttribute(node, ATTR_OUTPUT);
      String template = getAttribute(node, ATTR_TEMPLATE);

      WorkbookEntity workbook = new WorkbookEntity();

      if (output != null) {
         workbook.setOutput(output);
      }

      if (template != null) {
         workbook.setTemplate(template);
      }

      return workbook;
   }

   protected String getAttribute(Node node, String name) {
      Node attribute = node.getAttributes().getNamedItem(name);

      return attribute == null ? null : attribute.getNodeValue();
   }

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

   protected String getText(Node node) {
      if (node != null) {
         StringBuilder sb = new StringBuilder();
         NodeList children = node.getChildNodes();
         int len = children.getLength();

         for (int i = 0; i < len; i++) {
            Node child = children.item(i);

            if (child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == Node.CDATA_SECTION_NODE) {
               sb.append(child.getNodeValue());
            }
         }

         if (sb.length() != 0) {
            return sb.toString();
         }
      }

      return null;
   }
}
