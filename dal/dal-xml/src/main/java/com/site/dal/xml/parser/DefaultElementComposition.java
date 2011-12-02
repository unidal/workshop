package com.site.dal.xml.parser;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;

import com.site.converter.ConverterManager;
import com.site.dal.xml.XmlException;
import com.site.dal.xml.dynamic.DynamicAttributes;
import com.site.dal.xml.formatter.Formatter;
import com.site.dal.xml.registry.AttributeEntry;
import com.site.dal.xml.registry.ElementEntry;
import com.site.dal.xml.registry.XmlRegistry;
import com.site.lookup.ContainerHolder;

public class DefaultElementComposition extends ContainerHolder implements ElementComposition, LogEnabled {
   private XmlRegistry m_registry;

   private XmlParserPolicy m_policy;

   private Logger m_logger;

   public Object compose(ElementNode node) throws XmlException {
      Class<?> clazz = node.getElementEntry().getType();
      ElementEntry entry = m_registry.findElementEntry(clazz);

      if (entry == null && (clazz.isArray() || clazz.isAssignableFrom(List.class))) {
         List<Object> list = new ArrayList<Object>();

         // inject elements
         Map<String, ElementEntry> elementMap = node.getElementEntry().getElementMap();
         for (Map.Entry<String, List<Object>> e : node.getElements().entrySet()) {
            ElementEntry element = elementMap.get(e.getKey());

            if (element != null) {
               for (Object elementValue : e.getValue()) {
                  list.add(elementValue);
               }
            } else {
               handleUnknownElement("No such element(" + e.getKey() + ") defined in " + clazz);
            }
         }

         return list;
      } else if (entry != null) {
         Object object = newInstance(clazz);
         boolean hasElementsOrComponents = false;
         boolean hasDynamicAttributes = (object instanceof DynamicAttributes);

         // inject attributes
         Map<String, AttributeEntry> attributeMap = entry.getAttributeMap();
         for (Map.Entry<String, String> e : node.getAttributes().entrySet()) {
            AttributeEntry attribute = attributeMap.get(e.getKey());

            if (attribute != null) {
               injectAttribute(object, attribute, e.getValue());
            } else if (hasDynamicAttributes) {
               ((DynamicAttributes) object).setDynamicAttribute(e.getKey(), e.getValue());
            } else {
               handleUnknownElement("No such attribute(" + e.getKey() + ") defined in " + clazz);
            }
         }

         // inject elements
         Map<String, ElementEntry> elementMap = entry.getElementMap();
         for (Map.Entry<String, List<Object>> e : node.getElements().entrySet()) {
            ElementEntry element = elementMap.get(e.getKey());

            if (element != null) {
               injectElement(object, element, e.getValue());
               hasElementsOrComponents = true;
            } else {
               handleUnknownElement("No such element(" + e.getKey() + ") defined in " + clazz);
            }
         }

         // inject components
         for (Map.Entry<String, List<Object>> e : node.getComponentMap().entrySet()) {
            ElementEntry component = entry.getComponent(e.getKey());

            if (component != null) {
               injectComponent(object, component, e.getValue());
               hasElementsOrComponents = true;
            } else {
               handleUnknownElement("No such element(" + e.getKey() + ") of wrapper element(" + entry.getName()
                     + ") defined in " + clazz);
            }
         }

         if (!hasElementsOrComponents) {
            ElementEntry valueEntry = entry.getValueEntry();

            if (valueEntry != null) {
               injectValue(object, valueEntry, node.getText());
            } else {
               // ignore it
            }
         }

         return object;
      } else {
         ElementEntry elementEntry = node.getElementEntry();
         Object value = convertText(elementEntry.getType(), elementEntry.getFormat(), node.getText());

         return value;
      }
   }

   @SuppressWarnings("unchecked")
   private Object convert(Object value, Class<?> clazz) {
      if (value == null) {
         return null;
      } else if (clazz.isArray() || clazz.isAssignableFrom(List.class)) {
         // do nothing
      } else {
         if (value instanceof List) {
            List<Object> list = (List<Object>) value;

            if (list.size() == 1) {
               value = list.get(0);
            }
         }
      }

      return ConverterManager.getInstance().convert(value, clazz);
   }

   private Object convertText(Class<?> type, String format, String text) throws XmlException {
      if (format != null && format.length() > 0) {
         Formatter<?> formatter = lookup(Formatter.class, type.getName());

         release(formatter);
         return formatter.parse(format, text);
      } else {
         return convert(text, type);
      }
   }

   public void enableLogging(Logger logger) {
      m_logger = logger;
   }

   private void handleInvalidValue(String message, Throwable cause) throws XmlException {
      switch (m_policy.forInvalidValue()) {
      case IGNORE:
         // ignore it
         break;
      case WARNING:
         m_logger.warn(message, cause);
         break;
      case ERROR:
         throw new XmlException(message, cause);
      }
   }

   private void handleUnknownElement(String message) throws XmlException {
      switch (m_policy.forUnknownElement()) {
      case IGNORE:
         // ignore it
         break;
      case WARNING:
         m_logger.warn(message);
         break;
      case ERROR:
         throw new XmlException(message);
      }
   }

   private void inject(Object object, AnnotatedElement annotated, Object value) throws XmlException {
      try {
         if (annotated instanceof Field) {
            Field field = (Field) annotated;

            field.setAccessible(true);
            field.set(object, value);
         } else if (annotated instanceof Method) {
            ((Method) annotated).invoke(object, new Object[] { value });
         }
      } catch (Exception e) {
         handleInvalidValue("Error when injecting data to " + annotated, e);
      }
   }

   private void injectAttribute(Object object, AttributeEntry attribute, String text) throws XmlException {
      Class<?> type = attribute.getType();

      if (attribute.isList()) {
         String[] items = text.split("\\s");
         Object value = convert(items, type);

         inject(object, attribute.getAnnotatedElement(), value);
      } else {
         Object value = convertText(attribute.getType(), attribute.getFormat(), text);

         inject(object, attribute.getAnnotatedElement(), value);
      }
   }

   private void injectComponent(Object object, ElementEntry component, List<Object> values) throws XmlException {
      Object componentValue = convert(values, component.getType());

      inject(object, component.getAnnotatedElement(), componentValue);
   }

   private void injectElement(Object object, ElementEntry element, Object value) throws XmlException {
      Object elementValue = convert(value, element.getType());

      inject(object, element.getAnnotatedElement(), elementValue);
   }

   private void injectValue(Object object, ElementEntry valueEntry, String text) throws XmlException {
      Class<?> type = valueEntry.getType();

      if (valueEntry.isList()) {
         String[] items = text.split("\\s");
         Object value = convert(items, type);

         inject(object, valueEntry.getAnnotatedElement(), value);
      } else {
         Object value = convert(text, type);

         inject(object, valueEntry.getAnnotatedElement(), value);
      }
   }

   private Object newInstance(Class<?> clazz) throws XmlException {
      try {
         return clazz.newInstance();
      } catch (Exception e) {
         throw new XmlException("Can't instantiate " + clazz, e);
      }
   }
}
