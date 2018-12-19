package com.kms.katalon.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * Annotation for methods that will run after a test case is error
 *
 ***/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TearDownIfError {
	String description() default "";
	boolean skipped() default false;
}
