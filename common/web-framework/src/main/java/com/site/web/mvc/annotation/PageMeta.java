package com.site.web.mvc.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.site.web.lifecycle.ActionResolver;
import com.site.web.lifecycle.DefaultActionResolver;

@Retention(RUNTIME)
@Target(TYPE)
public @interface PageMeta {
   String name();
   
   String module();

   String defaultInboundAction() default "";

   String defaultTransition() default "";

   String defaultErrorAction() default "";

   Class<? extends ActionResolver> actionResolver() default DefaultActionResolver.class;
}
