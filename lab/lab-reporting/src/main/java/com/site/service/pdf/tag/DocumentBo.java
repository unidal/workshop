package com.site.service.pdf.tag;

import java.io.OutputStream;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;

public class DocumentBo extends DocumentDo {
   private Document m_document;
   
   static {
      init();
   }

   public void addPageBo(PageBo page) {
      addPageDo(page);
   }

   public List getPageBos() {
      return getPageDos();
   }

   public void createDocument(OutputStream outputStream)
         throws DocumentException {
      m_document = new Document();
      m_document.addCreationDate();

      if (!isEmpty(getAuthor())) {
         m_document.addAuthor(getAuthor());
      }

      if (!isEmpty(getSubject())) {
         m_document.addSubject(getSubject());
      }

      PdfWriter.getInstance(m_document, outputStream);
   }

   public void closeDocument() {
      m_document.close();
   }

   public Document getDocument() {
      return m_document;
   }


}
