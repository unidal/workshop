package com.site.service.pdf.tag;

import java.util.Stack;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

public class BaseFontBo extends BaseFontDo {

   static {
      init();
   }

   public void start(Stack parents) throws DocumentException {
      if (checkConstraint()) {
         try {
            BaseFont baseFont = BaseFont.createFont(getName(), getEncoding(),
                  isEmbedded());

            FontManager.getInstance().registerBaseFont(getAlias(), baseFont);
         } catch (Exception e) {
            throw new DocumentException(e);
         }
      } else {
         throw new RuntimeException(this + " is invalid under "
               + parents.peek());
      }
   }

   public void end(Stack parents) {
      // Do nothing here
   }

}
