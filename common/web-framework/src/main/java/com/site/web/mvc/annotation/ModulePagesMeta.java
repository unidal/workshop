package com.site.web.mvc.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.site.web.mvc.PageHandler;

@Retention(RUNTIME)
@Target(TYPE)
public @interface ModulePagesMeta {
	Class<? extends PageHandler<?>>[] value();
}
