package com.site.lab.performance.memory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MemoryMeta {
   int loops() default 100000;

   int warmups() default 10;
   
   boolean gcinfo() default false;
}
