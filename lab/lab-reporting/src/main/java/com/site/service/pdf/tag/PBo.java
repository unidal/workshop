package com.site.service.pdf.tag;

import java.util.Stack;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;

public class PBo extends PDo implements GenericContainer {
   private Paragraph m_paragraph;

   static {
      init();
   }

   public Element getElement() throws DocumentException {
      if (m_paragraph == null) {
         m_paragraph = makeParagraph();
      }

      return m_paragraph;
   }

   public void addChild(GenericContainer generic) throws DocumentException {
      if (m_paragraph == null) {
         m_paragraph = makeParagraph();
      }

      m_paragraph.add(generic.getElement());
   }

   private Paragraph makeParagraph() {
      Paragraph paragraph = new Paragraph();

      if (!isEmpty(getAlign())) {
         paragraph.setAlignment(GenericHelper.getAlignment(getAlign()));
      }

      return paragraph;
   }

   public void setReady(Stack parents) {
      // do nothing
   }

}
