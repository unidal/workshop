package com.site.service.pdf.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;

public class TrBo extends TrDo implements GenericContainer {
   private List m_cells = new ArrayList();
   private int m_cols;
   private boolean m_header;

   static {
      init();
   }

   public Element getElement() throws DocumentException {
      // should not be called, use getCells instead
      return null;
   }

   public void addChild(GenericContainer generic) throws DocumentException {
      if (generic instanceof TdBo) {
         TdBo td = (TdBo) generic;

         m_cols += td.getColspan();
         m_cells.add(td.getElement());

         if (td.isHeader()) {
            m_header = true;
         }
      }
   }

   public int getColumnCount() {
      return m_cols;
   }

   public List getCells() {
      return m_cells;
   }

   public boolean isHeader() {
      return m_header;
   }

   public void setReady(Stack parents) {
      Object obj = parents.peek();

      if (obj instanceof GenericHelper) {
         TableBo table = (TableBo) ((GenericHelper) obj).getGenericContainer();

         if (getBorder() < 0) {
            setBorder(table.getBorder());
         }

         if (getCellpadding() < 0) {
            setCellpadding(table.getCellpadding());
         }
      }
   }

}
