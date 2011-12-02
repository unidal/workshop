package com.site.service.pdf.tag;
import static com.site.kernel.dal.ValueType.INT;
import static com.site.kernel.dal.ValueType.STRING;
import static com.site.kernel.dal.model.NodeType.ATTRIBUTE;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;
import com.site.kernel.dal.model.ValidationException;

public class FontDo extends XmlModel {
   public static final XmlModelField NAME = new XmlModelField("name", ATTRIBUTE, STRING);
   public static final XmlModelField SIZE = new XmlModelField("size", ATTRIBUTE, INT, "-1");
   public static final XmlModelField STYLE = new XmlModelField("style", ATTRIBUTE, INT, "-1");
   public static final XmlModelField COLOR = new XmlModelField("color", ATTRIBUTE, STRING);

   private String m_name;
   private int m_size;
   private int m_style;
   private String m_color;

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public FontDo() {
      super(null, "font");
   }

   public boolean checkConstraint() throws ValidationException {
      return true;
   }

   public void destroy() {
   }

   protected void doValidate(Stack<XmlModel> parents) {
   }

   public String getName() {
      return m_name;
   }
   
   public int getSize() {
      return m_size;
   }
   
   public int getStyle() {
      return m_style;
   }
   
   public String getColor() {
      return m_color;
   }
   
   public void loadAttributes(Attributes attrs) {
      setFieldValue(NAME, attrs);
      setFieldValue(SIZE, attrs);
      setFieldValue(STYLE, attrs);
      setFieldValue(COLOR, attrs);
   }

   public void setName(String name) {
      m_name = name;
      setFieldUsed(NAME, true);
   }
   
   public void setSize(int size) {
      m_size = size;
      setFieldUsed(SIZE, true);
   }
   
   public void setStyle(int style) {
      m_style = style;
      setFieldUsed(STYLE, true);
   }
   
   public void setColor(String color) {
      m_color = color;
      setFieldUsed(COLOR, true);
   }
   
}
