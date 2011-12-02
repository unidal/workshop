package com.site.service.pdf.tag;

import java.util.Stack;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;

public class BrBo extends BrDo implements GenericContainer {

   static {
      init();
   }

   public Element getElement() throws DocumentException {
      return Chunk.NEWLINE;
   }

   public void addChild(GenericContainer generic) {
      // no child
   }

   public void setReady(Stack parents) {
      // do nothing
   }

}
