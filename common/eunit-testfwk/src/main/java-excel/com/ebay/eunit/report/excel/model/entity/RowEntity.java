package com.ebay.eunit.report.excel.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.ebay.eunit.report.excel.model.BaseEntity;
import com.ebay.eunit.report.excel.model.IVisitor;

public class RowEntity extends BaseEntity<RowEntity> {
   private List<BaseEntity<?>> m_allChildrenInSequence = new ArrayList<BaseEntity<?>>();

   private Integer m_startCol;

   private Integer m_startRow;

   private Boolean m_create;

   private List<LabelEntity> m_labels = new ArrayList<LabelEntity>();

   private List<NumberEntity> m_numbers = new ArrayList<NumberEntity>();

   private List<EmptyEntity> m_emptyList = new ArrayList<EmptyEntity>();

   private List<DateTimeEntity> m_dateTimes = new ArrayList<DateTimeEntity>();

   public RowEntity() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitRow(this);
   }

   public RowEntity addDateTime(DateTimeEntity dateTime) {
      m_allChildrenInSequence.add(dateTime);
      m_dateTimes.add(dateTime);
      return this;
   }

   public RowEntity addEmpty(EmptyEntity empty) {
      m_allChildrenInSequence.add(empty);
      m_emptyList.add(empty);
      return this;
   }

   public RowEntity addLabel(LabelEntity label) {
      m_allChildrenInSequence.add(label);
      m_labels.add(label);
      return this;
   }

   public RowEntity addNumber(NumberEntity number) {
      m_allChildrenInSequence.add(number);
      m_numbers.add(number);
      return this;
   }

   public List<BaseEntity<?>> getAllChildrenInSequence() {
      return m_allChildrenInSequence;
   }

   public Boolean getCreate() {
      return m_create;
   }

   public List<DateTimeEntity> getDateTimes() {
      return m_dateTimes;
   }

   public List<EmptyEntity> getEmptyList() {
      return m_emptyList;
   }

   public List<LabelEntity> getLabels() {
      return m_labels;
   }

   public List<NumberEntity> getNumbers() {
      return m_numbers;
   }

   public Integer getStartCol() {
      return m_startCol;
   }

   public Integer getStartRow() {
      return m_startRow;
   }

   public boolean isCreate() {
      return m_create != null && m_create.booleanValue();
   }

   @Override
   public void mergeAttributes(RowEntity other) {
      if (other.getStartCol() != null) {
         m_startCol = other.getStartCol();
      }

      if (other.getStartRow() != null) {
         m_startRow = other.getStartRow();
      }

      if (other.getCreate() != null) {
         m_create = other.getCreate();
      }
   }

   public RowEntity setCreate(Boolean create) {
      m_create=create;
      return this;
   }

   public RowEntity setStartCol(Integer startCol) {
      m_startCol=startCol;
      return this;
   }

   public RowEntity setStartRow(Integer startRow) {
      m_startRow=startRow;
      return this;
   }

}
