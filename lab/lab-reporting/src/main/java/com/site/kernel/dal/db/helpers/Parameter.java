package com.site.kernel.dal.db.helpers;

import com.site.kernel.dal.db.DataRowField;

public class Parameter {
   private DataRowField m_field;

   private int m_index; // Array index

   private boolean m_in;

   private boolean m_out;

   public Parameter(DataRowField field) {
      this(field, true, false);

      m_index = -1;
   }

   public Parameter(DataRowField field, boolean isIn, boolean isOut) {
      m_field = field;
      m_in = isIn;
      m_out = isOut;

      m_index = -1;
   }

   public Parameter(DataRowField field, int index) {
      this(field, true, false);

      m_index = index;
   }

   public DataRowField getField() {
      return m_field;
   }

   public int getIndex() {
      return m_index;
   }

   public boolean isIn() {
      return m_in;
   }

   public boolean isOut() {
      return m_out;
   }
}