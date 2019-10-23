package com.kms.katalon.core.main;

import static com.kms.katalon.core.constants.StringConstants.DF_CHARSET;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.codehaus.groovy.runtime.InvokerHelper;

import com.kms.katalon.core.configuration.RunConfiguration;
import com.kms.katalon.core.constants.StringConstants;
import com.kms.katalon.core.testcase.TestCaseFactory;
import com.kms.katalon.core.testdata.TestDataFactory;
import com.kms.katalon.core.testobject.ObjectRepository;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

public class ScriptEngine extends GroovyScriptEngine {
    
    private static final Map<String, String> testCaseNameLookup = new ConcurrentHashMap<>();
    
    // Used to generate new temp script name
    private int counter;

    private Map<String, Script> scriptLookup;

    private CompilerConfigurationProvider configurationProvider;

    private GroovyClassLoader executingScriptClassLoader;

    private GroovyClassLoader variableEvaluateClassLoader;

    public static ScriptEngine getDefault(ClassLoader parentClassLoader) throws IOException {
        URL[] roots = new URL[] { new File(RunConfiguration.getProjectDir(), StringConstants.CUSTOM_KEYWORD_FOLDER_NAME)
                .toURI().toURL() };
        return new ScriptEngine(roots, parentClassLoader);
    }

    public ScriptEngine(URL[] roots, ClassLoader parentClassLoader) {
        super(roots, parentClassLoader);
        counter = 0;
        scriptLookup = new HashMap<String, Script>();
        configurationProvider = new CompilerConfigurationProvider(getGroovyClassLoader());
    }

    public void changeConfigForExecutingScript() throws ClassNotFoundException {
        setConfig(configurationProvider.getConfigForExecutingScript());
    }

    public void changeConfigForCollectingVariable() {
        setConfig(configurationProvider.getConfigForCollectingVariable());
    }

    public GroovyClassLoader getExecutingScriptClassLoader() throws ClassNotFoundException {
        if (executingScriptClassLoader == null) {
            executingScriptClassLoader = new GroovyClassLoader(getParentClassLoader(),
                    configurationProvider.getConfigForExecutingScript());
        }
        return executingScriptClassLoader;
    }

    protected GroovyClassLoader getVariableValuateClassLoader() throws ClassNotFoundException {
        if (variableEvaluateClassLoader == null) {
            variableEvaluateClassLoader = new GroovyClassLoader(getParentClassLoader(),
                    configurationProvider.getConfigForCollectingVariable());
        }
        return variableEvaluateClassLoader;
    }

    protected synchronized String generateScriptName() {
        return "Script" + (++counter) + "." + StringConstants.SCRIPT_FILE_EXT;
    }

    // Parse this temporary class without caching
    public Object runScript(final String scriptText, Binding binding)
            throws ResourceException, ScriptException, IOException, ClassNotFoundException {
        return run(getGroovyCodeSource(scriptText, generateScriptName()), binding, false);
    }

    // Parse this temporary class without caching and not logging
    public Object runScriptWithoutLogging(final String scriptText, Binding binding)
            throws ResourceException, ScriptException, IOException, ClassNotFoundException {
        Class<?> clazz = getVariableValuateClassLoader()
                .parseClass(getGroovyCodeSource(scriptText, generateScriptName()), false);
        return getScript(clazz, binding, false).run();
    }

    // Parse this class as script text
    public Object runScriptAsRawText(
            final String scriptText, String className, Binding binding, String testCaseName)
            throws ResourceException, ScriptException, IOException, ClassNotFoundException {
        
        if (testCaseName != null) {
            String scriptId = new File(className).getName();
            testCaseNameLookup.put(scriptId, testCaseName);
            testCaseNameLookup.put(scriptId.replace(".groovy", ""), testCaseName);
        }
        String processedScriptText = preProcessScriptBeforeBuild(scriptText);
        return run(getGroovyCodeSource(processedScriptText, className), binding, true);
    }

    public Object runScriptMethodAsRawText(final String scriptText, final String className, final String methodName,
            Object args, Binding binding)
            throws ResourceException, ScriptException, ClassNotFoundException, IOException {
        String processedScriptText = preProcessScriptBeforeBuild(scriptText);
        return getScript(getGroovyCodeSource(processedScriptText, className), binding, true).invokeMethod(methodName,
                args);
    }

    public Object runScriptMethodAsRawText(final String scriptText, final String className, final String methodName,
            Binding binding) throws ResourceException, ScriptException, ClassNotFoundException, IOException {
        String processedScriptText = preProcessScriptBeforeBuild(scriptText);
        return getScript(getGroovyCodeSource(processedScriptText, className), binding, true).invokeMethod(methodName,
                null);
    }

    private String preProcessScriptBeforeBuild(String scriptText) {
        return processNotRunLabels(scriptText);
    }

    private static String processNotRunLabels(String scriptText) {
        String notRunLabel = StringConstants.NOT_RUN_LABEL;
        String notRunLabelSearchString = notRunLabel + ":";
        String notRunLabelPrefix = notRunLabel + "_";
        Matcher m = Pattern.compile(notRunLabelSearchString).matcher(scriptText);
        StringBuffer sb = new StringBuffer();
        int generatedIndex = 0;
        while (m.find()) {
            m.appendReplacement(sb, notRunLabelPrefix + (generatedIndex++) + ":");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public Object runScriptMethod(final String className, final String methodName, Object args, Binding binding)
            throws ResourceException, ScriptException, ClassNotFoundException {
        return getScript(getGroovyClassLoader().loadClass(className), binding, true).invokeMethod(methodName, args);
    }

    public Object runScriptMethod(final String className, final String methodName, Binding binding)
            throws ResourceException, ScriptException, ClassNotFoundException {
        return runScriptMethod(className, methodName, null, binding);
    }

    public Object runScript(final File file, Binding binding)
            throws ResourceException, ScriptException, IOException, ClassNotFoundException {
        return run(getGroovyCodeSource(file), binding, true);
    }

    public Script parseClass(final File file, Binding binding) throws IOException, ClassNotFoundException {
        return getScript(getGroovyCodeSource(file), binding, true);
    }

    private GroovyCodeSource getGroovyCodeSource(final File file) {
        try {
            return getGroovyCodeSource(FileUtils.readFileToString(file, DF_CHARSET),
                    file.toURI().toURL().toExternalForm());
        } catch (IOException e) {
            return null;
        }
    }

    private GroovyCodeSource getGroovyCodeSource(final String scriptText, final String fileName) {
        GroovyCodeSource gcs = AccessController.doPrivileged(new PrivilegedAction<GroovyCodeSource>() {
            public GroovyCodeSource run() {
                return new GroovyCodeSource(scriptText, fileName, GroovyShell.DEFAULT_CODE_BASE);
            }
        });
        return gcs;
    }

    private Object run(GroovyCodeSource gcs, Binding binding, boolean remember)
            throws IOException, ClassNotFoundException {
        return getScript(gcs, binding, remember).run();
    }

    private Script getScript(GroovyCodeSource gcs, Binding binding, boolean remember)
            throws IOException, ClassNotFoundException {
        Class<?> clazz = getExecutingScriptClassLoader().parseClass(gcs, remember);
        return getScript(clazz, binding, remember);
    }

    public Script getScript(Class<?> clazz, Binding binding, boolean remember) {
        if (!remember) {
            return InvokerHelper.createScript(clazz, binding);
        }

        Script script = scriptLookup.get(clazz.getName());
        return script != null ? script : InvokerHelper.createScript(clazz, binding);
    }

    private class CompilerConfigurationProvider {

        private static final String FIND_TEST_CASE_METHOD_NAME = "findTestCase";

        private static final String FIND_TEST_DATA_METHOD_NAME = "findTestData";

        private static final String FIND_TEST_OBJECT_METHOD_NAME = "findTestObject";

        private GroovyClassLoader parentClassLoader;

        private CompilerConfiguration executingScriptConfig;

        private CompilerConfiguration collectingVariableConfig;

        public CompilerConfigurationProvider(GroovyClassLoader parentClassLoader) {
            this.parentClassLoader = parentClassLoader;
        }

        @SuppressWarnings("unchecked")
        public CompilerConfiguration getConfigForExecutingScript() throws ClassNotFoundException {
            if (executingScriptConfig == null) {
                executingScriptConfig = new CompilerConfiguration(System.getProperties());
                Class<?> astTransformationClass = parentClassLoader
                        .loadClass(StringConstants.TEST_STEP_TRANSFORMATION_CLASS);

                executingScriptConfig.addCompilationCustomizers(
                        new ASTTransformationCustomizer((Class<? extends Annotation>) astTransformationClass));
            }
            return executingScriptConfig;
        }

        public CompilerConfiguration getConfigForCollectingVariable() {
            if (collectingVariableConfig == null) {
                collectingVariableConfig = new CompilerConfiguration();
                ImportCustomizer importCustomizer = new ImportCustomizer();
                importCustomizer.addImport(TestDataFactory.class.getSimpleName(), TestDataFactory.class.getName());
                importCustomizer.addImport(ObjectRepository.class.getSimpleName(), ObjectRepository.class.getName());
                importCustomizer.addImport(TestCaseFactory.class.getSimpleName(), TestCaseFactory.class.getName());
                importCustomizer.addImport(RunConfiguration.class.getSimpleName(), RunConfiguration.class.getName());
                importCustomizer.addStaticImport(TestDataFactory.class.getName(), FIND_TEST_DATA_METHOD_NAME);
                importCustomizer.addStaticImport(ObjectRepository.class.getName(), FIND_TEST_OBJECT_METHOD_NAME);
                importCustomizer.addStaticImport(TestCaseFactory.class.getName(), FIND_TEST_CASE_METHOD_NAME);
                importCustomizer.addImport("GlobalVariable", "internal.GlobalVariable");
                collectingVariableConfig.addCompilationCustomizers(importCustomizer);
            }
            return collectingVariableConfig;
        }
    }

    public static String getTestCaseName(String script) {
        String testCaseName = testCaseNameLookup.get(script);
        return testCaseName;
    }
}
