package com.kms.katalon.core.util;

import net.lightbody.bmp.core.har.HarEntry;

public class KatalonHarEntry extends HarEntry {

    private RequestInformation _katalonRequestInformation; // HAR's naming convention for custom fields
    
    public KatalonHarEntry() {
        
    }
    
    public KatalonHarEntry(HarEntry harEntry) {
        setPageref(harEntry.getPageref());
        setStartedDateTime(harEntry.getStartedDateTime());
        setRequest(harEntry.getRequest());
        setResponse(harEntry.getResponse());
        setCache(harEntry.getCache());
        setTimings(harEntry.getTimings());
        setServerIPAddress(harEntry.getServerIPAddress());
        setConnection(harEntry.getConnection());
        setComment(harEntry.getComment());
    }

    public RequestInformation get_katalonRequestInformation() {
        return _katalonRequestInformation;
    }
    
    public void set_katalonRequestInformation(RequestInformation _katalonRequestInformation) {
        this._katalonRequestInformation = _katalonRequestInformation;
    }
}
