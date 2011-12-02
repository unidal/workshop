package com.ebay.eunit.model.transform;

import static com.ebay.eunit.model.Constants.ATTR_BEFORE_AFTER;
import static com.ebay.eunit.model.Constants.ATTR_ID;
import static com.ebay.eunit.model.Constants.ATTR_IGNORED;
import static com.ebay.eunit.model.Constants.ATTR_MESSAGE;
import static com.ebay.eunit.model.Constants.ATTR_NAME;
import static com.ebay.eunit.model.Constants.ATTR_PATTERN;
import static com.ebay.eunit.model.Constants.ATTR_RETURN_TYPE;
import static com.ebay.eunit.model.Constants.ATTR_STATIC;
import static com.ebay.eunit.model.Constants.ATTR_TEST;
import static com.ebay.eunit.model.Constants.ATTR_TIMEOUT;
import static com.ebay.eunit.model.Constants.ATTR_TYPE;
import static com.ebay.eunit.model.Constants.ELEMENT_GROUP;
import static com.ebay.eunit.model.Constants.ELEMENT_GROUPS;
import static com.ebay.eunit.model.Constants.ENTITY_EUNIT_CLASS;
import static com.ebay.eunit.model.Constants.ENTITY_EUNIT_EXCEPTION;
import static com.ebay.eunit.model.Constants.ENTITY_EUNIT_FIELD;
import static com.ebay.eunit.model.Constants.ENTITY_EUNIT_METHOD;
import static com.ebay.eunit.model.Constants.ENTITY_EUNIT_PARAMETER;
import static com.ebay.eunit.model.Constants.ENTITY_FIELDS;
import static com.ebay.eunit.model.Constants.ENTITY_METHODS;
import static com.ebay.eunit.model.Constants.ENTITY_PARAMETERS;

import com.ebay.eunit.model.IVisitor;
import com.ebay.eunit.model.entity.EunitClass;
import com.ebay.eunit.model.entity.EunitException;
import com.ebay.eunit.model.entity.EunitField;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.model.entity.EunitParameter;

public class DefaultXmlBuilder implements IVisitor {

   private int m_level;

   private StringBuilder m_sb = new StringBuilder(2048);

   private void endTag(String name) {
      m_level--;

      indent();
      m_sb.append("</").append(name).append(">\r\n");
   }

   public String getString() {
      return m_sb.toString();
   }

   private void indent() {
      for (int i = m_level - 1; i >= 0; i--) {
         m_sb.append("   ");
      }
   }

   private void startTag(String name, boolean closed, Object... nameValues) {
      startTag(name, null, closed, nameValues);
   }

   private void startTag(String name, Object... nameValues) {
      startTag(name, false, nameValues);
   }
   
   private void startTag(String name, Object text, boolean closed, Object... nameValues) {
      indent();

      m_sb.append('<').append(name);

      int len = nameValues.length;

      for (int i = 0; i + 1 < len; i += 2) {
         Object attrName = nameValues[i];
         Object attrValue = nameValues[i + 1];

         if (attrValue != null) {
            m_sb.append(' ').append(attrName).append("=\"").append(attrValue).append('"');
         }
      }

      if (text != null && closed) {
         m_sb.append('>');
         m_sb.append(text == null ? "" : text);
         m_sb.append("</").append(name).append(">\r\n");
      } else {
         if (closed) {
            m_sb.append('/');
         } else {
            m_level++;
         }
   
         m_sb.append(">\r\n");
      }
   }

   private void tagWithText(String name, String text, Object... nameValues) {
      if (text == null) {
         return;
      }
      
      indent();

      m_sb.append('<').append(name);

      int len = nameValues.length;

      for (int i = 0; i + 1 < len; i += 2) {
         Object attrName = nameValues[i];
         Object attrValue = nameValues[i + 1];

         if (attrValue != null) {
            m_sb.append(' ').append(attrName).append("=\"").append(attrValue).append('"');
         }
      }

      m_sb.append(">");
      m_sb.append(text);
      m_sb.append("</").append(name).append(">\r\n");
   }

   @Override
   public void visitEunitClass(EunitClass eunitClass) {
      startTag(ENTITY_EUNIT_CLASS, ATTR_TYPE, eunitClass.getType() == null ? null : eunitClass.getType().getName(), ATTR_IGNORED, eunitClass.getIgnored());

      if (!eunitClass.getGroups().isEmpty()) {
         startTag(ELEMENT_GROUPS);

         for (String group : eunitClass.getGroups()) {
            tagWithText(ELEMENT_GROUP, group);
         }

         endTag(ELEMENT_GROUPS);
      }

      if (!eunitClass.getFields().isEmpty()) {
         startTag(ENTITY_FIELDS);

         for (EunitField field : eunitClass.getFields()) {
            visitEunitField(field);
         }

         endTag(ENTITY_FIELDS);
      }

      if (!eunitClass.getMethods().isEmpty()) {
         startTag(ENTITY_METHODS);

         for (EunitMethod method : eunitClass.getMethods()) {
            visitEunitMethod(method);
         }

         endTag(ENTITY_METHODS);
      }

      endTag(ENTITY_EUNIT_CLASS);
   }

   @Override
   public void visitEunitException(EunitException eunitException) {
      startTag(ENTITY_EUNIT_EXCEPTION, true, ATTR_TYPE, eunitException.getType() == null ? null : eunitException.getType().getName(), ATTR_MESSAGE, eunitException.getMessage(), ATTR_PATTERN, eunitException.getPattern());
   }

   @Override
   public void visitEunitField(EunitField eunitField) {
      startTag(ENTITY_EUNIT_FIELD, true, ATTR_NAME, eunitField.getName(), ATTR_TYPE, eunitField.getType() == null ? null : eunitField.getType().getName());
   }

   @Override
   public void visitEunitMethod(EunitMethod eunitMethod) {
      startTag(ENTITY_EUNIT_METHOD, ATTR_NAME, eunitMethod.getName(), ATTR_TEST, eunitMethod.getTest(), ATTR_TIMEOUT, eunitMethod.getTimeout(), ATTR_IGNORED, eunitMethod.getIgnored(), ATTR_BEFORE_AFTER, eunitMethod.getBeforeAfter(), ATTR_STATIC, eunitMethod.getStatic(), ATTR_RETURN_TYPE, eunitMethod.getReturnType() == null ? null : eunitMethod.getReturnType().getName());

      if (!eunitMethod.getGroups().isEmpty()) {
         startTag(ELEMENT_GROUPS);

         for (String group : eunitMethod.getGroups()) {
            tagWithText(ELEMENT_GROUP, group);
         }

         endTag(ELEMENT_GROUPS);
      }

      if (!eunitMethod.getParameters().isEmpty()) {
         startTag(ENTITY_PARAMETERS);

         for (EunitParameter parameter : eunitMethod.getParameters()) {
            visitEunitParameter(parameter);
         }

         endTag(ENTITY_PARAMETERS);
      }

      if (!eunitMethod.getExpectedExceptions().isEmpty()) {
         for (EunitException expectedException : eunitMethod.getExpectedExceptions()) {
            visitEunitException(expectedException);
         }
      }

      endTag(ENTITY_EUNIT_METHOD);
   }

   @Override
   public void visitEunitParameter(EunitParameter eunitParameter) {
      startTag(ENTITY_EUNIT_PARAMETER, true, ATTR_TYPE, eunitParameter.getType() == null ? null : eunitParameter.getType().getName(), ATTR_ID, eunitParameter.getId());
   }
}
