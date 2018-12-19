package com.kms.katalon.core.main;

import static com.kms.katalon.core.constants.StringConstants.DF_CHARSET;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ImportNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.builder.AstBuilder;
import org.codehaus.groovy.control.CompilePhase;

import com.kms.katalon.core.annotation.SetUp;
import com.kms.katalon.core.annotation.TearDown;
import com.kms.katalon.core.annotation.TearDownIfError;
import com.kms.katalon.core.annotation.TearDownIfFailed;
import com.kms.katalon.core.annotation.TearDownIfPassed;
import com.kms.katalon.core.ast.GroovyParser;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.testcase.TestCase;

public class TestCaseMethodNodeCollector {

    private Map<String, TestCaseMethodNodeWrapper> annotationNodeCollection;

    private ClassNode classNode;

    private String importString;

    TestCaseMethodNodeCollector(TestCase testCase) throws IOException {
        classNode = getMainClassNode(new AstBuilder().buildFromString(CompilePhase.CONVERSION, false,
                FileUtils.readFileToString(new File(testCase.getGroovyScriptPath()), DF_CHARSET)));
        if (classNode == null) {
            throw new IllegalArgumentException("No main method for test case: " + testCase.getTestCaseId());
        }

        importString = getImportString(classNode);

        annotationNodeCollection = collectSetupAndTeardownMethods();
    }

    String getImportString() {
        return importString;
    }

    ClassNode getClassNode() {
        return classNode;
    }

    TestCaseMethodNodeWrapper getMethodNodeWrapper(Class<?> annotationClass) {
        return annotationNodeCollection.get(annotationClass.getName());
    }

    private ClassNode getMainClassNode(List<ASTNode> astNodes) {
        for (ASTNode childNode : astNodes) {
            if (childNode instanceof ClassNode && ((ClassNode) childNode).isScript()) {
                return (ClassNode) childNode;
            }
        }
        return null;
    }

    private String getImportString(ClassNode classNode) {
        ModuleNode moduleNode = null;
        if (classNode == null || (moduleNode = classNode.getModule()) == null) {
            return StringUtils.EMPTY;
        }

        StringBuilder importString = new StringBuilder();
        GroovyParser groovyParser = new GroovyParser(importString);
        for (ImportNode importNode : moduleNode.getImports()) {
            groovyParser.parse(importNode);
        }
        return importString.toString();
    }

    private List<MethodNode> collectMethodWithAnnotation(Class<?> annotationClass) {
        List<MethodNode> methodList = classNode.getMethods();

        if (methodList == null) {
            return Collections.emptyList();
        }

        List<MethodNode> annotatedMethods = new ArrayList<MethodNode>();
        for (MethodNode method : methodList) {
            for (AnnotationNode annotationNode : method.getAnnotations()) {
                String annotationNodeName = annotationNode.getClassNode().getName();
                if (annotationNodeName.equals(annotationClass.getName())
                        || annotationNodeName.equals(annotationClass.getSimpleName())) {
                    annotatedMethods.add(method);
                }
            }
        }
        return annotatedMethods;
    }

    private TestCaseMethodNodeWrapper getWrapper(Class<?> clazz, String actionType, boolean ignoredIfFailed,
            String startMessage) {
        return new TestCaseMethodNodeWrapper(collectMethodWithAnnotation(clazz), actionType, ignoredIfFailed,
                startMessage);
    }

    private Map<String, TestCaseMethodNodeWrapper> collectSetupAndTeardownMethods() {
        Map<String, TestCaseMethodNodeWrapper> methodNodeCollection = new HashMap<String, TestCaseMethodNodeWrapper>();

        methodNodeCollection.put(SetUp.class.getName(), getWrapper(SetUp.class, StringConstants.LOG_SETUP_ACTION, false,
                StringConstants.MAIN_MSG_START_RUNNING_SETUP_METHODS_FOR_TC));

        methodNodeCollection.put(TearDown.class.getName(),
                getWrapper(TearDown.class, StringConstants.LOG_TEAR_DOWN_ACTION, true,
                        StringConstants.MAIN_MSG_START_RUNNING_TEAR_DOWN_METHODS_FOR_TC));

        methodNodeCollection.put(TearDownIfPassed.class.getName(),
                getWrapper(TearDownIfPassed.class, StringConstants.LOG_TEAR_DOWN_ACTION, true,
                        StringConstants.MAIN_MSG_START_RUNNING_TEAR_DOWN_METHODS_FOR_PASSED_TC));

        methodNodeCollection.put(TearDownIfFailed.class.getName(),
                getWrapper(TearDownIfFailed.class, StringConstants.LOG_TEAR_DOWN_ACTION, true,
                        StringConstants.MAIN_MSG_START_RUNNING_TEAR_DOWN_METHODS_FOR_FAILED_TC));

        methodNodeCollection.put(TearDownIfError.class.getName(),
                getWrapper(TearDownIfError.class, StringConstants.LOG_TEAR_DOWN_ACTION, true,
                        StringConstants.MAIN_MSG_START_RUNNING_TEAR_DOWN_METHODS_FOR_ERROR_TC));

        return methodNodeCollection;
    }
}
