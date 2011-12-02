package com.site.service.pdf.tag;
import static com.site.kernel.dal.Cardinality.ZERO_TO_ONE;
import static com.site.kernel.dal.ValueType.BOOLEAN;
import static com.site.kernel.dal.ValueType.INT;
import static com.site.kernel.dal.ValueType.STRING;
import static com.site.kernel.dal.model.NodeType.ATTRIBUTE;
import static com.site.kernel.dal.model.NodeType.MODEL;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;
import com.site.kernel.dal.model.ValidationException;

public class PageDo extends XmlModel {
   public static final XmlModelField SIZE = new XmlModelField("size", ATTRIBUTE, STRING);
   public static final XmlModelField ROTATE = new XmlModelField("rotate", ATTRIBUTE, BOOLEAN, "false");
   public static final XmlModelField MARGIN_LEFT = new XmlModelField("margin-left", ATTRIBUTE, INT, "36");
   public static final XmlModelField MARGIN_RIGHT = new XmlModelField("margin-right", ATTRIBUTE, INT, "36");
   public static final XmlModelField MARGIN_TOP = new XmlModelField("margin-top", ATTRIBUTE, INT, "36");
   public static final XmlModelField MARGIN_BOTTOM = new XmlModelField("margin-bottom", ATTRIBUTE, INT, "36");

   public static final XmlModelField HEADER = new XmlModelField("header", MODEL, ZERO_TO_ONE, HeaderDo.class);
   public static final XmlModelField FOOTER = new XmlModelField("footer", MODEL, ZERO_TO_ONE, FooterDo.class);

   private String m_size;
   private boolean m_rotate;
   private int m_marginLeft;
   private int m_marginRight;
   private int m_marginTop;
   private int m_marginBottom;

   private HeaderDo m_header;
   private FooterDo m_footer;

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public PageDo() {
      super(null, "page");
   }

   public boolean checkConstraint() throws ValidationException {
      return true;
   }

   public void destroy() {
      if (m_header != null) {
         m_header.destroy();
      }

      if (m_footer != null) {
         m_footer.destroy();
      }
   }

   protected void doValidate(Stack<XmlModel> parents) {
      if (m_header != null) {
         m_header.validate(parents);
      }

      if (m_footer != null) {
         m_footer.validate(parents);
      }
   }

   protected HeaderDo getHeaderDo() {
      return m_header;
   }
   
   protected FooterDo getFooterDo() {
      return m_footer;
   }
   
   public String getSize() {
      return m_size;
   }
   
   public boolean isRotate() {
      return m_rotate;
   }
   
   public int getMarginLeft() {
      return m_marginLeft;
   }
   
   public int getMarginRight() {
      return m_marginRight;
   }
   
   public int getMarginTop() {
      return m_marginTop;
   }
   
   public int getMarginBottom() {
      return m_marginBottom;
   }
   
   public void loadAttributes(Attributes attrs) {
      setFieldValue(SIZE, attrs);
      setFieldValue(ROTATE, attrs);
      setFieldValue(MARGIN_LEFT, attrs);
      setFieldValue(MARGIN_RIGHT, attrs);
      setFieldValue(MARGIN_TOP, attrs);
      setFieldValue(MARGIN_BOTTOM, attrs);
   }

   protected void setHeaderDo(HeaderDo header) {
      m_header = header;
      setFieldUsed(HEADER, true);
   }
   
   protected void setFooterDo(FooterDo footer) {
      m_footer = footer;
      setFieldUsed(FOOTER, true);
   }
   
   public void setSize(String size) {
      m_size = size;
      setFieldUsed(SIZE, true);
   }
   
   public void setRotate(boolean rotate) {
      m_rotate = rotate;
      setFieldUsed(ROTATE, true);
   }
   
   public void setMarginLeft(int marginLeft) {
      m_marginLeft = marginLeft;
      setFieldUsed(MARGIN_LEFT, true);
   }
   
   public void setMarginRight(int marginRight) {
      m_marginRight = marginRight;
      setFieldUsed(MARGIN_RIGHT, true);
   }
   
   public void setMarginTop(int marginTop) {
      m_marginTop = marginTop;
      setFieldUsed(MARGIN_TOP, true);
   }
   
   public void setMarginBottom(int marginBottom) {
      m_marginBottom = marginBottom;
      setFieldUsed(MARGIN_BOTTOM, true);
   }
   
}
