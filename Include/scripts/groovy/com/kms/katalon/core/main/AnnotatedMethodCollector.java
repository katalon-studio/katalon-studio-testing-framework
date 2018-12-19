package com.kms.katalon.core.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.builder.AstBuilder;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.control.CompilePhase;

import com.kms.katalon.core.exception.KatalonRuntimeException;

/**
 * Provides the way to collect all {@link MethodNode} that each node was annotated with {@link AnnotationNode} of a
 * script.
 */
public class AnnotatedMethodCollector {

    private static final String FIELD_SKIPPED = "skipped";

    private ClassNode classNode;

    private Set<String> annotatedMethodNames;

    private Map<String, List<MethodNode>> methodCollector;

    /**
     * Collects all {@link MethodNode} that each name is specified in the <code>annotatedMethodNames</code>
     * of a script file.
     * 
     * @param annotatedMethodNames annotation class name. Eg: SetUp, TearDown...
     */
    public AnnotatedMethodCollector(Set<String> annotatedMethodNames) {
        this.annotatedMethodNames = annotatedMethodNames;
    }

    private ClassNode getMainClassNode(List<ASTNode> astNodes) {
        for (ASTNode childNode : astNodes) {
            if (childNode instanceof ClassNode && ((ClassNode) childNode).isScript()) {
                return (ClassNode) childNode;
            }
        }
        return null;
    }

    private List<MethodNode> collectMethodWithAnnotation(Class<?> annotationClass) {
        List<MethodNode> methodList = classNode.getMethods();

        if (methodList == null) {
            return Collections.emptyList();
        }

        List<MethodNode> annotatedMethods = new ArrayList<>();
        methodList.stream().forEach(method -> {
            method.getAnnotations().stream().forEach(annotationNode -> {
                if (isAnnotationNodeName(annotationNode, annotationClass) && isAnnotationNotSkipped(annotationNode)) {
                    annotatedMethods.add(method);
                }
            });
        });
        return annotatedMethods;
    }

    public boolean isAnnotationNodeName(AnnotationNode annotationNode, Class<?> annotationClass) {
        String annotationNodeName = annotationNode.getClassNode().getName();
        return annotationNodeName.equals(annotationClass.getName())
                || annotationNodeName.equals(annotationClass.getSimpleName());
    }

    public boolean isAnnotationNotSkipped(AnnotationNode annotationNode) {
        Map<String, Expression> fieldMembers = annotationNode.getMembers();
        if (!fieldMembers.containsKey(FIELD_SKIPPED)) {
            return true;
        }
        Expression skippedExprs = fieldMembers.get(FIELD_SKIPPED);
        if (skippedExprs instanceof ConstantExpression) {
            Object value = ((ConstantExpression) skippedExprs).getValue();
            if (value instanceof Boolean && (boolean) value) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return a collection of {@link MethodNode} that is grouped annotation class name.
     * 
     * @throws IOException thrown when file not found or cannot be read.
     */
    public Map<String, List<MethodNode>> getMethodNodes(String scriptContent) throws IOException {
        if (methodCollector != null) {
            return methodCollector;
        }

        methodCollector = new HashMap<>();

        classNode = getMainClassNode(new AstBuilder().buildFromString(CompilePhase.CONVERSION, false, scriptContent));
        if (classNode == null) {
            return methodCollector;
        }

        annotatedMethodNames.stream().forEach(methodName -> {
            try {
                Class<?> clazz = Class.forName(methodName);
                methodCollector.put(methodName, collectMethodWithAnnotation(clazz));
            } catch (ClassNotFoundException e) {
                throw new KatalonRuntimeException(e);
            }
        });

        return methodCollector;
    }
}
