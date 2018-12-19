package com.kms.katalon.core.main;

import java.security.PrivilegedAction;

@SuppressWarnings("rawtypes")
public class DoSetContextAction implements PrivilegedAction {
	ClassLoader classLoader;
	Thread currentThread;

	public DoSetContextAction(Thread thread, ClassLoader loader) {
		classLoader = loader;
		currentThread = thread;
	}

	public Object run() {
		currentThread.setContextClassLoader(classLoader);
		return null;
	}
}