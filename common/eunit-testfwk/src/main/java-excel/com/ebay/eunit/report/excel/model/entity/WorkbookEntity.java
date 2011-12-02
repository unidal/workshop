package com.ebay.eunit.report.excel.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.ebay.eunit.report.excel.model.BaseEntity;
import com.ebay.eunit.report.excel.model.IVisitor;

public class WorkbookEntity extends BaseEntity<WorkbookEntity> {
   private String m_output;

   private String m_template;

   private List<SheetEntity> m_sheets = new ArrayList<SheetEntity>();

   public WorkbookEntity() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitWorkbook(this);
   }

   public WorkbookEntity addSheet(SheetEntity sheet) {
      m_sheets.add(sheet);
      return this;
   }

   public String getOutput() {
      return m_output;
   }

   public List<SheetEntity> getSheets() {
      return m_sheets;
   }

   public String getTemplate() {
      return m_template;
   }

   @Override
   public void mergeAttributes(WorkbookEntity other) {
      if (other.getOutput() != null) {
         m_output = other.getOutput();
      }

      if (other.getTemplate() != null) {
         m_template = other.getTemplate();
      }
   }

   public WorkbookEntity setOutput(String output) {
      m_output=output;
      return this;
   }

   public WorkbookEntity setTemplate(String template) {
      m_template=template;
      return this;
   }

}
