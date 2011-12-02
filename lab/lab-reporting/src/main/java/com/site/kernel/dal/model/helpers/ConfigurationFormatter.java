package com.site.kernel.dal.model.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.site.kernel.dal.model.XmlModel;
import com.site.kernel.dal.model.XmlModelMetaData;
import com.site.kernel.dal.model.common.Event;
import com.site.kernel.dal.model.common.FormatingException;
import com.site.kernel.dal.model.common.Formatter;
import com.site.kernel.dal.model.common.ParsingException;
import com.site.kernel.util.SequencedHashMap;

public class ConfigurationFormatter implements Formatter {
   private XmlModel m_model;

   private String m_fieldNames;

   private String m_fieldValues;

   private int m_level;

   private Stack<List<String>> m_names = new Stack<List<String>>();

   private Stack<List<List>> m_values = new Stack<List<List>>();

   private Stack<List> m_curs = new Stack<List>();

   private List<String> m_rootNames;

   private List<List> m_rootValues;

   public ConfigurationFormatter(XmlModel model, String fieldNames, String fieldValues) {
      m_model = model;
      m_fieldNames = fieldNames;
      m_fieldValues = fieldValues;
      m_level = 0;
      m_rootNames = new ArrayList<String>();
      m_rootValues = new ArrayList<List>();
   }

   public String format(Object value, boolean indented) throws FormatingException {
      XmlModel root = (XmlModel) value;

      return buildXml(root.getModelName(), root.getLevel(), m_rootNames, m_rootValues, indented);
   }

   private String buildXml(String tagName, int level, List<String> names, List<List> values, boolean indented) {
      StringBuffer sb = new StringBuffer();
      String indent = (indented && level > 0 ? getSpaces(level) : EMPTY);

      // Render START tag
      if (indented) {
         sb.append(indent);
      }
      sb.append('<').append(tagName).append(">\r\n");

      // Render ELEMENT
      for (int i = 0; i < names.size(); i++) {
         String name = names.get(i);
         List vals = values.get(i);

         for (int j = 0; j < vals.size(); j++) {
            Object val = values.get(j);

            if (val instanceof String) {
               if (indented) {
                  sb.append(indent).append(ONE_INDENT);
               }

               sb.append("<").append(name).append(">");
               sb.append(val);
               sb.append("</").append(name).append(">\r\n");
            } else if (val instanceof Map) {
               Map map = (Map) val;
               List<String> nms = new ArrayList<String>(map.size());
               List<List> vls = new ArrayList<List>(map.size());
               Iterator iter = map.keySet().iterator();

               while (iter.hasNext()) {
                  String nm = (String) iter.next();
                  List vl = (List) map.get(nm);

                  nms.add(nm);
                  vls.add(vl);
               }

               sb.append(buildXml(name, level + 1, nms, vls, indented));
            }
         }
      }

      // Render END tag
      if (indented) {
         sb.append(indent);
      }
      sb.append("</").append(tagName).append(">\r\n");

      return sb.toString();
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

   @SuppressWarnings("unchecked")
   public void handleEnd(FormatterContext context, Event e) throws ParsingException {
      m_level--;

      if (isTopLevel()) {
         context.notifyModelCompleted();

         XmlModelMetaData metadata = m_model.getMetaData();

         m_model.setFieldValue(metadata.getField(m_fieldNames), m_names.peek());
         m_model.setFieldValue(metadata.getField(m_fieldValues), m_values.peek());
      } else {
         List keys = m_names.pop();
         List vals = m_values.pop();
         List<Map> curs = m_curs.pop();

         if (!keys.isEmpty() && !vals.isEmpty()) {
            Map map = new SequencedHashMap();

            for (int i = 0; i < keys.size(); i++) {
               map.put(keys.get(i), vals.get(i));
            }

            curs.add(map);
         }
      }
   }

   public void handleStart(FormatterContext context, Event e) throws ParsingException {
      if (isTopLevel()) {
         m_names.push(m_rootNames);
         m_values.push(m_rootValues);
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
         String name = e.getLocalName();
         List<String> keys = m_names.peek();
         List<List> vals = m_values.peek();
         int index = keys.indexOf(name);
         List curs;

         if (index < 0) {
            curs = new ArrayList(3);

            keys.add(name);
            vals.add(curs);
         } else {
            curs = vals.get(index);
         }

         m_names.push(new ArrayList<String>(3));
         m_values.push(new ArrayList<List>(3));
         m_curs.push(curs);
         m_level++;
      }
   }

   @SuppressWarnings("unchecked")
   public void handleText(FormatterContext context, Event e) throws ParsingException {
      if (!isTopLevel()) {
         List<String> curs = m_curs.peek();

         curs.add(e.getText());
      }
   }

   public boolean ignoreWhiteSpaces() {
      return m_model.ignoreWhiteSpace();
   }

   private boolean isTopLevel() {
      return m_level == 0;
   }

}
