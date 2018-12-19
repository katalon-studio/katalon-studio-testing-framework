package com.kms.katalon.core.context.internal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kms.katalon.core.annotation.AfterTestCase;
import com.kms.katalon.core.annotation.AfterTestSuite;
import com.kms.katalon.core.annotation.BeforeTestCase;
import com.kms.katalon.core.annotation.BeforeTestSuite;

public class TestListenerCollector implements ExecutionListenerEventHandler {

    private static final Map<ExecutionListenerEvent, String> LISTENER_ANNOTATION_NAMES;

    static {
        LISTENER_ANNOTATION_NAMES = new HashMap<>();
        LISTENER_ANNOTATION_NAMES.put(ExecutionListenerEvent.BEFORE_TEST_SUITE, BeforeTestSuite.class.getName());
        LISTENER_ANNOTATION_NAMES.put(ExecutionListenerEvent.AFTER_TEST_SUITE, AfterTestSuite.class.getName());
        LISTENER_ANNOTATION_NAMES.put(ExecutionListenerEvent.BEFORE_TEST_CASE, BeforeTestCase.class.getName());
        LISTENER_ANNOTATION_NAMES.put(ExecutionListenerEvent.AFTER_TEST_CASE, AfterTestCase.class.getName());
    }

    private String sourceFolder;

    private List<TestHooker> testHookers;

    public TestListenerCollector(String sourceFolder) {
        this.sourceFolder = sourceFolder;
        collectTestContextInProject();
    }

    public void collectTestContextInProject() {
        testHookers = new ArrayList<>();
        File testListenerFolder = new File(sourceFolder);
        if (!testListenerFolder.exists()) {
            return;
        }

        try {
            Files.walk(testListenerFolder.toPath())
                    .filter(p -> p.toString().endsWith(".groovy"))
                    .map(p -> p.toAbsolutePath().toFile())
                    .forEach(file -> {
                        TestHooker testHooker = new TestHooker(file.getAbsolutePath());
                        testHookers.add(testHooker);
                    });
        } catch (IOException ignored) {}
    }

    @Override
    public void handleListenerEvent(ExecutionListenerEvent listenerEvent, Object[] injectedObjects) {
        testHookers.forEach(hooker -> {
            if (LISTENER_ANNOTATION_NAMES.containsKey(listenerEvent)) {
                String listenerAnnotationName = LISTENER_ANNOTATION_NAMES.get(listenerEvent);
                hooker.invokeContextMethods(listenerAnnotationName, injectedObjects);
            }
        });
    }
}
