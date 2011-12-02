package com.site.web.lifecycle;

import com.site.web.mvc.payload.ParameterProvider;

public interface ActionResolver {
   public UrlMapping parseUrl(ParameterProvider provider);

   public String buildUrl(ParameterProvider provider, UrlMapping mapping);
}
