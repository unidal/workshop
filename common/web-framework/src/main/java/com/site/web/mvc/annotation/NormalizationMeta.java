package com.site.web.mvc.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.site.web.mvc.Normalizer;

@Retention(RUNTIME)
@Target(METHOD)
public @interface NormalizationMeta {
   Class<? extends Normalizer<?>> value();

}
