package com.site.service.pdf.tag;
import static com.site.kernel.dal.ValueType.INT;
import static com.site.kernel.dal.ValueType.STRING;
import static com.site.kernel.dal.model.NodeType.ATTRIBUTE;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;
import com.site.kernel.dal.model.ValidationException;

public class ImgDo extends XmlModel {
   public static final XmlModelField SRC = new XmlModelField("src", ATTRIBUTE, STRING);
   public static final XmlModelField ALT = new XmlModelField("alt", ATTRIBUTE, STRING);
   public static final XmlModelField ALIGN = new XmlModelField("align", ATTRIBUTE, STRING);
   public static final XmlModelField WIDTH = new XmlModelField("width", ATTRIBUTE, INT, "0");
   public static final XmlModelField HEIGHT = new XmlModelField("height", ATTRIBUTE, INT, "0");

   private String m_src;
   private String m_alt;
   private String m_align;
   private int m_width;
   private int m_height;

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public ImgDo() {
      super(null, "img");
   }

   public boolean checkConstraint() throws ValidationException {
      if (isEmpty(m_src)) {
         return false;
      }
      
      return true;
   }

   public void destroy() {
   }

   protected void doValidate(Stack<XmlModel> parents) {
   }

   public String getSrc() {
      return m_src;
   }
   
   public String getAlt() {
      return m_alt;
   }
   
   public String getAlign() {
      return m_align;
   }
   
   public int getWidth() {
      return m_width;
   }
   
   public int getHeight() {
      return m_height;
   }
   
   public void loadAttributes(Attributes attrs) {
      setFieldValue(SRC, attrs);
      setFieldValue(ALT, attrs);
      setFieldValue(ALIGN, attrs);
      setFieldValue(WIDTH, attrs);
      setFieldValue(HEIGHT, attrs);
   }

   public void setSrc(String src) {
      m_src = src;
      setFieldUsed(SRC, true);
   }
   
   public void setAlt(String alt) {
      m_alt = alt;
      setFieldUsed(ALT, true);
   }
   
   public void setAlign(String align) {
      m_align = align;
      setFieldUsed(ALIGN, true);
   }
   
   public void setWidth(int width) {
      m_width = width;
      setFieldUsed(WIDTH, true);
   }
   
   public void setHeight(int height) {
      m_height = height;
      setFieldUsed(HEIGHT, true);
   }
   
}
