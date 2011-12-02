package com.site.app.rule.facet;

public enum Inclusion {
   NONE(false, false),

   MIN(true, false),

   MAX(false, true),

   BOTH(true, true);

   private boolean m_includeMin;
   private boolean m_includeMax;

   Inclusion(boolean includeMin, boolean includeMax) {
      m_includeMin = includeMin;
      m_includeMax = includeMax;
   }

   public boolean isIncludeMin() {
      return m_includeMin;
   }

   public boolean isIncludeMax() {
      return m_includeMax;
   }
}
