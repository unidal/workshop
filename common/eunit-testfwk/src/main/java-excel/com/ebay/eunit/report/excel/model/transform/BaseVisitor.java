package com.ebay.eunit.report.excel.model.transform;

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

public abstract class BaseVisitor implements IVisitor {
   @Override
   public void visitCol(ColEntity col) {
      for (BaseEntity<?> child : col.getAllChildrenInSequence()) {
         child.accept(this);
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
      for (BaseEntity<?> child : row.getAllChildrenInSequence()) {
         child.accept(this);
      }
   }

   @Override
   public void visitSheet(SheetEntity sheet) {
      for (BaseEntity<?> child : sheet.getAllChildrenInSequence()) {
         child.accept(this);
      }
   }

   @Override
   public void visitWorkbook(WorkbookEntity workbook) {
      for (SheetEntity sheet : workbook.getSheets()) {
         visitSheet(sheet);
      }
   }
}
