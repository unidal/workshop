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

import com.ebay.eunit.report.excel.model.BaseEntity;
import com.ebay.eunit.report.excel.model.IVisitor;
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

public class DefaultXmlBuilder implements IVisitor {

   private int m_level;

   private StringBuilder m_sb = new StringBuilder(2048);

   private void endTag(String name) {
      m_level--;

      indent();
      m_sb.append("</").append(name).append(">\r\n");
   }

   public String getString() {
      return m_sb.toString();
   }

   private void indent() {
      for (int i = m_level - 1; i >= 0; i--) {
         m_sb.append("   ");
      }
   }

   private void startTag(String name, boolean closed, Object... nameValues) {
      startTag(name, null, closed, nameValues);
   }

   private void startTag(String name, Object... nameValues) {
      startTag(name, false, nameValues);
   }
   
   private void startTag(String name, Object text, boolean closed, Object... nameValues) {
      indent();

      m_sb.append('<').append(name);

      int len = nameValues.length;

      for (int i = 0; i + 1 < len; i += 2) {
         Object attrName = nameValues[i];
         Object attrValue = nameValues[i + 1];

         if (attrValue != null) {
            m_sb.append(' ').append(attrName).append("=\"").append(attrValue).append('"');
         }
      }

      if (text != null && closed) {
         m_sb.append('>');
         m_sb.append(text == null ? "" : text);
         m_sb.append("</").append(name).append(">\r\n");
      } else {
         if (closed) {
            m_sb.append('/');
         } else {
            m_level++;
         }
   
         m_sb.append(">\r\n");
      }
   }

   @Override
   public void visitCol(ColEntity col) {
      startTag(ENTITY_COL, ATTR_START_ROW, col.getStartRow(), ATTR_START_COL, col.getStartCol(), ATTR_CREATE, col.getCreate());

      for (BaseEntity<?> child : col.getAllChildrenInSequence()) {
         child.accept(this);
      }

      endTag(ENTITY_COL);
   }

   @Override
   public void visitDateTime(DateTimeEntity dateTime) {
      startTag(ENTITY_DATE_TIME, dateTime.getText(), true, ATTR_FORMAT, dateTime.getFormat(), ATTR_OFFSET, dateTime.getOffset());
   }

   @Override
   public void visitEmpty(EmptyEntity empty) {
      startTag(ENTITY_EMPTY, true, ATTR_COLS, empty.getCols(), ATTR_ROWS, empty.getRows());
   }

   @Override
   public void visitFont(FontEntity font) {
      startTag(ENTITY_FONT, true, ATTR_NAME, font.getName(), ATTR_SIZE, font.getSize(), ATTR_BOLD, font.getBold(), ATTR_ITALIC, font.getItalic());
   }

   @Override
   public void visitFormat(FormatEntity format) {
      startTag(ENTITY_FORMAT, ATTR_ID, format.getId());

      if (format.getFont() != null) {
         visitFont(format.getFont());
      }

      if (format.getPattern() != null) {
         visitPattern(format.getPattern());
      }

      endTag(ENTITY_FORMAT);
   }

   @Override
   public void visitLabel(LabelEntity label) {
      startTag(ENTITY_LABEL, label.getText(), true, ATTR_FORMAT, label.getFormat());
   }

   @Override
   public void visitNumber(NumberEntity number) {
      startTag(ENTITY_NUMBER, number.getText(), true, ATTR_FORMAT, number.getFormat());
   }

   @Override
   public void visitPattern(PatternEntity pattern) {
      startTag(ENTITY_PATTERN, pattern.getText(), true, ATTR_TYPE, pattern.getType());
   }

   @Override
   public void visitRow(RowEntity row) {
      startTag(ENTITY_ROW, ATTR_START_COL, row.getStartCol(), ATTR_START_ROW, row.getStartRow(), ATTR_CREATE, row.getCreate());

      for (BaseEntity<?> child : row.getAllChildrenInSequence()) {
         child.accept(this);
      }

      endTag(ENTITY_ROW);
   }

   @Override
   public void visitSheet(SheetEntity sheet) {
      startTag(ENTITY_SHEET, ATTR_ID, sheet.getId(), ATTR_NAME, sheet.getName());

      for (BaseEntity<?> child : sheet.getAllChildrenInSequence()) {
         child.accept(this);
      }

      endTag(ENTITY_SHEET);
   }

   @Override
   public void visitWorkbook(WorkbookEntity workbook) {
      startTag(ENTITY_WORKBOOK, ATTR_OUTPUT, workbook.getOutput(), ATTR_TEMPLATE, workbook.getTemplate());

      if (!workbook.getSheets().isEmpty()) {
         for (SheetEntity sheet : workbook.getSheets()) {
            visitSheet(sheet);
         }
      }

      endTag(ENTITY_WORKBOOK);
   }
}
