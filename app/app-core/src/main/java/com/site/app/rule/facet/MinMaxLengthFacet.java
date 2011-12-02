package com.site.app.rule.facet;

import com.site.app.error.FacetErrorId;


public class MinMaxLengthFacet<T> implements RuleFacet<T> {
   private int m_minLength;
   private int m_maxLength;
   private Inclusion m_inclusion;

   public MinMaxLengthFacet(int minLength, int maxLength) {
      this(minLength, maxLength, Inclusion.BOTH);
   }

   public MinMaxLengthFacet(int minLength, int maxLength, Inclusion inclusion) {
      m_minLength = minLength;
      m_maxLength = maxLength;
      m_inclusion = inclusion;
   }

   public boolean isValid(T value) {
      boolean valid = false;

      if (value instanceof String) {
         int length = ((String) value).length();

         if (length >= m_minLength) {
            valid = (length > m_minLength) || m_inclusion.isIncludeMin();
         }

         if (length <= m_maxLength) {
            valid = (length < m_maxLength) || m_inclusion.isIncludeMax();
         }
      }

      return valid;
   }

   public FacetErrorId getErrorId() {
      return FacetErrorId.INVALID_DATA_LENGTH;
   }
}
