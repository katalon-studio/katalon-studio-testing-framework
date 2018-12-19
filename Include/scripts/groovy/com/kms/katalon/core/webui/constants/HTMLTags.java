package com.kms.katalon.core.webui.constants;

import org.apache.commons.lang.StringUtils;

public class HTMLTags {
    // Default HTML Tags
    public static final String TAG_RADIO = "radio";

    public static final String TAG_TEXT = "text";

    public static final String TAG_TEXTAREA = "textarea";

    public static final String TAG_LABEL = "label";

    public static final String TAG_SELECT = "select";

    public static final String TAG_IMAGE = "image";

    public static final String TAG_IMG = "img";

    public static final String TAG_FILE = "file";

    public static final String TAG_CHECKBOX = "checkbox";

    public static final String TAG_BUTTON = "button";

    public static final String TAG_RESET = "reset";

    public static final String TAG_SUBMIT = "submit";

    public static final String TAG_INPUT = "input";

    public static final String TAG_A = "a";

    public static String getElementType(String tag, String type) {
        return TAG_INPUT.equals(tag) ? StringUtils.defaultString(type) : StringUtils.defaultString(tag);
    }
}
