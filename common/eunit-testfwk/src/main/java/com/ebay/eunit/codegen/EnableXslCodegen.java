package com.ebay.eunit.codegen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ebay.eunit.annotation.ServiceProvider;
import com.ebay.eunit.codegen.handler.EnableXslCodegenHandler;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ServiceProvider(EnableXslCodegenHandler.class)
public @interface EnableXslCodegen {
   
}
