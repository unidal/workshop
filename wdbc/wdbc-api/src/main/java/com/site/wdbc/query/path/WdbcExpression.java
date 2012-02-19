package com.site.wdbc.query.path;

import java.util.Map;

public interface WdbcExpression {
   public boolean matches(int value, Map<String, String> attributes);
}
