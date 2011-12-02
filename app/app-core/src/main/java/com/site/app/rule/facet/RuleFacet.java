package com.site.app.rule.facet;

import com.site.app.error.ErrorId;

public interface RuleFacet<T> {
   public boolean isValid(T value);

   public ErrorId getErrorId();
}
