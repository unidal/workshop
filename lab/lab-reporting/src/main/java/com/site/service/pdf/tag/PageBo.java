package com.site.service.pdf.tag;

import java.lang.reflect.Field;
import java.util.Stack;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

public class PageBo extends PageDo implements GenericContainer {
   private Document m_document;

   static {
      init();
   }

   public HeaderBo getHeaderBo() {
      return (HeaderBo) getHeaderDo();
   }

   public FooterBo getFooterBo() {
      return (FooterBo) getFooterDo();
   }

   public void setHeaderBo(HeaderBo header) {
      setHeaderDo(header);
   }

   public void setFooterBo(FooterBo footer) {
      setFooterDo(footer);
   }

   private Rectangle getPageSize() {
      if (isEmpty(getSize())) {
         return null;
      }

      Rectangle pageSize = PageSize.A4;

      try {
         Field field = PageSize.class.getField(getSize());

         pageSize = (Rectangle) field.get(null);
      } catch (Exception e) {
         e.printStackTrace();
         // ignore it
      }

      if (isRotate()) {
         return pageSize.rotate();
      } else {
         return pageSize;
      }
   }

   public void start(Stack parents) throws DocumentException {
      DocumentBo parent = (DocumentBo) parents.peek();
      Document document = parent.getDocument();

      Rectangle pageSize = getPageSize();
      if (pageSize != null) {
         document.setPageSize(pageSize);
      }

      document.setMargins(getMarginLeft(), getMarginRight(), getMarginTop(),
            getMarginBottom());

      if (document.isOpen()) {
         document.newPage();
      }

      m_document = document;
   }

   public void end(Stack parents) {
      // Do nothing here
   }

   public Element getElement() throws DocumentException {
      // This method will not be called
      return null;
   }

   public void addChild(GenericContainer generic) throws DocumentException {
      if (generic instanceof HeaderBo) {
         m_document.setHeader((HeaderFooter) generic.getElement());
      } else if (generic instanceof FooterBo) {
         m_document.setFooter((HeaderFooter) generic.getElement());
      } else {
         if (!m_document.isOpen()) {
            m_document.open();
         }

         Element element = generic.getElement();

         if (element != null) {
            m_document.add(generic.getElement());
         }
      }
   }

   public void setReady(Stack parents) {
      // do nothing
   }

}
