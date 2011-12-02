package com.site.service.pdf.tag;
import static com.site.kernel.dal.ValueType.FLOAT;
import static com.site.kernel.dal.ValueType.INT;
import static com.site.kernel.dal.ValueType.STRING;
import static com.site.kernel.dal.model.NodeType.ATTRIBUTE;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;
import com.site.kernel.dal.model.ValidationException;

public class TableDo extends XmlModel {
   public static final XmlModelField BORDER = new XmlModelField("border", ATTRIBUTE, INT, "15");
   public static final XmlModelField CELLPADDING = new XmlModelField("cellpadding", ATTRIBUTE, FLOAT, "2");
   public static final XmlModelField ALIGN = new XmlModelField("align", ATTRIBUTE, STRING);
   public static final XmlModelField WIDTH = new XmlModelField("width", ATTRIBUTE, STRING);
   public static final XmlModelField WIDTHS = new XmlModelField("widths", ATTRIBUTE, STRING);

   private int m_border;
   private float m_cellpadding;
   private String m_align;
   private String m_width;
   private String m_widths;

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public TableDo() {
      super(null, "table");
   }

   public boolean checkConstraint() throws ValidationException {
      return true;
   }

   public void destroy() {
   }

   protected void doValidate(Stack<XmlModel> parents) {
   }

   public int getBorder() {
      return m_border;
   }
   
   public float getCellpadding() {
      return m_cellpadding;
   }
   
   public String getAlign() {
      return m_align;
   }
   
   public String getWidth() {
      return m_width;
   }
   
   public String getWidths() {
      return m_widths;
   }
   
   public void loadAttributes(Attributes attrs) {
      setFieldValue(BORDER, attrs);
      setFieldValue(CELLPADDING, attrs);
      setFieldValue(ALIGN, attrs);
      setFieldValue(WIDTH, attrs);
      setFieldValue(WIDTHS, attrs);
   }

   public void setBorder(int border) {
      m_border = border;
      setFieldUsed(BORDER, true);
   }
   
   public void setCellpadding(float cellpadding) {
      m_cellpadding = cellpadding;
      setFieldUsed(CELLPADDING, true);
   }
   
   public void setAlign(String align) {
      m_align = align;
      setFieldUsed(ALIGN, true);
   }
   
   public void setWidth(String width) {
      m_width = width;
      setFieldUsed(WIDTH, true);
   }
   
   public void setWidths(String widths) {
      m_widths = widths;
      setFieldUsed(WIDTHS, true);
   }
   
}
