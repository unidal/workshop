package com.site.service.pdf.tag;
import static com.site.kernel.dal.ValueType.BOOLEAN;
import static com.site.kernel.dal.ValueType.FLOAT;
import static com.site.kernel.dal.ValueType.INT;
import static com.site.kernel.dal.ValueType.STRING;
import static com.site.kernel.dal.model.NodeType.ATTRIBUTE;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;
import com.site.kernel.dal.model.ValidationException;

public class TdDo extends XmlModel {
   public static final XmlModelField CELLPADDING = new XmlModelField("cellpadding", ATTRIBUTE, FLOAT, "-1");
   public static final XmlModelField BORDER = new XmlModelField("border", ATTRIBUTE, INT, "-1");
   public static final XmlModelField ALIGN = new XmlModelField("align", ATTRIBUTE, STRING);
   public static final XmlModelField VALIGN = new XmlModelField("valign", ATTRIBUTE, STRING);
   public static final XmlModelField HEIGHT = new XmlModelField("height", ATTRIBUTE, FLOAT, "-1");
   public static final XmlModelField COLSPAN = new XmlModelField("colspan", ATTRIBUTE, INT, "1");
   public static final XmlModelField NOWRAP = new XmlModelField("nowrap", ATTRIBUTE, BOOLEAN, "false");
   public static final XmlModelField HEADER = new XmlModelField("header", ATTRIBUTE, BOOLEAN, "false");

   private float m_cellpadding;
   private int m_border;
   private String m_align;
   private String m_valign;
   private float m_height;
   private int m_colspan;
   private boolean m_nowrap;
   private boolean m_header;

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public TdDo() {
      super(null, "td");
   }

   public boolean checkConstraint() throws ValidationException {
      return true;
   }

   public void destroy() {
   }

   protected void doValidate(Stack<XmlModel> parents) {
   }

   public float getCellpadding() {
      return m_cellpadding;
   }
   
   public int getBorder() {
      return m_border;
   }
   
   public String getAlign() {
      return m_align;
   }
   
   public String getValign() {
      return m_valign;
   }
   
   public float getHeight() {
      return m_height;
   }
   
   public int getColspan() {
      return m_colspan;
   }
   
   public boolean isNowrap() {
      return m_nowrap;
   }
   
   public boolean isHeader() {
      return m_header;
   }
   
   public void loadAttributes(Attributes attrs) {
      setFieldValue(CELLPADDING, attrs);
      setFieldValue(BORDER, attrs);
      setFieldValue(ALIGN, attrs);
      setFieldValue(VALIGN, attrs);
      setFieldValue(HEIGHT, attrs);
      setFieldValue(COLSPAN, attrs);
      setFieldValue(NOWRAP, attrs);
      setFieldValue(HEADER, attrs);
   }

   public void setCellpadding(float cellpadding) {
      m_cellpadding = cellpadding;
      setFieldUsed(CELLPADDING, true);
   }
   
   public void setBorder(int border) {
      m_border = border;
      setFieldUsed(BORDER, true);
   }
   
   public void setAlign(String align) {
      m_align = align;
      setFieldUsed(ALIGN, true);
   }
   
   public void setValign(String valign) {
      m_valign = valign;
      setFieldUsed(VALIGN, true);
   }
   
   public void setHeight(float height) {
      m_height = height;
      setFieldUsed(HEIGHT, true);
   }
   
   public void setColspan(int colspan) {
      m_colspan = colspan;
      setFieldUsed(COLSPAN, true);
   }
   
   public void setNowrap(boolean nowrap) {
      m_nowrap = nowrap;
      setFieldUsed(NOWRAP, true);
   }
   
   public void setHeader(boolean header) {
      m_header = header;
      setFieldUsed(HEADER, true);
   }
   
}
