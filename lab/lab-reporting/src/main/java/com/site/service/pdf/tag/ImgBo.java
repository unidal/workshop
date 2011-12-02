package com.site.service.pdf.tag;

import java.util.Stack;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.site.kernel.dal.model.ValidationException;

public class ImgBo extends ImgDo implements GenericContainer {

   static {
      init();
   }

   public Element getElement() throws DocumentException {
      if (!checkConstraint()) {
         throw new ValidationException(this + " is invalid");
      }

      try {
         Image img = Image.getInstance(getSrc());

         if (!isEmpty(getAlt())) {
            img.setAlt(getAlt());
         }

         if (getWidth() > 0) {
            img.scaleAbsoluteWidth(getWidth());
         }

         if (getHeight() > 0) {
            img.scaleAbsoluteHeight(getHeight());
         }

         if (!isEmpty(getAlign())) {
            img.setAlignment(GenericHelper.getAlignment(getAlign()));
         }

         return img;
      } catch (Exception e) {
         throw new DocumentException(e);
      }
   }

   public void addChild(GenericContainer generic) {
      // no child
   }

   public void setReady(Stack parents) {
      // do nothing
   }
}
