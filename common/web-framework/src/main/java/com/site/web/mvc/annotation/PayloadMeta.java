package com.site.web.mvc.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.site.web.mvc.Action;
import com.site.web.mvc.ActionPayload;
import com.site.web.mvc.Page;

@Retention(RUNTIME)
@Target(METHOD)
public @interface PayloadMeta {
   Class<? extends ActionPayload<? extends Page, ? extends Action>> value();
}
