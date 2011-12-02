package com.site.app.rule.facet;

import com.site.app.error.FacetErrorId;



public class MinMaxValueFacet<T extends Comparable<T>> implements RuleFacet<T> {
   private T m_minValue;
   private T m_maxValue;
   private Inclusion m_inclusion;

   public MinMaxValueFacet(T minValue, T maxValue) {
      this(minValue, maxValue, Inclusion.BOTH);
   }

   public MinMaxValueFacet(T minValue, T maxValue, Inclusion inclusion) {
      m_minValue = minValue;
      m_maxValue = maxValue;
      m_inclusion = inclusion;
   }

   public boolean isValid(T value) {
      boolean valid = false;

      if (value != null) {
         if (m_minValue != null) {
            int sign = value.compareTo(m_minValue);

            if (sign >= 0) {
               valid = (sign > 0 || m_inclusion.isIncludeMin());
            }
         }

         if (m_maxValue != null) {
            int sign = value.compareTo(m_maxValue);

            if (sign <= 0) {
               valid = (sign < 0 || m_inclusion.isIncludeMax());
            }
         }
      }

      return valid;
   }

   public FacetErrorId getErrorId() {
      return FacetErrorId.INVALID_DATA_VALUE;
   }
}
