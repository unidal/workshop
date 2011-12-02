package com.site.dal.xml.registry;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.site.converter.TypeUtil;
import com.site.dal.xml.annotation.XmlAttribute;
import com.site.dal.xml.annotation.XmlElement;
import com.site.dal.xml.annotation.XmlElementWrapper;
import com.site.dal.xml.annotation.XmlElements;
import com.site.dal.xml.annotation.XmlList;
import com.site.dal.xml.annotation.XmlRootElement;
import com.site.dal.xml.annotation.XmlValue;

public class DefaultXmlRegistry implements XmlRegistry {
   private XmlRegistryFilter m_filter;

   private Map<String, Class<?>> m_rootElementMap = new HashMap<String, Class<?>>();

   private Map<Class<?>, ElementEntry> m_elementEntryMap = new HashMap<Class<?>, ElementEntry>();

   public ElementEntry findElementEntry(Class<?> clazz) {
      return m_elementEntryMap.get(clazz);
   }

   public Class<?> findRootElementType(String name) {
      return m_rootElementMap.get(name);
   }

   private void processAnnotatedElement(ElementEntry entry, AnnotatedElement annotated, Type type, Set<Class<?>> done) {
      validateAnnotatedElement(annotated);

      XmlList l = annotated.getAnnotation(XmlList.class);
      XmlAttribute a = annotated.getAnnotation(XmlAttribute.class);
      XmlValue v = annotated.getAnnotation(XmlValue.class);
      XmlElement e = annotated.getAnnotation(XmlElement.class);
      XmlElements es = annotated.getAnnotation(XmlElements.class);
      XmlElementWrapper er = annotated.getAnnotation(XmlElementWrapper.class);

      if (a != null) {
         AttributeEntry attribute = new AttributeEntry();

         attribute.setName(a.name());
         attribute.setRequired(a.required());
         attribute.setFormat(a.format());
         attribute.setType(TypeUtil.getRawType(type));
         attribute.setList(l != null);
         attribute.setAnnotatedElement(annotated);

         entry.addAttribute(attribute);
      } else if (v != null) {
         ElementEntry element = new ElementEntry();

         element.setRequired(v.required());
         element.setFormat(v.format());
         element.setType(TypeUtil.getRawType(type));
         element.setList(l != null);
         element.setAnnotatedElement(annotated);
         element.setValue(true);

         entry.addElement(element);
      } else {
         if (er != null) {
            ElementEntry wrapperElement = new ElementEntry();

            wrapperElement.setName(er.name());
            wrapperElement.setType(TypeUtil.getRawType(type));
            wrapperElement.setAnnotatedElement(annotated);

            entry.addElement(wrapperElement);
            entry = wrapperElement;
            type = TypeUtil.getComponentType(type);
            annotated = null;
         }

         if (e != null) {
            ElementEntry element = new ElementEntry();

            element.setName(e.name());
            element.setDefaultValue(e.defaultValue());
            element.setFormat(e.format());
            element.setRequired(e.required());
            element.setType(TypeUtil.getRawType(type));
            element.setList(l != null);
            element.setAnnotatedElement(annotated);

            entry.addElement(element);
         } else if (es != null) {
            ElementEntry component = new ElementEntry();

            component.setType(TypeUtil.getRawType(type));
            component.setAnnotatedElement(annotated);

            for (XmlElement item : es.value()) {
               ElementEntry element = new ElementEntry();

               if (item.name() == null || item.type() == XmlElement.DEFAULT.class) {
                  throw new RuntimeException(
                        "Both name and type attributes of XmlElement annotation should be specified");
               }

               element.setName(item.name());
               element.setDefaultValue(item.defaultValue());
               element.setFormat(item.format());
               element.setRequired(item.required());
               element.setType(item.type());
               element.setInComponent(true);

               component.addElement(element);
               register(item.type(), done);
            }

            entry.addComponent(component);
         }

         registerType(type, done);
      }
   }

   private void validateAnnotatedElement(AnnotatedElement annotated) {
      XmlList l = annotated.getAnnotation(XmlList.class);
      XmlAttribute a = annotated.getAnnotation(XmlAttribute.class);
      XmlValue v = annotated.getAnnotation(XmlValue.class);
      XmlElement e = annotated.getAnnotation(XmlElement.class);
      XmlElements es = annotated.getAnnotation(XmlElements.class);
      XmlElementWrapper er = annotated.getAnnotation(XmlElementWrapper.class);
      int magic = 0;

      magic += (l == null ? 0 : 1);
      magic += (a == null ? 0 : 2);
      magic += (v == null ? 0 : 4);
      magic += (e == null ? 0 : 8);
      magic += (es == null ? 0 : 16);
      magic += (er == null ? 0 : 32);

      switch (magic) {
      case 0: // nothing
         break;
      case 2: // attribute
      case 3: // attribute + list
         break;
      case 4: // value
      case 5: // value + list
         break;
      case 8: // element
         break;
      case 16: // elements
         break;
      case 24: // wrapper + element
      case 40: // wrapper + elements
         break;
      default:
         throw new RuntimeException("Invalid annotations of " + annotated + ", magic=" + magic);
      }
   }

   private void registerType(Type type, Set<Class<?>> done) {
      if (type instanceof Class) {
         Class<?> clazz = (Class<?>) type;

         if (!m_elementEntryMap.containsKey(clazz) && m_filter.matches(clazz)) {
            register(clazz, done);
         }
      } else if (type instanceof ParameterizedType) {
         ParameterizedType ptype = (ParameterizedType) type;
         Type[] actualTypes = ptype.getActualTypeArguments();

         for (Type actualType : actualTypes) {
            registerType(actualType, done);
         }
      } else {
         System.err.println("Unknown type: " + type);
      }
   }

   private void processFields(ElementEntry entry, Class<?> clazz, Set<Class<?>> done) {
      for (Field field : clazz.getDeclaredFields()) {
         Type valueType = field.getGenericType();

         processAnnotatedElement(entry, field, valueType, done);
      }
   }

   private void processMethods(ElementEntry entry, Class<?> clazz, Set<Class<?>> done) {
      for (Method method : clazz.getDeclaredMethods()) {
         Type[] types = method.getGenericParameterTypes();
         Type valueType = types.length == 0 ? method.getGenericReturnType() : types[0];

         processAnnotatedElement(entry, method, valueType, done);
      }
   }

   private void processClass(ElementEntry entry, Class<?> clazz) {
      XmlRootElement re = clazz.getAnnotation(XmlRootElement.class);
      XmlElement e = clazz.getAnnotation(XmlElement.class);

      if (re != null) {
         m_rootElementMap.put(re.name(), clazz);
         entry.setName(re.name());
      }

      if (e != null) {
         entry.setName(e.name());
         entry.setRequired(e.required());
      }

      entry.setType(clazz);
   }

   public synchronized void register(Class<?> clazz) {
      register(clazz, new HashSet<Class<?>>());
   }

   private synchronized void register(Class<?> clazz, Set<Class<?>> done) {
      if (done.contains(clazz) || m_elementEntryMap.containsKey(clazz) || m_filter != null && !m_filter.matches(clazz)) {
         return;
      }

      ElementEntry entry = new ElementEntry();

      done.add(clazz);
      
      processClass(entry, clazz);
      processFields(entry, clazz, done);
      processMethods(entry, clazz, done);

      entry.validate();

      m_elementEntryMap.put(clazz, entry);
   }
}
