package com.site.dal.xml.registry;

import java.lang.reflect.AnnotatedElement;

public final class AttributeEntry {
   private String m_name;

   private boolean m_required;

   private String m_format;

   private Class<?> m_type;

   private boolean m_list;

   private AnnotatedElement m_annotatedElement;

   String asString(int idents) {
      StringBuilder sb = new StringBuilder(1024);

      sb.append(space(idents)).append("<AttributeEntry");

      if (m_name != null) {
         sb.append(" name=\"").append(m_name).append("\"");
      }

      if (m_required) {
         sb.append(" required=\"").append(m_required).append("\"");
      }

      if (m_format.length() > 0) {
         sb.append(" format=\"").append(m_format).append("\"");
      }

      if (m_list) {
         sb.append(" isList=\"").append(m_list).append("\"");
      }

      sb.append(">\r\n");
      idents++;

      sb.append(space(idents)).append("<type>").append(m_type).append("</type>\r\n");
      sb.append(space(idents)).append("<member>").append(m_annotatedElement).append("</member>\r\n");

      idents--;
      sb.append(space(idents)).append("</AttributeEntry>\r\n");

      return sb.toString();
   }

   public AnnotatedElement getAnnotatedElement() {
      return m_annotatedElement;
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

   public boolean isList() {
      return m_list;
   }

   public boolean isRequired() {
      return m_required;
   }

   public void setAnnotatedElement(AnnotatedElement annotatedElement) {
      m_annotatedElement = annotatedElement;
   }

   public void setFormat(String format) {
      m_format = format;
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
}