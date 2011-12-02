package com.site.wdbc.query.path;

import java.util.List;

public interface WdbcPathPattern {
   public int NOT_MATCHED = 0;

   public int FULL_MATCHED = 1;

   public int FULL_WILD_MATCHED = 2;

   public int PARTIAL_WILD_MATCHED = 3;

   public char PATTERN_ATTRIBUTE_STARTING = '@';

   public String PATTERN_COMMENT = "#comment";

   public String PATTERN_TEXT = "#text";

   public String PATTERN_ALL_TEXT = "*text";

   public String PATTERN_ALL_NODE = "*";

   public List<String> getSectionNames();

   public List<WdbcExpression> getSectionExpressions();
}
