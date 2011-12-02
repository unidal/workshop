package com.ebay.eunit.report.excel.model.transform;

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

public class DefaultLinker implements ILinker {

   @Override
   public boolean onCol(SheetEntity parent, ColEntity col) {
      parent.addCol(col);
      return true;
   }

   @Override
   public boolean onDateTime(RowEntity parent, DateTimeEntity dateTime) {
      parent.addDateTime(dateTime);
      return true;
   }

   @Override
   public boolean onDateTime(ColEntity parent, DateTimeEntity dateTime) {
      parent.addDateTime(dateTime);
      return true;
   }

   @Override
   public boolean onEmpty(SheetEntity parent, EmptyEntity empty) {
      parent.addEmpty(empty);
      return true;
   }

   @Override
   public boolean onEmpty(RowEntity parent, EmptyEntity empty) {
      parent.addEmpty(empty);
      return true;
   }

   @Override
   public boolean onEmpty(ColEntity parent, EmptyEntity empty) {
      parent.addEmpty(empty);
      return true;
   }

   @Override
   public boolean onFont(FormatEntity parent, FontEntity font) {
      parent.setFont(font);
      return true;
   }

   @Override
   public boolean onFormat(SheetEntity parent, FormatEntity format) {
      parent.addFormat(format);
      return true;
   }

   @Override
   public boolean onLabel(RowEntity parent, LabelEntity label) {
      parent.addLabel(label);
      return true;
   }

   @Override
   public boolean onLabel(ColEntity parent, LabelEntity label) {
      parent.addLabel(label);
      return true;
   }

   @Override
   public boolean onNumber(RowEntity parent, NumberEntity number) {
      parent.addNumber(number);
      return true;
   }

   @Override
   public boolean onNumber(ColEntity parent, NumberEntity number) {
      parent.addNumber(number);
      return true;
   }

   @Override
   public boolean onPattern(FormatEntity parent, PatternEntity pattern) {
      parent.setPattern(pattern);
      return true;
   }

   @Override
   public boolean onRow(SheetEntity parent, RowEntity row) {
      parent.addRow(row);
      return true;
   }

   @Override
   public boolean onSheet(WorkbookEntity parent, SheetEntity sheet) {
      parent.addSheet(sheet);
      return true;
   }
}
