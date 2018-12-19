package com.kms.katalon.core.driver.internal;

import java.util.ArrayList;
import java.util.List;

public class DriverCleanerCollector {
	public List<IDriverCleaner> driverCleaners;
	
	private static DriverCleanerCollector _instance;
	private DriverCleanerCollector() {
		driverCleaners = new ArrayList<IDriverCleaner>();
	}
	
	public static DriverCleanerCollector getInstance() {
		if (_instance == null) {
			_instance = new DriverCleanerCollector();
		}
		return _instance;
	}
	
	public void addDriverCleaner(IDriverCleaner driverCleaner) {
		getInstance().driverCleaners.add(driverCleaner);
	}
	
	public void cleanDrivers() {
		for (IDriverCleaner cleaner : getInstance().driverCleaners) {
			cleaner.cleanDrivers();;
		}
	}
}
