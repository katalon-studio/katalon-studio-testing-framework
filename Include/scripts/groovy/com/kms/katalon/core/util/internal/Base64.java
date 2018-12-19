package com.kms.katalon.core.util.internal;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang.StringUtils.defaultString;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * A <code>base64</code> util-class
 */
public class Base64 {

    /**
     * Encode string
     * 
     * @param plainText
     * @return encoded string
     * @see #decode(String)
     */
    public static String encode(String plainText) {
        if (isEmpty(plainText)) {
            return plainText;
        }

        // return DatatypeConverter.printBase64Binary(plainText.getBytes(UTF_8)); // Java 7
        return java.util.Base64.getEncoder().encodeToString(plainText.getBytes(UTF_8)); // Java 8
    }

    /**
     * Decode encoded string
     * 
     * @param encodedText
     * @return decoded string
     * @see #encode(String)
     */
    public static String decode(String encodedText) {
        if (isBlank(encodedText)) {
            return encodedText;
        }

        // return new String(DatatypeConverter.parseBase64Binary(encodedText), UTF_8); // Java 7
        return new String(java.util.Base64.getDecoder().decode(encodedText), UTF_8); // Java 8
    }

    public static String basicEncode(String username, String password) {
        return encode(defaultString(username) + ":" + defaultString(password));
    }

    public static String[] basicDecode(String encodedString) {
        String usernamePassword = decode(encodedString);
        String[] data = new String[] { "", "" };
        if (isEmpty(usernamePassword)) {
            return data;
        }

        data[0] = usernamePassword;

        int separatorPos = usernamePassword.indexOf(":");
        if (separatorPos >= 0) {
            data[0] = usernamePassword.substring(0, separatorPos);
            data[1] = usernamePassword.substring(separatorPos + 1);
        }
        return data;
    }

}
