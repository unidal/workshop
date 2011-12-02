package com.ebay.eunit.report.excel.model.transform;

import static com.ebay.eunit.report.excel.model.Constants.ATTR_ID;
import static com.ebay.eunit.report.excel.model.Constants.ENTITY_COL;
import static com.ebay.eunit.report.excel.model.Constants.ENTITY_FORMAT;
import static com.ebay.eunit.report.excel.model.Constants.ENTITY_ROW;
import static com.ebay.eunit.report.excel.model.Constants.ENTITY_SHEET;
import static com.ebay.eunit.report.excel.model.Constants.ENTITY_WORKBOOK;

import java.util.Stack;

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

public class DefaultValidator implements IVisitor {

   private Path m_path = new Path();
   
   protected void assertRequired(String name, Object value) {
      if (value == null) {
         throw new RuntimeException(String.format("%s at path(%s) is required!", name, m_path));
      }
   }

   @Override
   public void visitCol(ColEntity col) {
      m_path.down(ENTITY_COL);

      visitColChildren(col);

      m_path.up(ENTITY_COL);
   }

   protected void visitColChildren(ColEntity col) {
      for (LabelEntity label : col.getLabels()) {
         visitLabel(label);
      }

      for (NumberEntity number : col.getNumbers()) {
         visitNumber(number);
      }

      for (EmptyEntity empty : col.getEmptyList()) {
         visitEmpty(empty);
      }

      for (DateTimeEntity dateTime : col.getDateTimes()) {
         visitDateTime(dateTime);
      }
   }

   @Override
   public void visitDateTime(DateTimeEntity dateTime) {
   }

   @Override
   public void visitEmpty(EmptyEntity empty) {
   }

   @Override
   public void visitFont(FontEntity font) {
   }

   @Override
   public void visitFormat(FormatEntity format) {
      m_path.down(ENTITY_FORMAT);

      assertRequired(ATTR_ID, format.getId());

      visitFormatChildren(format);

      m_path.up(ENTITY_FORMAT);
   }

   protected void visitFormatChildren(FormatEntity format) {
      if (format.getFont() != null) {
         visitFont(format.getFont());
      }

      if (format.getPattern() != null) {
         visitPattern(format.getPattern());
      }
   }

   @Override
   public void visitLabel(LabelEntity label) {
   }

   @Override
   public void visitNumber(NumberEntity number) {
   }

   @Override
   public void visitPattern(PatternEntity pattern) {
   }

   @Override
   public void visitRow(RowEntity row) {
      m_path.down(ENTITY_ROW);

      visitRowChildren(row);

      m_path.up(ENTITY_ROW);
   }

   protected void visitRowChildren(RowEntity row) {
      for (LabelEntity label : row.getLabels()) {
         visitLabel(label);
      }

      for (NumberEntity number : row.getNumbers()) {
         visitNumber(number);
      }

      for (EmptyEntity empty : row.getEmptyList()) {
         visitEmpty(empty);
      }

      for (DateTimeEntity dateTime : row.getDateTimes()) {
         visitDateTime(dateTime);
      }
   }

   @Override
   public void visitSheet(SheetEntity sheet) {
      m_path.down(ENTITY_SHEET);

      visitSheetChildren(sheet);

      m_path.up(ENTITY_SHEET);
   }

   protected void visitSheetChildren(SheetEntity sheet) {
      for (FormatEntity format : sheet.getFormats()) {
         visitFormat(format);
      }

      for (RowEntity row : sheet.getRows()) {
         visitRow(row);
      }

      for (EmptyEntity empty : sheet.getEmptyList()) {
         visitEmpty(empty);
      }

      for (ColEntity col : sheet.getCols()) {
         visitCol(col);
      }
   }

   @Override
   public void visitWorkbook(WorkbookEntity workbook) {
      m_path.down(ENTITY_WORKBOOK);

      visitWorkbookChildren(workbook);

      m_path.up(ENTITY_WORKBOOK);
   }

   protected void visitWorkbookChildren(WorkbookEntity workbook) {
      for (SheetEntity sheet : workbook.getSheets()) {
         visitSheet(sheet);
      }
   }

   static class Path {
      private Stack<String> m_sections = new Stack<String>();

      public Path down(String nextSection) {
         m_sections.push(nextSection);

         return this;
      }

      @Override
      public String toString() {
         StringBuilder sb = new StringBuilder();

         for (String section : m_sections) {
            sb.append('/').append(section);
         }

         return sb.toString();
      }

      public Path up(String currentSection) {
         if (m_sections.isEmpty() || !m_sections.peek().equals(currentSection)) {
            throw new RuntimeException("INTERNAL ERROR: stack mismatched!");
         }

         m_sections.pop();
         return this;
      }
   }
}
