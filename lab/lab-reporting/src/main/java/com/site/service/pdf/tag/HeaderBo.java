package com.site.service.pdf.tag;

import java.util.Stack;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Phrase;

public class HeaderBo extends HeaderDo implements GenericContainer {
   private HeaderFooter m_header;
   private Phrase m_phrase;

   static {
      init();
   }

   public Element getElement() throws DocumentException {
      if (m_header == null) {
         m_header = makeHeaderFooter(new Phrase());
      }

      return m_header;
   }

   public void addChild(GenericContainer generic) throws DocumentException {
      if (m_header == null) {
         m_phrase = new Phrase();
         m_header = makeHeaderFooter(m_phrase);
      }

      m_phrase.add(generic.getElement());
   }

   private HeaderFooter makeHeaderFooter(Phrase phrase) {
      HeaderFooter header = new HeaderFooter(phrase, false);

      header.setBorder(getBorder());
      return header;
   }

   public void setReady(Stack parents) {
      // do nothing
   }
}
