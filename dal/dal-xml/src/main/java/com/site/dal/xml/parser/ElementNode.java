package com.site.dal.xml.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.site.dal.xml.registry.ElementEntry;

public class ElementNode {
   private boolean m_root;

   private ElementEntry m_elementEntry;

   private Map<String, String> m_attributes;

   private Map<String, List<Object>> m_elements;

   private Map<String, List<Object>> m_components;

   private String m_text;

   public ElementNode(boolean root) {
      m_root = root;
   }

   public void addComponentValue(String name, Object value) {
      if (m_components == null) {
         m_components = new LinkedHashMap<String, List<Object>>();
      }

      List<Object> values = m_components.get(name);

      if (values == null) {
         values = new ArrayList<Object>(3);
         m_components.put(name, values);
      }

      values.add(value);
   }

   @SuppressWarnings("unchecked")
   public void addElementValue(String name, Object value) {
      if (m_elements == null) {
         m_elements = new LinkedHashMap<String, List<Object>>();
      }

      List<Object> values = m_elements.get(name);

      if (values == null) {
         values = new ArrayList<Object>(3);
         m_elements.put(name, values);
      }

      if (value instanceof List) {
         values.addAll((List<Object>) value);
      } else {
         values.add(value);
      }
   }

   public Map<String, String> getAttributes() {
      if (m_attributes == null) {
         return Collections.emptyMap();
      } else {
         return m_attributes;
      }
   }

   public Map<String, List<Object>> getComponentMap() {
      if (m_components == null) {
         return Collections.emptyMap();
      } else {
         return m_components;
      }
   }

   public ElementEntry getElementEntry() {
      return m_elementEntry;
   }

   public Map<String, List<Object>> getElements() {
      if (m_elements == null) {
         return Collections.emptyMap();
      } else {
         return m_elements;
      }
   }

   public Object getRootElementValue() {
      if (m_elements != null && m_elements.size() == 1) {
         List<Object> list =  m_elements.values().iterator().next();

         return list.get(0);
      }

      throw new RuntimeException("No root element found.");
   }

   public String getText() {
      return m_text;
   }

   public boolean isRoot() {
      return m_root;
   }

   public void setAttributeValue(String name, String value) {
      if (m_attributes == null) {
         m_attributes = new LinkedHashMap<String, String>();
      }

      m_attributes.put(name, value);
   }

   public void setElementEntry(ElementEntry entry) {
      m_elementEntry = entry;
   }

   public void setText(String text) {
      m_text = text;
   }

   private String space(int count) {
      StringBuilder sb = new StringBuilder(count * 3);

      for (int i = 0; i < count; i++) {
         sb.append("   ");
      }

      return sb.toString();
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder(1024);
      String name = m_elementEntry == null ? null : m_elementEntry.getName();
      int idents = 0;

      if (name != null) {
         sb.append(space(idents)).append("<").append(name);

         if (m_attributes != null) {
            for (Map.Entry<String, String> e : m_attributes.entrySet()) {
               sb.append(' ').append(e.getKey()).append("=\"").append(e.getValue()).append("\"");
            }
         }

         sb.append(">\r\n");

         idents++;

         if (m_elements != null) {
            for (Map.Entry<String, List<Object>> e : m_elements.entrySet()) {
               for (Object value : e.getValue()) {
                  sb.append(space(idents)).append("<").append(e.getKey()).append(">");
                  sb.append(value);
                  sb.append("</").append(e.getKey()).append(">\r\n");
               }
            }
         }

         if (m_components != null) {
            for (Map.Entry<String, List<Object>> e : m_components.entrySet()) {
               List<Object> list = e.getValue();

               for (Object item : list) {
                  sb.append(space(idents)).append("<").append(e.getKey()).append(">");
                  sb.append(item);
                  sb.append("</").append(e.getKey()).append(">\r\n");
               }
            }
         }

         if (m_text != null && m_text.trim().length() > 0) {
            sb.append(space(idents)).append("<![CDATA[").append(m_text).append("]]>\r\n");
         }

         idents--;

         sb.append(space(idents)).append("</").append(name).append(">\r\n");
      }

      return sb.toString();
   }
}
