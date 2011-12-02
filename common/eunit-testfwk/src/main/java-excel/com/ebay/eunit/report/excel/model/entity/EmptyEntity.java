package com.ebay.eunit.report.excel.model.entity;

import com.ebay.eunit.report.excel.model.BaseEntity;
import com.ebay.eunit.report.excel.model.IVisitor;

public class EmptyEntity extends BaseEntity<EmptyEntity> {
   private Integer m_cols;

   private Integer m_rows;

   public EmptyEntity() {
   }

   @Override
   public void accept(IVisitor visitor) {
      visitor.visitEmpty(this);
   }

   public Integer getCols() {
      return m_cols;
   }

   public Integer getRows() {
      return m_rows;
   }

   @Override
   public void mergeAttributes(EmptyEntity other) {
      if (other.getCols() != null) {
         m_cols = other.getCols();
      }

      if (other.getRows() != null) {
         m_rows = other.getRows();
      }
   }

   public EmptyEntity setCols(Integer cols) {
      m_cols=cols;
      return this;
   }

   public EmptyEntity setRows(Integer rows) {
      m_rows=rows;
      return this;
   }

}
