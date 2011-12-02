package com.site.service.pdf.tag;

import org.xml.sax.Attributes;

public class ThBo extends TdBo {
   static {
      init();
   }

   public void loadAttributes(Attributes attrs) {
      super.loadAttributes(attrs);

      setHeader(true);
   }

}
