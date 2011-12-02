package com.site.kernel.dal.model;

import java.util.ArrayList;
import java.util.List;

public class ModelFilter {
   private List<XmlModelField> m_fields = new ArrayList<XmlModelField>();

   public ModelFilter(XmlModelField[] XmlModelFields) {
      add(XmlModelFields);
   }

   public void add(ModelFilter subFilter) {
      m_fields.addAll(subFilter.m_fields);
   }

   public void add(XmlModelField[] XmlModelFields) {
      if (XmlModelFields == null) {
         return;
      }

      for (int i = 0; i < XmlModelFields.length; i++) {
         add(XmlModelFields[i]);
      }
   }

   public boolean add(XmlModelField XmlModelField) {
      if (XmlModelField == null) {
         return false;
      }

      if (!m_fields.contains(XmlModelField)) {
         return m_fields.add(XmlModelField);
      } else {
         return true;
      }
   }

   public boolean remove(XmlModelField XmlModelField) {
      if (XmlModelField == null) {
         return false;
      }

      return m_fields.remove(XmlModelField);
   }

   public boolean contains(XmlModelField XmlModelField) {
      return m_fields.contains(XmlModelField);
   }
}
