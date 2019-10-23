package com.kms.katalon.core.context.internal;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;

import com.kms.katalon.core.annotation.AfterTestCase;
import com.kms.katalon.core.annotation.AfterTestSuite;
import com.kms.katalon.core.annotation.BeforeTestCase;
import com.kms.katalon.core.annotation.BeforeTestDataBindToTestCase;
import com.kms.katalon.core.annotation.BeforeTestSuite;
import com.kms.katalon.core.constants.CoreMessageConstants;
import com.kms.katalon.core.logging.ErrorCollector;
import com.kms.katalon.core.logging.KeywordLogger;
import com.kms.katalon.core.logging.KeywordLogger.KeywordStackElement;
import com.kms.katalon.core.util.internal.ExceptionsUtil;
import com.kms.katalon.core.util.internal.PrimitiesUtil;

import groovy.lang.GroovyObject;

public class TestHooker {
    
    private final KeywordLogger logger = KeywordLogger.getInstance(this.getClass());

    private Map<String, List<MethodNode>> testContextMethods;

    private GroovyObject testContextClassInstance;

    private Class<?> scriptClazz;

    public TestHooker(Class<?> scriptClazz) {
        this.scriptClazz = scriptClazz;
        collectContextMethods();
    }

    private void clearContextMethods() {
        testContextMethods = new HashMap<>();
        testContextMethods.put(BeforeTestCase.class.getName(), new ArrayList<>());
        testContextMethods.put(AfterTestCase.class.getName(), new ArrayList<>());
        testContextMethods.put(BeforeTestSuite.class.getName(), new ArrayList<>());
        testContextMethods.put(AfterTestSuite.class.getName(), new ArrayList<>());
        testContextMethods.put(BeforeTestDataBindToTestCase.class.getName(), new ArrayList<>());
    }

    public void collectContextMethods() {
        clearContextMethods();

        try {
            ClassNode classNode = new ClassNode(scriptClazz);
            classNode.getAllDeclaredMethods().stream().forEach(method -> {
                evaluateTestContextMethod(method);
            });
        } catch (MultipleCompilationErrorsException e) {
            System.out.println(ExceptionsUtil.getMessageForThrowable(e));
        }
    }

    private void evaluateTestContextMethod(MethodNode method) {
        method.getAnnotations().forEach(annotationNode -> {
            testContextMethods.keySet().forEach(testContextAnnotation -> {
                List<MethodNode> annotationContextMethods = testContextMethods.get(testContextAnnotation);
                if (annotationContextMethods == null) {
                    annotationContextMethods = new ArrayList<>();
                }
                if (testContextAnnotation.contains(annotationNode.getClassNode().getName())
                        && !annotationContextMethods.contains(method)) {
                    annotationContextMethods.add(method);
                }
                testContextMethods.put(testContextAnnotation, annotationContextMethods);
            });
        });
    }

    public void invokeContextMethods(String listenerAnnotationName, Object[] injectedObjects) {
        if (testContextClassInstance == null) {
            try {
                testContextClassInstance = (GroovyObject) scriptClazz.newInstance();
            } catch (InstantiationException | IllegalAccessException | CompilationFailedException e) {
                System.err.println(ExceptionsUtil.getMessageForThrowable(e));
            }
        }
        testContextMethods.get(listenerAnnotationName).forEach(method -> {
            invokeMethod(listenerAnnotationName, method, injectedObjects);
        });
    }

    private void invokeMethod(String listenerAnnotationName, MethodNode method, Object[] injectedObjects) {
        String methodName = method.getName();
        ErrorCollector errorCollector = ErrorCollector.getCollector();
        List<Throwable> oldErrors = errorCollector.getCoppiedErrors();
        Stack<KeywordStackElement> keywordStack = new Stack<>();
        try {
            errorCollector.clearErrors();

            String methodDisplayName = scriptClazz.getName() + "." + methodName + "(...)";
            logger.startListenerKeyword(methodName, null, keywordStack);
            logger.logDebug(MessageFormat.format(CoreMessageConstants.EXEC_LOG_STARTING_INVOKE_LISTENER_METHOD,
                    listenerAnnotationName, methodDisplayName));
            testContextClassInstance.invokeMethod(methodName, getValueForParemeters(method, injectedObjects));
            logger.logDebug(MessageFormat.format(CoreMessageConstants.EXEC_LOG_INVOKE_LISTENER_METHOD_COMPLETED,
                    listenerAnnotationName, methodDisplayName));
        } catch (Throwable e) {
            logger.logError(ExceptionsUtil.getStackTraceForThrowable(e), null, e);
        } finally {
            while (!keywordStack.isEmpty()) {
                KeywordStackElement keywordStackElement = keywordStack.pop();
                logger.endKeyword(keywordStackElement.getKeywordName(), null, keywordStackElement.getNestedLevel());
            }
            logger.endListenerKeyword(methodName, null, keywordStack);
            errorCollector.clearErrors();
            errorCollector.getErrors().addAll(oldErrors);
        }
    }

    private Object[] getValueForParemeters(MethodNode method, Object[] injectedObjects) {
        if (injectedObjects == null) {
            return null;
        }
        return Arrays.stream(method.getParameters()).map(param -> findInjectedObject(param, injectedObjects)).toArray();
    }

    private Object findInjectedObject(Parameter param, Object[] injectedObjects) {
        Optional<Object> suitableOpt = Arrays.stream(injectedObjects).filter(o -> isSameClass(o, param)).findFirst();
        if (suitableOpt.isPresent()) {
            return suitableOpt.get();
        }
        Class<?> clazz = getParamClass(param);
        return clazz != null ? PrimitiesUtil.defaultValue(clazz) : null;
    }

    private Class<?> getParamClass(Parameter param) {
        try {
            String paramClassName = ClassHelper.getWrapper(param.getType()).getName();
            return Class.forName(paramClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isSameClass(Object o, Parameter param) {
        if (o == null) {
            return false;
        }
        Class<?> clazz = getParamClass(param);
        if (clazz != null) {
            return !clazz.equals(Object.class) && clazz.isInstance(o);
        }
        return false;
    }
}
