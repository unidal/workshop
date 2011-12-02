package com.site.service.pdf.tag;

import java.util.Stack;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class TdBo extends TdDo implements GenericContainer {
   private PdfPCell m_cell;
   private PdfPTable m_nested;

   static {
      init();
   }

   public Element getElement() throws DocumentException {
      if (m_nested != null) {
         return m_nested;
      } else if (m_cell == null) {
         m_cell = makeCell();
      }

      return m_cell;
   }

   public void addChild(GenericContainer generic) throws DocumentException {
      if (m_cell == null) {
         m_cell = makeCell();
      }

      Element element = generic.getElement();

      if (element instanceof Image) {
         m_cell.setImage((Image) element);
         m_cell.setPadding(-0.25f);
      } else if (element instanceof Chunk || element instanceof Phrase) {
         m_cell.getPhrase().add(element);
      } else if (element instanceof PdfPTable) {
         m_nested = (PdfPTable) element;
      }
   }

   private PdfPCell makeCell() {
      PdfPCell cell = new PdfPCell(new Phrase());

      cell.setBorder(getBorder());
      cell.setPadding(getCellpadding());

      if (getHeight() > 0) {
         cell.setMinimumHeight(getHeight());
      }

      // cell.setRowspan(getRowspan());
      cell.setColspan(getColspan());

      if (!isEmpty(getAlign())) {
         cell.setHorizontalAlignment(GenericHelper.getAlignment(getAlign()));
      } else if (isHeader()) {
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      }

      if (!isEmpty(getValign())) {
         cell.setVerticalAlignment(GenericHelper.getAlignment(getValign()));
      } else if (isHeader()) {
         cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      }

      if (isNowrap()) {
         cell.setNoWrap(true);
      }

      return cell;
   }

   public void setReady(Stack parents) {
      Object obj = parents.peek();

      if (obj instanceof GenericHelper) {
         TrBo tr = (TrBo) ((GenericHelper) obj).getGenericContainer();

         if (getBorder() < 0) {
            setBorder(tr.getBorder());
         }

         if (getCellpadding() < 0) {
            setCellpadding(tr.getCellpadding());
         }

         if (isEmpty(getAlign())) {
            setAlign(tr.getAlign());
         }

         if (isEmpty(getValign())) {
            setValign(tr.getValign());
         }
      }
   }

}
