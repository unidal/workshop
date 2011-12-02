package com.site.service.pdf.tag;
import static com.site.kernel.dal.Cardinality.ZERO_TO_MANY;
import static com.site.kernel.dal.ValueType.STRING;
import static com.site.kernel.dal.model.NodeType.ATTRIBUTE;
import static com.site.kernel.dal.model.NodeType.MODEL;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;
import com.site.kernel.dal.model.ValidationException;

public class DocumentDo extends XmlModel {
   public static final XmlModelField AUTHOR = new XmlModelField("author", ATTRIBUTE, STRING);
   public static final XmlModelField SUBJECT = new XmlModelField("subject", ATTRIBUTE, STRING);

   public static final XmlModelField BASE_FONT = new XmlModelField("base-font", MODEL, ZERO_TO_MANY, BaseFontDo.class);
   public static final XmlModelField PAGE = new XmlModelField("page", MODEL, ZERO_TO_MANY, PageDo.class);

   private String m_author;
   private String m_subject;

   private List m_baseFonts = new ArrayList(3);
   private List m_pages = new ArrayList(3);

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public DocumentDo() {
      super(null, "document");
   }

   protected void addBaseFontDo(BaseFontDo baseFont) {
      super.addChild(BASE_FONT, baseFont);
   }
   
   protected void addPageDo(PageDo page) {
      super.addChild(PAGE, page);
   }
   
   public boolean checkConstraint() throws ValidationException {
      return true;
   }

   public void destroy() {
      for (int i = 0; i != m_baseFonts.size(); i++) {
         BaseFontDo baseFont = (BaseFontDo) m_baseFonts.get(i);

         baseFont.destroy();
      }

      for (int i = 0; i != m_pages.size(); i++) {
         PageDo page = (PageDo) m_pages.get(i);

         page.destroy();
      }
   }

   protected void doValidate(Stack<XmlModel> parents) {
      for (int i = 0; i != m_baseFonts.size(); i++) {
         BaseFontDo baseFont = (BaseFontDo) m_baseFonts.get(i);

         baseFont.validate(parents);
      }

      for (int i = 0; i != m_pages.size(); i++) {
         PageDo page = (PageDo) m_pages.get(i);

         page.validate(parents);
      }
   }

   protected List getBaseFontDos() {
      return m_baseFonts;
   }
   
   protected List getPageDos() {
      return m_pages;
   }
   
   public String getAuthor() {
      return m_author;
   }
   
   public String getSubject() {
      return m_subject;
   }
   
   public void loadAttributes(Attributes attrs) {
      setFieldValue(AUTHOR, attrs);
      setFieldValue(SUBJECT, attrs);
   }

   public void setAuthor(String author) {
      m_author = author;
      setFieldUsed(AUTHOR, true);
   }
   
   public void setSubject(String subject) {
      m_subject = subject;
      setFieldUsed(SUBJECT, true);
   }
   
}
