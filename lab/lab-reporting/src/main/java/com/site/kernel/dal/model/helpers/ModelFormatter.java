package com.site.kernel.dal.model.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.site.kernel.dal.DataObjectField;
import com.site.kernel.dal.model.ModelFilter;
import com.site.kernel.dal.model.ModelRegistry;
import com.site.kernel.dal.model.Namespace;
import com.site.kernel.dal.model.NodeType;
import com.site.kernel.dal.model.ValidationException;
import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelField;
import com.site.kernel.dal.model.common.Event;
import com.site.kernel.dal.model.common.FormatingException;
import com.site.kernel.dal.model.common.Formatter;
import com.site.kernel.dal.model.common.KeyValue;
import com.site.kernel.dal.model.common.ParsingException;

public class ModelFormatter implements Formatter {
   private XmlModel m_model;

   private XmlModelField m_current;

   private XmlModelField m_text;

   private int m_level;

   public ModelFormatter(XmlModel model) {
      m_model = model;
      m_current = null;
      m_level = 0;

      m_text = getTextField();
   }

   protected String buildXml(XmlModel root, ModelFilter filter, boolean indented) throws FormatingException {
      StringBuffer sb = new StringBuffer(4096);
      int level = root.getLevel();
      String indent = (indented && level > 0 ? getSpaces(level) : EMPTY);
      Namespace bns = root.getNamespace();
      String tagName = (bns == null || !bns.hasAlias() ? root.getModelName() : bns.getAlias() + ":" + root.getModelName());
      int mark = 0;

      if (level >= 0) {
         // Render START tag
         sb.append(indent).append('<').append(tagName);

         // Render ATTRIBUTE list
         buildXmlAttributes(root, filter, sb);

         // Render Namespace List
         buildXmlNamespace(root, filter, sb);

         mark = sb.length();
         sb.append(">\r\n");

         // Render ELEMENT list
         buildXmlElements(root, filter, sb, indented);

         // Render TEXT
         buildXmlText(root, filter, sb, indented);
      }

      // Render MODEL list
      buildXmlModels(root, filter, sb, indented);

      if (level >= 0) {
         // Render END tag
         if (sb.length() == mark + 3) {
            // no ELEMENT and no MODEL
            sb.insert(mark, "/");
         } else {
            sb.append(indent).append("</").append(tagName).append(">\r\n");
         }
      }

      return sb.toString();
   }

   protected void buildXmlAttributes(XmlModel root, ModelFilter filter, StringBuffer sb) throws FormatingException {
      List<DataObjectField> fields = root.getMetaData().getFields();

      for (DataObjectField f : fields) {
         if (f instanceof XmlModelField) {
            XmlModelField field = (XmlModelField) f;
            NodeType nodeType = field.getNodeType();

            if (nodeType != NodeType.ATTRIBUTE) {
               continue;
            }

            if (!root.isFieldUsed(field) || filter != null && !filter.contains(field)) {
               continue;
            }

            Object value = field.getFieldValue(root);

            if (value != null) {
               sb.append(' ').append(field.getTagName()).append("=\"");

               if (field.hasFormatter()) {
                  Formatter formatter = field.getFormatter();

                  sb.append(xmlEncode(formatter.format(value, false)));
               } else {
                  sb.append(xmlEncode(value.toString()));
               }

               sb.append("\"");
            }
         }
      }
   }

   protected void buildXmlElements(XmlModel root, ModelFilter filter, StringBuffer sb, boolean indented) throws FormatingException {
      String indent = (indented ? getSpaces(root.getLevel()) : EMPTY);
      List<DataObjectField> fields = root.getMetaData().getFields();

      for (DataObjectField f : fields) {
         if (f instanceof XmlModelField) {
            XmlModelField field = (XmlModelField) f;
            NodeType nodeType = field.getNodeType();

            if (nodeType != NodeType.ELEMENT && nodeType != NodeType.ELEMENT_CDATA) {
               continue;
            }

            if (!root.isFieldUsed(field) || filter != null && !filter.contains(field)) {
               continue;
            }

            Object value = field.getFieldValue(root);

            if (value != null) {
               if (indented) {
                  sb.append(indent).append(ONE_INDENT);
               }

               sb.append('<').append(field.getTagName()).append('>');

               if (field.getNodeType() == NodeType.ELEMENT_CDATA) {
                  sb.append("<![CDATA[").append(value).append("]]>");
               } else if (field.hasFormatter()) {
                  Formatter formatter = field.getFormatter();

                  sb.append(xmlEncode(formatter.format(value, indented)));
               } else {
                  sb.append(xmlEncode(value.toString()));
               }

               sb.append("</").append(field.getTagName()).append(">\r\n");
            }
         }
      }
   }

   protected void buildXmlModels(XmlModel root, ModelFilter filter, StringBuffer sb, boolean indented) throws FormatingException {
      List<DataObjectField> fields = root.getMetaData().getFields();

      for (DataObjectField f : fields) {
         if (f instanceof XmlModelField) {
            XmlModelField field = (XmlModelField) f;
            NodeType nodeType = field.getNodeType();

            if (nodeType != NodeType.MODEL) {
               continue;
            }

            if (filter != null && !filter.contains(field)) {
               continue;
            }

            Object value = field.getFieldValue(root);

            if (value != null) {
               if (value instanceof List) {
                  List list = (List) value;

                  for (int j = 0; j < list.size(); j++) {
                     XmlModel node = (XmlModel) list.get(j);

                     node.setLevel(root.getLevel() + 1);
                     sb.append(buildXml(node, filter, indented));
                  }
               } else if (value instanceof Map) {
                  Map map = (Map) value;
                  List<KeyValue> sortedKeys = sortKeys(map.keySet().iterator());

                  for (int k = 0; k < sortedKeys.size(); k++) {
                     KeyValue key = sortedKeys.get(k);
                     XmlModel node = (XmlModel) map.get(key);

                     node.setLevel(root.getLevel() + 1);
                     sb.append(buildXml(node, filter, indented));
                  }
               } else if (value instanceof XmlModel) {
                  XmlModel node = (XmlModel) value;

                  node.setLevel(root.getLevel() + 1);
                  sb.append(buildXml(node, filter, indented));
               }
            }
         }
      }
   }

   protected void buildXmlNamespace(XmlModel root, ModelFilter filter, StringBuffer sb) {
      Namespace[] nss = root.getDeclaredNamespaces();
      int len = (nss == null ? 0 : nss.length);

      for (int i = 0; i < len; i++) {
         Namespace ns = nss[i];

         if (ns.hasNamespaceURI()) {
            sb.append(" xmlns");

            if (ns.hasAlias()) {
               sb.append(':').append(ns.getAlias());
            }

            sb.append("=\"").append(ns.getName()).append('\"');
         }
      }
   }

   protected void buildXmlText(XmlModel root, ModelFilter filter, StringBuffer sb, boolean indented) throws FormatingException {
      String indent = (indented ? getSpaces(root.getLevel()) : EMPTY);
      List<DataObjectField> fields = root.getMetaData().getFields();

      for (DataObjectField f : fields) {
         if (f instanceof XmlModelField) {
            XmlModelField field = (XmlModelField) f;
            NodeType nodeType = field.getNodeType();

            if (nodeType != NodeType.TEXT && nodeType != NodeType.TEXT_CDATA) {
               continue;
            }

            if (!root.isFieldUsed(field) || filter != null && !filter.contains(field)) {
               continue;
            }

            Object value = field.getFieldValue(root);

            if (value != null) {
               if (indented) {
                  sb.append(indent).append(ONE_INDENT);
               }

               if (field.getNodeType() == NodeType.TEXT_CDATA) {
                  sb.append("<![CDATA[").append(value).append("]]>");
               } else if (field.hasFormatter()) {
                  Formatter formatter = field.getFormatter();

                  sb.append(xmlEncode(formatter.format(value, indented)));
               } else {
                  sb.append(xmlEncode(value.toString()));
               }

               sb.append("\r\n");
            }
         }
      }
   }

   public String format(Object value, boolean indented) throws FormatingException {
      return buildXml((XmlModel) value, null, indented);
   }

   private String getSpaces(int level) {
      if (level == 0) {
         return "";
      } else if (level == 1) {
         return ONE_INDENT;
      } else {
         StringBuffer sb = new StringBuffer(ONE_INDENT.length() * level);

         for (int i = 0; i < level; i++) {
            sb.append(ONE_INDENT);
         }

         return sb.toString();
      }
   }

   public XmlModel getModel() {
      return m_model;
   }

   public XmlModelField getTextField() {
      List<DataObjectField> fields = m_model.getMetaData().getFields();

      for (DataObjectField f : fields) {
         XmlModelField field = (XmlModelField) f;
         NodeType nodeType = field.getNodeType();

         if (nodeType == NodeType.TEXT || nodeType == NodeType.TEXT_CDATA) {
            return field;
         }
      }

      return null;
   }

   public void handleEnd(FormatterContext context, Event e) throws ParsingException {
      m_level--;

      if (isTopLevel()) {
         context.notifyModelCompleted();

         if (!m_model.checkConstraint()) {
            XmlModel parent = context.getLeafModel();

            if (parent == null) {
               throw new ValidationException(m_model + " is invalid");
            } else {
               throw new ValidationException(m_model + " is invalid under " + parent);
            }
         }
      }

      if (m_current != null && m_current.hasFormatter()) {
         m_model.setFieldValue(m_current, context.getFormatResult());
      }

      m_current = null;
   }

   public void handleStart(FormatterContext context, Event e) throws ParsingException {
      String localName = e.getLocalName();

      if (isTopLevel()) {
         m_model.loadAttributes(e.getAttributes());

         XmlModel parent = context.getLeafModel();
         if (parent == null) {
            context.setRootModel(m_model);
         } else {
            parent.setChildModel(m_model);
         }

         context.setCurrentModel(m_model);
         m_level++;
      } else {
         XmlModelField field = (XmlModelField) m_model.getMetaData().getField(localName);
         NodeType nodeType = field.getNodeType();

         if (nodeType == NodeType.MODEL) {
            XmlModel model = (XmlModel) ModelRegistry.newInstance(field.getModelClass());
            Formatter formatter = model.getFormatter();

            context.setCurrentFormatter(formatter);
            formatter.handleStart(context, e);
         } else if (nodeType == NodeType.ELEMENT || nodeType == NodeType.ELEMENT_CDATA) {
            if (field.hasFormatter()) {
               Formatter formatter = field.getFormatter();

               context.setCurrentFormatter(formatter);
               formatter.handleStart(context, e);
            }

            m_level++;
         } else {
            throw new ParsingException("Unknown tag(" + localName + ") found under " + m_model);
         }

         m_current = field;
      }
   }

   public void handleText(FormatterContext context, Event e) throws ParsingException {
      if (m_current != null) {
         NodeType nodeType = m_current.getNodeType();

         // For ELEMENT which has no formatter assigned
         if (nodeType == NodeType.ELEMENT || nodeType == NodeType.ELEMENT_CDATA) {
            m_model.setFieldValue(m_current, e.getText());
         }
      } else if (m_text != null) {
         m_model.setFieldValue(m_text, e.getText());
      }
   }

   public boolean ignoreWhiteSpaces() {
      return m_model.ignoreWhiteSpace();
   }

   private boolean isTopLevel() {
      return m_level == 0;
   }

   protected List<KeyValue> sortKeys(Iterator keys) {
      List<KeyValue> sortedKeys = new ArrayList<KeyValue>();

      while (keys.hasNext()) {
         KeyValue key = (KeyValue) keys.next();
         int index = sortedKeys.size();

         for (int k = 0; k < sortedKeys.size(); k++) {
            KeyValue kv = sortedKeys.get(k);

            if (key.getIndex() < kv.getIndex()) {
               index = k;
               break;
            }
         }

         sortedKeys.add(index, key);
      }

      return sortedKeys;
   }

   protected String xmlEncode(String text) {
      int len = (text == null ? 0 : text.length());
      StringBuffer sb = new StringBuffer(len + 64);

      for (int i = 0; i < len; i++) {
         char ch = (text.charAt(i));

         switch (ch) {
         case '&':
            sb.append("&amp;");
            break;
         case '<':
            sb.append("&lt;");
            break;
         case '>':
            sb.append("&gt;");
            break;
         case '"':
            sb.append("&quot;");
            break;
         default:
            sb.append(ch);
            break;
         }
      }

      return sb.toString();
   }
}
