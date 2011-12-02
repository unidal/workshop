package com.site.service.pdf.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class TableBo extends TableDo implements GenericContainer {
   private PdfPTable m_table;
   private int m_headerRows;

   static {
      init();
   }

   public Element getElement() throws DocumentException {
      return m_table;
   }

   public void addChild(GenericContainer generic) throws DocumentException {
      if (generic instanceof TrBo) {
         TrBo tr = (TrBo) generic;

         if (m_table == null) { // at first row
            m_table = makeTable(tr.getColumnCount());
         }

         if (tr.isHeader()) {
            m_headerRows++;
            m_table.setHeaderRows(m_headerRows);
         }

         List cells = tr.getCells();
         for (int i = 0; i < cells.size(); i++) {
            Object cell = cells.get(i);

            if (cell instanceof PdfPCell) {
               m_table.addCell((PdfPCell) cell);
            } else if (cell instanceof PdfPTable) {
               PdfPCell pcell = m_table.getDefaultCell();
               
               pcell.setPadding(0);
               pcell.setColspan(((PdfPTable) cell).getAbsoluteWidths().length);
               m_table.addCell((PdfPTable) cell);
            }
         }
      }
   }

   private PdfPTable makeTable(int cols) throws DocumentException {
      PdfPTable table = new PdfPTable(cols);

      String width = getWidth();
      int len = (width == null ? 0 : width.length());
      if (len > 0) {
         if (len > 1 && width.endsWith("%")) {
            table.setWidthPercentage(Integer.parseInt(width.substring(0,
                  len - 1)));
         } else {
            table.setTotalWidth(Integer.parseInt(width));
         }
      }

      if (!isEmpty(getAlign())) {
         table.setHorizontalAlignment(GenericHelper.getAlignment(getAlign()));
      }

      if (!isEmpty(getWidths())) {
         table.setWidths(splitFloat(getWidths(), ','));
      }

      return table;
   }

   private float[] splitFloat(String values, char sp) {
      if (values == null || values.length() == 0)
         return new float[0];

      List parts = new ArrayList();
      int off = 0;

      while (true) {
         int pos = values.indexOf(sp, off);

         if (pos >= 0) {
            parts.add(values.substring(off, pos).trim());
            off = pos + 1;
         } else {
            parts.add(values.substring(off));
            break;
         }
      }

      try {
         float[] floats = new float[parts.size()];
         for (int i = 0; i < floats.length; i++)
            floats[i] = Float.parseFloat((String) parts.get(i));

         return floats;
      } catch (Exception e) {
         return new float[0];
      }
   }

   public void setReady(Stack parents) {
      // do nothing
   }

}
