package com.kms.katalon.core.webservice.util;

import com.kms.katalon.core.testobject.RequestObject;

public class WebServiceCommonUtil {
    public static int getValidRequestTimeout(int timeout) {
        return timeout >= 0
                ? timeout
                : RequestObject.DEFAULT_TIMEOUT;
    }

    public static boolean isUnsetRequestTimeout(int timeout) {
        return timeout == RequestObject.TIMEOUT_UNSET;
    }

    public static boolean isUnsetMaxRequestResponseSize(long responseSizeLimit) {
        return responseSizeLimit == RequestObject.MAX_RESPONSE_SIZE_UNSET;
    }

    public static boolean isLimitedRequestResponseSize(long responseSizeLimit) {
        return responseSizeLimit > 0;
    }
}
