package com.kms.katalon.core.execution;

import java.util.HashMap;
import java.util.Map;

import com.kms.katalon.core.main.TestCaseExecutor;

/**
 * This stores and retrieves data about test execution such as test name,
 * location of report folder
 * 
 * @see TestExecutionSocketServerEndpoint#handleMhtml(java.nio.ByteBuffer)
 * @see TestCaseExecutor#notifyTestExecutionSocketServerEndpoint
 * 
 * @author thanhto
 *
 */
public class TestExecutionDataProvider {
    private Map<String, Object> hashtoTestInfo;

    private static TestExecutionDataProvider _instance;

    private TestExecutionDataProvider() {
        hashtoTestInfo = new HashMap<>();
    }

    public static TestExecutionDataProvider getInstance() {
        if (_instance == null) {
            _instance = new TestExecutionDataProvider();
        }
        return _instance;
    }

    public void addTestExecutionData(String key, Object value) {
        hashtoTestInfo.put(key, value);
    }

    public Object getTestExecutionData(String key) {
        return hashtoTestInfo.get(key);
    }
}
