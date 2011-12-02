package com.site.kernel.dal;

import com.site.kernel.common.BaseEnum;

public class Cardinality extends BaseEnum {
   public static final Cardinality ZERO_TO_ONE = new Cardinality(1, "?", false);

   public static final Cardinality EXACT_ONE = new Cardinality(2, "1", false);

   public static final Cardinality ZERO_TO_MANY = new Cardinality(3, "*", true);

   public static final Cardinality ONE_TO_MANY = new Cardinality(4, "+", true);

   private boolean m_many;

   protected Cardinality(int id, String name, boolean many) {
      super(id, name);

      m_many = many;
   }

   public boolean isSingle() {
      return !m_many;
   }

   public boolean isMany() {
      return m_many;
   }
}
