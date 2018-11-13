package com.kms.katalon.core.keyword.internal;

public interface IKeyword {

    SupportLevel getSupportLevel(Object... params);

    Object execute(Object... params);

}
