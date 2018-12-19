package com.kms.katalon.core.ast;

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

import org.codehaus.groovy.transform.GroovyASTTransformationClass

@Retention (RetentionPolicy.SOURCE)
@Target ([ElementType.TYPE])
@GroovyASTTransformationClass ("com.kms.katalon.core.ast.AstTestStepTransformation")
public @interface RequireAstTestStepTransformation {
}
