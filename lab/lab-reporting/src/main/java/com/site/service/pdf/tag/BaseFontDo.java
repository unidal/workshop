package com.site.service.pdf.tag;
import static com.site.kernel.dal.ValueType.BOOLEAN;
import static com.site.kernel.dal.ValueType.STRING;
import static com.site.kernel.dal.model.NodeType.ATTRIBUTE;

import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;
import com.site.kernel.dal.model.ValidationException;

public class BaseFontDo extends XmlModel {
   public static final XmlModelField ALIAS = new XmlModelField("alias", ATTRIBUTE, STRING);
   public static final XmlModelField NAME = new XmlModelField("name", ATTRIBUTE, STRING);
   public static final XmlModelField ENCODING = new XmlModelField("encoding", ATTRIBUTE, STRING);
   public static final XmlModelField EMBEDDED = new XmlModelField("embedded", ATTRIBUTE, BOOLEAN, "false");

   private String m_alias;
   private String m_name;
   private String m_encoding;
   private boolean m_embedded;

   static {
      init();
   }

   protected static void init() {
      initialize(null);
   }

   public BaseFontDo() {
      super(null, "base-font");
   }

   public boolean checkConstraint() throws ValidationException {
      if (isEmpty(m_alias)) {
         return false;
      }
      
      if (isEmpty(m_name)) {
         return false;
      }
      
      if (isEmpty(m_encoding)) {
         return false;
      }
      
      return true;
   }

   public void destroy() {
   }

   protected void doValidate(Stack<XmlModel> parents) {
   }

   public String getAlias() {
      return m_alias;
   }
   
   public String getName() {
      return m_name;
   }
   
   public String getEncoding() {
      return m_encoding;
   }
   
   public boolean isEmbedded() {
      return m_embedded;
   }
   
   public void loadAttributes(Attributes attrs) {
      setFieldValue(ALIAS, attrs);
      setFieldValue(NAME, attrs);
      setFieldValue(ENCODING, attrs);
      setFieldValue(EMBEDDED, attrs);
   }

   public void setAlias(String alias) {
      m_alias = alias;
      setFieldUsed(ALIAS, true);
   }
   
   public void setName(String name) {
      m_name = name;
      setFieldUsed(NAME, true);
   }
   
   public void setEncoding(String encoding) {
      m_encoding = encoding;
      setFieldUsed(ENCODING, true);
   }
   
   public void setEmbedded(boolean embedded) {
      m_embedded = embedded;
      setFieldUsed(EMBEDDED, true);
   }
   
}
