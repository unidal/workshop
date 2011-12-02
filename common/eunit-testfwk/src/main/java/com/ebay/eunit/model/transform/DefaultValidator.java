package com.ebay.eunit.model.transform;

import static com.ebay.eunit.model.Constants.ATTR_NAME;
import static com.ebay.eunit.model.Constants.ATTR_TYPE;
import static com.ebay.eunit.model.Constants.ENTITY_EUNIT_CLASS;
import static com.ebay.eunit.model.Constants.ENTITY_EUNIT_EXCEPTION;
import static com.ebay.eunit.model.Constants.ENTITY_EUNIT_FIELD;
import static com.ebay.eunit.model.Constants.ENTITY_EUNIT_METHOD;
import static com.ebay.eunit.model.Constants.ENTITY_FIELDS;
import static com.ebay.eunit.model.Constants.ENTITY_METHODS;
import static com.ebay.eunit.model.Constants.ENTITY_PARAMETERS;

import java.util.Stack;

import com.ebay.eunit.model.IVisitor;
import com.ebay.eunit.model.entity.EunitClass;
import com.ebay.eunit.model.entity.EunitException;
import com.ebay.eunit.model.entity.EunitField;
import com.ebay.eunit.model.entity.EunitMethod;
import com.ebay.eunit.model.entity.EunitParameter;

public class DefaultValidator implements IVisitor {

   private Path m_path = new Path();
   
   protected void assertRequired(String name, Object value) {
      if (value == null) {
         throw new RuntimeException(String.format("%s at path(%s) is required!", name, m_path));
      }
   }

   @Override
   public void visitEunitClass(EunitClass eunitClass) {
      m_path.down(ENTITY_EUNIT_CLASS);

      visitEunitClassChildren(eunitClass);

      m_path.up(ENTITY_EUNIT_CLASS);
   }

   protected void visitEunitClassChildren(EunitClass eunitClass) {
      m_path.down(ENTITY_FIELDS);

      for (EunitField eunitField : eunitClass.getFields()) {
         visitEunitField(eunitField);
      }

      m_path.up(ENTITY_FIELDS);

      m_path.down(ENTITY_METHODS);

      for (EunitMethod eunitMethod : eunitClass.getMethods()) {
         visitEunitMethod(eunitMethod);
      }

      m_path.up(ENTITY_METHODS);
   }

   @Override
   public void visitEunitException(EunitException eunitException) {
      m_path.down(ENTITY_EUNIT_EXCEPTION);

      assertRequired(ATTR_TYPE, eunitException.getType());

      m_path.up(ENTITY_EUNIT_EXCEPTION);
   }

   @Override
   public void visitEunitField(EunitField eunitField) {
      m_path.down(ENTITY_EUNIT_FIELD);

      assertRequired(ATTR_NAME, eunitField.getName());

      m_path.up(ENTITY_EUNIT_FIELD);
   }

   @Override
   public void visitEunitMethod(EunitMethod eunitMethod) {
      m_path.down(ENTITY_EUNIT_METHOD);

      assertRequired(ATTR_NAME, eunitMethod.getName());

      visitEunitMethodChildren(eunitMethod);

      m_path.up(ENTITY_EUNIT_METHOD);
   }

   protected void visitEunitMethodChildren(EunitMethod eunitMethod) {
      m_path.down(ENTITY_PARAMETERS);

      for (EunitParameter eunitParameter : eunitMethod.getParameters()) {
         visitEunitParameter(eunitParameter);
      }

      m_path.up(ENTITY_PARAMETERS);

      for (EunitException eunitException : eunitMethod.getExpectedExceptions()) {
         visitEunitException(eunitException);
      }
   }

   @Override
   public void visitEunitParameter(EunitParameter eunitParameter) {
   }

   static class Path {
      private Stack<String> m_sections = new Stack<String>();

      public Path down(String nextSection) {
         m_sections.push(nextSection);

         return this;
      }

      @Override
      public String toString() {
         StringBuilder sb = new StringBuilder();

         for (String section : m_sections) {
            sb.append('/').append(section);
         }

         return sb.toString();
      }

      public Path up(String currentSection) {
         if (m_sections.isEmpty() || !m_sections.peek().equals(currentSection)) {
            throw new RuntimeException("INTERNAL ERROR: stack mismatched!");
         }

         m_sections.pop();
         return this;
      }
   }
}
