package com.site.app.rule.facet;

import java.io.UnsupportedEncodingException;

import com.site.app.error.FacetErrorId;

public class MinMaxUtf8LengthFacet<T> implements RuleFacet<T> {
   private int m_minLength;
   private int m_maxLength;
   private Inclusion m_inclusion;

   public MinMaxUtf8LengthFacet(int minLength, int maxLength) {
      this(minLength, maxLength, Inclusion.BOTH);
   }

   public MinMaxUtf8LengthFacet(int minLength, int maxLength, Inclusion inclusion) {
      m_minLength = minLength;
      m_maxLength = maxLength;
      m_inclusion = inclusion;
   }

   public boolean isValid(T value) {
      boolean valid = false;

      if (value instanceof String) {
         try {
            int length = ((String) value).getBytes("utf-8").length;

            if (length >= m_minLength) {
               valid = (length > m_minLength) || m_inclusion.isIncludeMin();
            }

            if (length <= m_maxLength) {
               valid = (length < m_maxLength) || m_inclusion.isIncludeMax();
            }
         } catch (UnsupportedEncodingException e) {
            // ignore it
            e.printStackTrace();
         }
      }

      return valid;
   }

   public FacetErrorId getErrorId() {
      return FacetErrorId.INVALID_DATA_LENGTH;
   }
}
