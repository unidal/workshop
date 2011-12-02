package com.site.web.mvc.payload.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.site.web.mvc.Action;
import com.site.web.mvc.Page;
import com.site.web.mvc.PayloadProvider;

@Retention(RUNTIME)
@Target(TYPE)
public @interface PayloadProviderMeta {
   Class<? extends PayloadProvider<? extends Page, ? extends Action>> value();
}
