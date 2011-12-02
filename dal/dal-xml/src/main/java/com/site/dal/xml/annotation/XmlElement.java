package com.site.dal.xml.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target( { TYPE, FIELD, METHOD })
public @interface XmlElement {
   String defaultValue() default "\u0000";

   String name();

   boolean required() default false;

   Class<?> type() default DEFAULT.class;

   String format() default "";

   static final class DEFAULT {
   }
}
