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

public interface IParser<T> {
   public WorkbookEntity parse(IMaker<T> maker, ILinker linker, T node);

   public void parseForColEntity(IMaker<T> maker, ILinker linker, ColEntity parent, T node);

   public void parseForDateTimeEntity(IMaker<T> maker, ILinker linker, DateTimeEntity parent, T node);

   public void parseForEmptyEntity(IMaker<T> maker, ILinker linker, EmptyEntity parent, T node);

   public void parseForFontEntity(IMaker<T> maker, ILinker linker, FontEntity parent, T node);

   public void parseForFormatEntity(IMaker<T> maker, ILinker linker, FormatEntity parent, T node);

   public void parseForLabelEntity(IMaker<T> maker, ILinker linker, LabelEntity parent, T node);

   public void parseForNumberEntity(IMaker<T> maker, ILinker linker, NumberEntity parent, T node);

   public void parseForPatternEntity(IMaker<T> maker, ILinker linker, PatternEntity parent, T node);

   public void parseForRowEntity(IMaker<T> maker, ILinker linker, RowEntity parent, T node);

   public void parseForSheetEntity(IMaker<T> maker, ILinker linker, SheetEntity parent, T node);
}
