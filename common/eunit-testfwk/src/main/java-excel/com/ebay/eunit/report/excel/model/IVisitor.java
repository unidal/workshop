package com.ebay.eunit.report.excel.model;

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

public interface IVisitor {

   public void visitCol(ColEntity col);

   public void visitDateTime(DateTimeEntity dateTime);

   public void visitEmpty(EmptyEntity empty);

   public void visitFont(FontEntity font);

   public void visitFormat(FormatEntity format);

   public void visitLabel(LabelEntity label);

   public void visitNumber(NumberEntity number);

   public void visitPattern(PatternEntity pattern);

   public void visitRow(RowEntity row);

   public void visitSheet(SheetEntity sheet);

   public void visitWorkbook(WorkbookEntity workbook);
}
