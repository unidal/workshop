package com.site.app.rule;

import java.util.List;

import com.site.app.DataProvider;
import com.site.app.FieldId;
import com.site.app.error.ErrorObject;
import com.site.app.rule.facet.RuleFacet;

public interface Rule<S extends FieldId, T> {
   public FieldId getFieldId();

   public T getDefaultValue();
   
   public T evaluate(DataProvider<S> dataProvider, List<ErrorObject> errors);

   public Rule<S, T> facet(RuleFacet<T> facet);
}
