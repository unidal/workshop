package com.site.web.mvc;

import com.site.web.mvc.annotation.ErrorActionMeta;
import com.site.web.mvc.annotation.TransitionMeta;

public abstract class AbstractModule {
	@TransitionMeta(name = "default")
	public void handleTransition(ActionContext<?> ctx) {
		// simple cases, nothing here
	}

	@ErrorActionMeta(name = "default")
	public void onError(ActionContext<?> ctx) {
		ctx.getException().printStackTrace();
	}
}