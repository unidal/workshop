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

public interface IMaker<T> {

   public ColEntity buildCol(T node);

   public DateTimeEntity buildDateTime(T node);

   public EmptyEntity buildEmpty(T node);

   public FontEntity buildFont(T node);

   public FormatEntity buildFormat(T node);

   public LabelEntity buildLabel(T node);

   public NumberEntity buildNumber(T node);

   public PatternEntity buildPattern(T node);

   public RowEntity buildRow(T node);

   public SheetEntity buildSheet(T node);

   public WorkbookEntity buildWorkbook(T node);
}
