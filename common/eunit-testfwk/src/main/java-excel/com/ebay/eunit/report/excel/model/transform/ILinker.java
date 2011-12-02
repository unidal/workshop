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

public interface ILinker {

   public boolean onCol(SheetEntity parent, ColEntity col);

   public boolean onDateTime(RowEntity parent, DateTimeEntity dateTime);

   public boolean onDateTime(ColEntity parent, DateTimeEntity dateTime);

   public boolean onEmpty(SheetEntity parent, EmptyEntity empty);

   public boolean onEmpty(RowEntity parent, EmptyEntity empty);

   public boolean onEmpty(ColEntity parent, EmptyEntity empty);

   public boolean onFont(FormatEntity parent, FontEntity font);

   public boolean onFormat(SheetEntity parent, FormatEntity format);

   public boolean onLabel(RowEntity parent, LabelEntity label);

   public boolean onLabel(ColEntity parent, LabelEntity label);

   public boolean onNumber(RowEntity parent, NumberEntity number);

   public boolean onNumber(ColEntity parent, NumberEntity number);

   public boolean onPattern(FormatEntity parent, PatternEntity pattern);

   public boolean onRow(SheetEntity parent, RowEntity row);

   public boolean onSheet(WorkbookEntity parent, SheetEntity sheet);
}
