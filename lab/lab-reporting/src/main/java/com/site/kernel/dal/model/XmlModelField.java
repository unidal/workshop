package com.site.kernel.dal.model;

import com.site.kernel.dal.Cardinality;
import com.site.kernel.dal.DataObjectField;
import com.site.kernel.dal.ValueType;
import com.site.kernel.dal.model.common.Formatter;
import com.site.kernel.dal.model.helpers.DateFormatter;
import com.site.kernel.dal.model.helpers.TimeFormatter;

public class XmlModelField extends DataObjectField {
   private Formatter m_formatter;

   private XmlModelKey m_key;

   private Class m_modelClass;

   private Cardinality m_modelType;

   private Namespace m_namespace;

   private NodeType m_nodeType;

   private Object m_defaultValue;

   public XmlModelField(Namespace namespace, String name, NodeType nodeType, Cardinality modelType) {
      this(namespace, name, nodeType, modelType, null);
   }

   public XmlModelField(Namespace namespace, String name, NodeType nodeType, Cardinality modelType, XmlModelKey key) {
      super(name, null);

      m_namespace = namespace;
      m_nodeType = nodeType;
      m_modelType = modelType;
      m_key = key;

      doIntegrityCheckAndSetup();
   }

   public XmlModelField(Namespace namespace, String name, NodeType nodeType, ValueType valueType) {
      this(namespace, name, nodeType, valueType, null, null);
   }

   public XmlModelField(Namespace namespace, String name, NodeType nodeType, ValueType valueType, Object defaultValue) {
      this(namespace, name, nodeType, valueType, null, defaultValue);
   }

   public XmlModelField(Namespace namespace, String name, NodeType nodeType, ValueType valueType, Formatter formatter) {
      this(namespace, name, nodeType, valueType, formatter, null);
   }

   public XmlModelField(Namespace namespace, String name, NodeType nodeType, ValueType valueType, Formatter formatter, Object defaultValue) {
      super(name, valueType);

      m_namespace = namespace;
      m_nodeType = nodeType;
      m_formatter = formatter;
      m_defaultValue = defaultValue;

      doIntegrityCheckAndSetup();
   }

   public XmlModelField(String name, NodeType nodeType, Cardinality modelType, Class modelClass) {
      this(name, nodeType, modelType, modelClass, null);
   }

   public XmlModelField(String name, NodeType nodeType, Cardinality modelType, Class modelClass, XmlModelKey key) {
      super(name, null);

      m_nodeType = nodeType;
      m_modelType = modelType;
      m_modelClass = modelClass;
      m_key = key;
   }

   public XmlModelField(String name, NodeType nodeType, ValueType valueType) {
      this(name, nodeType, valueType, null, null);
   }

   public XmlModelField(String name, NodeType nodeType, ValueType valueType, Object defaultValue) {
      this(name, nodeType, valueType, null, defaultValue);
   }

   public XmlModelField(String name, NodeType nodeType, ValueType valueType, Formatter formatter) {
      this(name, nodeType, valueType, formatter, null);
   }

   public XmlModelField(String name, NodeType nodeType, ValueType valueType, Formatter formatter, Object defaultValue) {
      super(name, valueType);

      m_nodeType = nodeType;
      m_formatter = formatter;
      m_defaultValue = defaultValue;

      doIntegrityCheckAndSetup();
   }

   private void doIntegrityCheckAndSetup() {
      if (m_modelType == null) {
         if (m_nodeType == NodeType.ELEMENT_CDATA && m_formatter != null) {
            throw new IllegalArgumentException("Formatter can't co-exist with ELEMENT_CDATA");
         } else if (m_nodeType != NodeType.ELEMENT_CDATA && m_formatter == null) {
            if (getValueType() == ValueType.TIME) {
               m_formatter = TimeFormatter.DEFAULT;
            } else if (getValueType() == ValueType.DATE) {
               m_formatter = DateFormatter.DEFAULT;
            }
         }
      }
   }

   public Object getDefaultValue() {
      return m_defaultValue;
   }

   public Formatter getFormatter() {
      return m_formatter;
   }

   public XmlModelKey getKey() {
      return m_key;
   }

   public Class getModelClass() {
      return m_modelClass;
   }

   public Cardinality getCardinality() {
      return m_modelType;
   }

   public Namespace getNamespace() {
      return m_namespace;
   }

   public NodeType getNodeType() {
      return m_nodeType;
   }

   public String getTagName() {
      if (m_namespace != null && m_namespace.hasAlias()) {
         return m_namespace.getAlias() + ":" + getName();
      } else {
         return getName();
      }
   }

   public boolean hasDefaultValue() {
      return m_defaultValue != null;
   }

   public boolean hasFormatter() {
      return m_formatter != null;
   }

   public boolean hasNamespace() {
      return m_namespace != null;
   }

   public boolean isModel() {
      return m_modelType != null;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer(256);

      sb.append("XmlModelField[");
      sb.append("namespace=").append(m_namespace);
      sb.append(",name=").append(getName());
      sb.append(",modelType=").append(m_modelType);
      sb.append(",modelClass=").append(m_modelClass);
      sb.append(",keyInfo=").append(m_key);
      sb.append(",nodeType=").append(m_nodeType);
      sb.append(",valueType=").append(getValueType());
      sb.append(",defaultValue=").append(m_defaultValue);
      sb.append(",formatter=").append(m_formatter);
      sb.append(",index=").append(getIndex());
      sb.append("]");

      return sb.toString();
   }
}