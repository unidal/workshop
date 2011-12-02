package com.site.app.rule;

import java.util.ArrayList;
import java.util.List;

import com.site.app.DataProvider;
import com.site.app.FieldId;
import com.site.app.error.DefaultErrorObject;
import com.site.app.error.ErrorId;
import com.site.app.error.ErrorObject;
import com.site.app.error.ErrorSeverity;
import com.site.app.error.RuleErrorId;
import com.site.app.rule.facet.RuleFacet;

public abstract class BaseRule<S extends FieldId, T> implements Rule<S, T> {
   private S m_fieldId;

   private T m_defaultValue;

   private boolean m_hasDefaultValue;

   private List<RuleFacet<T>> m_facets = new ArrayList<RuleFacet<T>>();

   public BaseRule(S fieldId) {
      m_fieldId = fieldId;
   }

   public BaseRule(S fieldId, T defaultValue) {
      m_fieldId = fieldId;
      m_defaultValue = defaultValue;
      m_hasDefaultValue = true;
   }

   public BaseRule<S, T> facet(RuleFacet<T> facet) {
      m_facets.add(facet);
      return this;
   }

   public T getDefaultValue() {
      return m_defaultValue;
   }

   public S getFieldId() {
      return m_fieldId;
   }

   public final T evaluate(DataProvider<S> dataProvider, List<ErrorObject> errors) {
      if (dataProvider == null) {
         throw new RuntimeException("No DataProvider specified");
      }

      Object rawValue = dataProvider.getValue(getFieldId());
      T value = null;

      if (rawValue == null || "".equals(rawValue)) {
         if (m_hasDefaultValue) {
            return getDefaultValue();
         } else {
            ErrorObject error = createError(RuleErrorId.REQUIRED);

            errors.add(error);
         }
      } else {
         try {
            value = convert(rawValue);

            boolean valid = true;

            for (RuleFacet<T> facet : m_facets) {
               if (!facet.isValid(value)) {
                  ErrorObject error = createError(facet.getErrorId());

                  errors.add(error);
               }
            }

            // reset value if it's not passed facet checks
            if (!valid) {
               value = null;
            }
         } catch (RuntimeException e) {
            ErrorObject error = createError(RuleErrorId.INVALID_FORMAT);

            value = null;
            error.setException(e);
            errors.add(error);
         }
      }

      return value;
   }

   protected ErrorObject createError(ErrorId errorId) {
      ErrorObject error = new DefaultErrorObject();

      error.setErrorId(errorId);
      error.setErrorSeverity(ErrorSeverity.ERROR);
      error.setFieldId(getFieldId());

      return error;
   }

   protected abstract T convert(Object value);
}
