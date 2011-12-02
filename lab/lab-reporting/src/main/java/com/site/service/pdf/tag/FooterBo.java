package com.site.service.pdf.tag;

import java.util.Stack;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Phrase;

public class FooterBo extends FooterDo implements GenericContainer {
   private Phrase m_phrase;

   static {
      init();
   }

   public Element getElement() throws DocumentException {
      HeaderFooter footer = new HeaderFooter(m_phrase, false);

      footer.setBorder(getBorder());
      return footer;
   }

   public void addChild(GenericContainer generic) throws DocumentException {
      if (m_phrase == null) {
         m_phrase = new Phrase();
      }

      m_phrase.add(generic.getElement());
   }

   public void setReady(Stack parents) {
      // do nothing
   }

}
