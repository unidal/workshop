package com.site.web.mvc;

import java.util.List;

import com.site.web.lifecycle.UrlMapping;
import com.site.web.mvc.payload.ParameterProvider;

public interface PayloadProvider<S extends Page, T extends Action> {
	public void register(Class<? extends ActionPayload<S, T>> payloadClass);

	public List<ErrorObject> process(UrlMapping mapping, ParameterProvider parameterProvider, ActionPayload<S, T> payload);
}
