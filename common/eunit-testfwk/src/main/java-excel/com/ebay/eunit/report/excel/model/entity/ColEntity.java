package com.ebay.eunit.report.excel.model.entity;

import java.util.ArrayList;
import java.util.List;

import com.ebay.eunit.report.excel.model.BaseEntity;
import com.ebay.eunit.report.excel.model.IVisitor;

public class ColEntity extends BaseEntity<ColEntity> {
   private List<BaseEntity<?>> m_allChildrenInSequence = new ArrayList<BaseEntity<?>>();

   private Integer m_startRow;

   private Integer m_startCol;

   private Boolean m_create;

   private List<LabelEntity> m_labels = new ArrayList<LabelEntity>();

   private List<NumberEntity> m_numbers = new ArrayList<NumberEntity>();

   private List<EmptyEntity> m_emptyList = new ArrayList<EmptyEntity>();

   private List<DateTimeEntity> m_dateTimes = new ArrayList<DateTimeEntity>();

   public ColEntity() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitCol(this);
   }

   public ColEntity addDateTime(DateTimeEntity dateTime) {
      m_allChildrenInSequence.add(dateTime);
      m_dateTimes.add(dateTime);
      return this;
   }

   public ColEntity addEmpty(EmptyEntity empty) {
      m_allChildrenInSequence.add(empty);
      m_emptyList.add(empty);
      return this;
   }

   public ColEntity addLabel(LabelEntity label) {
      m_allChildrenInSequence.add(label);
      m_labels.add(label);
      return this;
   }

   public ColEntity addNumber(NumberEntity number) {
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
   public void mergeAttributes(ColEntity other) {
      if (other.getStartRow() != null) {
         m_startRow = other.getStartRow();
      }

      if (other.getStartCol() != null) {
         m_startCol = other.getStartCol();
      }

      if (other.getCreate() != null) {
         m_create = other.getCreate();
      }
   }

   public ColEntity setCreate(Boolean create) {
      m_create=create;
      return this;
   }

   public ColEntity setStartCol(Integer startCol) {
      m_startCol=startCol;
      return this;
   }

   public ColEntity setStartRow(Integer startRow) {
      m_startRow=startRow;
      return this;
   }

}
