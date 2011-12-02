package com.site.kernel.dal.model;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.xml.sax.Attributes;

import com.site.kernel.dal.Cardinality;
import com.site.kernel.dal.DataObject;
import com.site.kernel.dal.DataObjectField;
import com.site.kernel.dal.FieldAccessException;
import com.site.kernel.dal.ValueType;
import com.site.kernel.dal.model.common.Event;
import com.site.kernel.dal.model.common.Formatter;
import com.site.kernel.dal.model.common.KeyValue;
import com.site.kernel.dal.model.common.ParsingException;
import com.site.kernel.dal.model.helpers.FormatterContext;
import com.site.kernel.dal.model.helpers.ModelFormatter;

public abstract class XmlModel extends DataObject {
   private int m_level;

   private String m_modelName;

   private Namespace m_ns;

   public XmlModel(Namespace ns, String modelName) {
      this(ns, modelName, false);
   }

   public XmlModel(Namespace ns, String modelName, boolean virtual) {
      super();

      m_ns = ns;
      m_modelName = modelName;
      m_level = (virtual ? -1 : 0);
   }

   protected static void initialize(Namespace[] namespaces) {
      Class doClass = CallerIntrospector.getCaller();
      XmlModelNaming naming = new XmlModelNaming();
      XmlModelMetaData metadata = new XmlModelMetaData(naming, doClass, namespaces);

      try {
         metadata.initialize(doClass);
      } catch (RuntimeException e) {
         e.printStackTrace();
         throw e;
      }
   }

   @SuppressWarnings("unchecked")
   protected void addChild(XmlModelField field, XmlModel child) {
      if (!field.isModel()) {
         throw new FieldAccessException(field + " must be a MODEL");
      }

      Object obj = field.getFieldValue(this);

      if (obj instanceof List) {
         List list = (List) obj;

         list.add(child);
      } else if (obj instanceof Map) {
         Map map = (Map) obj;
         KeyValue key = buildKey(field.getKey(), child);

         key.setIndex(map.size());
         map.put(key, child);
      } else {
         throw new FieldAccessException("Method addChild can not be called by " + field);
      }
   }

   protected void afterValidate(Stack<XmlModel> parents) {
      // to be overridden in sub-class
   }

   protected void beforeValidate(Stack<XmlModel> parents) {
      // to be overridden in sub-class
   }

   private KeyValue buildKey(XmlModelKey key, XmlModel model) {
      XmlModelField[] fields = key.getFields();
      Object[] values = new Object[fields.length];

      for (int i = 0; i < fields.length; i++) {
         values[i] = fields[i].getFieldValue(model);
      }

      return new KeyValue(values);
   }

   public abstract boolean checkConstraint() throws ValidationException;

   protected abstract void doValidate(Stack<XmlModel> parents);

   public Namespace[] getDeclaredNamespaces() {
      return getMetaData().getNamespaces();
   }

   public Formatter getFormatter() {
      return new ModelFormatter(this);
   }

   public int getLevel() {
      return m_level;
   }

   protected Object getModelByKey(XmlModelField field, Object[] values) {
      Object obj = field.getFieldValue(this);

      if (obj instanceof Map) {
         Map map = (Map) obj;
         KeyValue key = new KeyValue(values);

         return map.get(key);
      } else {
         throw new FieldAccessException(field + " can not be accessed by key");
      }
   }

   public String getModelName() {
      return m_modelName;
   }

   public Namespace getNamespace() {
      return m_ns;
   }

   public abstract void loadAttributes(Attributes attrs);

   public boolean ignoreWhiteSpace() {
      return true;
   }

   public boolean isEmpty(String src) {
      return src == null || src.length() == 0;
   }

   public XmlModelMetaData getMetaData() {
      return (XmlModelMetaData) super.getMetaData();
   }

   public void setChildModel(XmlModel child) {
      XmlModelMetaData metadata = getMetaData();
      XmlModelField field = (XmlModelField) metadata.getField(child.getModelName());
      Cardinality modelType = field.getCardinality();

      if (modelType == Cardinality.ZERO_TO_MANY || modelType == Cardinality.ONE_TO_MANY) {
         addChild(field, child);
      } else if (modelType == Cardinality.ZERO_TO_ONE || modelType == Cardinality.EXACT_ONE) {
         field.setFieldValue(this, new Object[] { child });
      }
   }

   protected void setFieldValue(XmlModelField field, Attributes attrs) {
      if (field.isModel()) {
         // No field value can be assigned to a Model
         return;
      }

      if (attrs != null && field.getNodeType() == NodeType.ATTRIBUTE) {
         String value = attrs.getValue(field.getName());

         if (value != null) {
            if (field.hasFormatter()) {
               Formatter formatter = field.getFormatter();
               FormatterContext context = new FormatterContext();
               Event event = new Event(value);

               try {
                  context.setCurrentFormatter(formatter);
                  formatter.handleText(context, event);
                  setFieldValue(field, context.getFormatResult());
               } catch (ParsingException e) {
                  throw new IllegalArgumentException("Error set default value for " + field + ", message: " + e.getMessage());
               }
            } else {
               setFieldValue(field, value);
               return;
            }
         }
      }

      if (field.hasDefaultValue()) {
         Object defaultValue = field.getDefaultValue();

         if (field.hasFormatter() && defaultValue instanceof String) {
            Formatter formatter = field.getFormatter();
            FormatterContext context = new FormatterContext();
            Event event = new Event((String) defaultValue);

            try {
               context.setCurrentFormatter(formatter);
               formatter.handleText(context, event);
               setFieldValue(field, context.getFormatResult());
            } catch (ParsingException e) {
               throw new IllegalArgumentException("Error set default value for " + field + ", message: " + e.getMessage());
            }
         } else {
            setFieldValue(field, defaultValue);
         }

         // Clear flag since it's set by default value
         // it should not be picked up by buildXml
         setFieldUsed(field, false);
      }
   }

   public void setFieldValue(String fieldName, String text) {
      XmlModelMetaData metadata = getMetaData();
      XmlModelField fi = (XmlModelField) metadata.getField(fieldName);

      setFieldValue(fi, text);
   }

   public void setLevel(int level) {
      m_level = level;
   }

   /**
    * for debugging purpose
    */
   public String toString() {
      StringBuffer sb = new StringBuffer(256);
      List<DataObjectField> fields = getMetaData().getFields();
      boolean first = true;

      sb.append(getModelName());
      sb.append('[');

      for (DataObjectField f : fields) {
         XmlModelField field = (XmlModelField) f;
         NodeType nodeType = field.getNodeType();

         if (nodeType != NodeType.ATTRIBUTE && nodeType != NodeType.ELEMENT) {
            continue;
         }

         Object value = field.getFieldValue(this);

         if (!first) {
            sb.append(',');
         }

         first = false;
         sb.append(field.getTagName()).append('=');

         if (field.getValueType() == ValueType.STRING && value != null) {
            sb.append('"').append(value).append('"');
         } else {
            sb.append(value);
         }
      }

      sb.append(']');
      return sb.toString();
   }

   public final void validate(Stack<XmlModel> parents) {
      beforeValidate(parents);
      parents.push(this);
      doValidate(parents);
      parents.pop();
      afterValidate(parents);
   }
}