package com.site.web.mvc;

public interface Normalizer<T extends ActionContext<?>> {
	public void normalize(T context);
}
