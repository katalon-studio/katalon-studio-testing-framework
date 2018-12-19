package com.kms.katalon.core.helper.screenrecorder;

import atu.testrecorder.exceptions.ATUTestRecorderException;

public class VideoRecorderException extends Exception {

    private static final long serialVersionUID = 4515907718547533774L;

    public VideoRecorderException(String message) {
        super(message);
    }

    public VideoRecorderException(ATUTestRecorderException e) {
        this(e.toString().replace("[ATU Test Recorder Exception] ", ""));
    }

    public VideoRecorderException(Throwable e) {
        super(e);
    }

    @Override
    public String toString() {
        return "[Katalon Video Recorder Exception] " + getMessage();
    }

}
