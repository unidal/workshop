package org.unidal.ezsell.api.paypal;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface PaypalFieldMeta {

   String value();

   boolean property() default true;

   String format() default "";

   boolean required() default true;
}
