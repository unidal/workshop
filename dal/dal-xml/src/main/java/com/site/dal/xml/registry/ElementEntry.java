package com.site.dal.xml.registry;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ElementEntry {
   private String m_name;

   private String m_defaultValue;

   private boolean m_required;

   private String m_format;

   private Class<?> m_type;

   private AnnotatedElement m_annotatedElement;

   private boolean m_value;

   private boolean m_list;

   private boolean m_inComponent;

   private List<AttributeEntry> m_attributes;

   private List<ElementEntry> m_elements;

   private List<ElementEntry> m_components;

   public void addAttribute(AttributeEntry attribute) {
      if (m_attributes == null) {
         m_attributes = new ArrayList<AttributeEntry>(3);
      }

      m_attributes.add(attribute);
   }

   public void addComponent(ElementEntry element) {
      if (m_components == null) {
         m_components = new ArrayList<ElementEntry>(3);
      }

      m_components.add(element);
   }

   public void addElement(ElementEntry element) {
      if (m_elements == null) {
         m_elements = new ArrayList<ElementEntry>(3);
      }

      m_elements.add(element);
   }

   String asString(int idents) {
      StringBuilder sb = new StringBuilder(1024);

      sb.append(space(idents)).append("<ElementEntry");

      if (m_name != null) {
         sb.append(" name=\"").append(m_name).append("\"");
      }

      if (m_defaultValue != null && !m_defaultValue.equals("\u0000")) {
         sb.append(" defaultValue=\"").append(m_defaultValue).append("\"");
      }

      if (m_required) {
         sb.append(" required=\"").append(m_required).append("\"");
      }

      if (m_format != null && m_format.length() > 0) {
         sb.append(" format=\"").append(m_format).append("\"");
      }

      if (m_list) {
         sb.append(" isList=\"").append(m_list).append("\"");
      }

      if (m_value) {
         sb.append(" hasValue=\"").append(m_value).append("\"");
      }

      if (m_inComponent) {
         sb.append(" inComponent=\"").append(m_inComponent).append("\"");
      }

      sb.append(">\r\n");
      idents++;

      sb.append(space(idents)).append("<type>").append(m_type).append("</type>\r\n");

      if (m_annotatedElement != null) {
         sb.append(space(idents)).append("<member>").append(m_annotatedElement).append("</member>\r\n");
      }

      if (m_attributes != null) {
         for (AttributeEntry e : m_attributes) {
            sb.append(e.asString(idents));
         }
      }

      if (m_elements != null) {
         for (ElementEntry e : m_elements) {
            sb.append(e.asString(idents));
         }
      }

      if (m_components != null) {
         for (ElementEntry e : m_components) {
            sb.append(e.asString(idents));
         }
      }

      idents--;
      sb.append(space(idents)).append("</ElementEntry>\r\n");

      return sb.toString();
   }

   public AnnotatedElement getAnnotatedElement() {
      return m_annotatedElement;
   }

   public List<AttributeEntry> getAttributes() {
      if (m_attributes == null) {
         return Collections.emptyList();
      } else {
         return m_attributes;
      }
   }

   public List<ElementEntry> getElements() {
      if (m_elements == null) {
         return Collections.emptyList();
      } else {
         return m_elements;
      }
   }

   public boolean hasComponents() {
      return m_components != null && m_components.size() > 0;
   }

   public List<ElementEntry> getComponents() {
      if (m_components == null) {
         return Collections.emptyList();
      } else {
         return m_components;
      }
   }

   public Map<String, AttributeEntry> getAttributeMap() {
      if (m_attributes == null) {
         return Collections.emptyMap();
      } else {
         Map<String, AttributeEntry> map = new HashMap<String, AttributeEntry>(m_attributes.size() * 2);

         for (AttributeEntry attribute : m_attributes) {
            map.put(attribute.getName(), attribute);
         }

         return map;
      }
   }

   public ElementEntry getComponent(String name) {
      if (m_components != null) {
         for (ElementEntry component : m_components) {
            ElementEntry element = component.getElement(name);

            if (element != null) {
               return component;
            }
         }
      }

      return null;
   }

   public ElementEntry getComponentElement(String name) {
      if (m_components != null) {
         for (ElementEntry component : m_components) {
            ElementEntry element = component.getElement(name);

            if (element != null) {
               return element;
            }
         }
      }

      return null;
   }

   public String getDefaultValue() {
      return m_defaultValue;
   }

   public ElementEntry getElement(Class<?> type) {
      if (m_elements != null) {
         for (ElementEntry e : m_elements) {
            if (e.getType() == type) {
               return e;
            }
         }
      }

      return null;
   }

   public ElementEntry getElement(String name) {
      if (m_elements != null) {
         for (ElementEntry e : m_elements) {
            if (!e.isValue() && e.getName().equals(name)) {
               return e;
            }
         }
      }

      return null;
   }

   public Map<String, ElementEntry> getElementMap() {
      if (m_elements == null) {
         return Collections.emptyMap();
      } else {
         Map<String, ElementEntry> map = new HashMap<String, ElementEntry>(m_elements.size() * 2);

         for (ElementEntry element : m_elements) {
            map.put(element.getName(), element);
         }

         return map;
      }
   }

   public String getFormat() {
      return m_format;
   }

   public String getName() {
      return m_name;
   }

   public Class<?> getType() {
      return m_type;
   }

   public ElementEntry getValueEntry() {
      if (m_elements != null) {
         for (ElementEntry e : m_elements) {
            if (e.isValue()) {
               return e;
            }
         }
      }

      return null;
   }

   public boolean isInComponent() {
      return m_inComponent;
   }

   public boolean isList() {
      return m_list;
   }

   public boolean isRequired() {
      return m_required;
   }

   public boolean isValue() {
      return m_value;
   }

   public void setAnnotatedElement(AnnotatedElement annotatedElement) {
      m_annotatedElement = annotatedElement;
   }

   public void setDefaultValue(String defaultValue) {
      m_defaultValue = defaultValue;
   }

   public void setFormat(String format) {
      m_format = format;
   }

   public void setInComponent(boolean inComponent) {
      m_inComponent = inComponent;
   }

   public void setList(boolean list) {
      m_list = list;
   }

   public void setName(String name) {
      m_name = name;
   }

   public void setRequired(boolean required) {
      m_required = required;
   }

   public void setType(Class<?> type) {
      m_type = type;
   }

   public void setValue(boolean value) {
      m_value = value;
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
      return asString(0);
   }

   public void validate() {
      if (m_elements != null) {
         boolean hasValue = false;

         for (ElementEntry e : m_elements) {
            if (e.isValue()) {
               if (!hasValue) {
                  hasValue = true;
               } else {
                  throw new RuntimeException("Only at most one XmlValue can be annotated to " + m_type);
               }
            }
         }
      }
   }
}