package com.ebay.eunit.report.excel.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.ebay.eunit.report.excel.model.BaseEntity;
import com.ebay.eunit.report.excel.model.IVisitor;

public class SheetEntity extends BaseEntity<SheetEntity> {
   private List<BaseEntity<?>> m_allChildrenInSequence = new ArrayList<BaseEntity<?>>();

   private Integer m_id;

   private String m_name;

   private List<FormatEntity> m_formats = new ArrayList<FormatEntity>();

   private List<RowEntity> m_rows = new ArrayList<RowEntity>();

   private List<EmptyEntity> m_emptyList = new ArrayList<EmptyEntity>();

   private List<ColEntity> m_cols = new ArrayList<ColEntity>();

   public SheetEntity() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitSheet(this);
   }

   public SheetEntity addCol(ColEntity col) {
      m_allChildrenInSequence.add(col);
      m_cols.add(col);
      return this;
   }

   public SheetEntity addEmpty(EmptyEntity empty) {
      m_allChildrenInSequence.add(empty);
      m_emptyList.add(empty);
      return this;
   }

   public SheetEntity addFormat(FormatEntity format) {
      m_allChildrenInSequence.add(format);
      m_formats.add(format);
      return this;
   }

   public SheetEntity addRow(RowEntity row) {
      m_allChildrenInSequence.add(row);
      m_rows.add(row);
      return this;
   }

   public FormatEntity findFormat(String id) {
      for (FormatEntity format : m_formats) {
         if (!format.getId().equals(id)) {
            continue;
         }

         return format;
      }

      return null;
   }

   public List<BaseEntity<?>> getAllChildrenInSequence() {
      return m_allChildrenInSequence;
   }

   public List<ColEntity> getCols() {
      return m_cols;
   }

   public List<EmptyEntity> getEmptyList() {
      return m_emptyList;
   }

   public List<FormatEntity> getFormats() {
      return m_formats;
   }

   public Integer getId() {
      return m_id;
   }

   public String getName() {
      return m_name;
   }

   public List<RowEntity> getRows() {
      return m_rows;
   }

   @Override
   public void mergeAttributes(SheetEntity other) {
      if (other.getId() != null) {
         m_id = other.getId();
      }

      if (other.getName() != null) {
         m_name = other.getName();
      }
   }

   public boolean removeFormat(String id) {
      int len = m_formats.size();

      for (int i = 0; i < len; i++) {
         FormatEntity format = m_formats.get(i);

         if (!format.getId().equals(id)) {
            continue;
         }

         m_formats.remove(i);
         return true;
      }

      return false;
   }

   public SheetEntity setId(Integer id) {
      m_id=id;
      return this;
   }

   public SheetEntity setName(String name) {
      m_name=name;
      return this;
   }

}
