package com.kms.katalon.core.webservice.support;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

public final class UrlEncoder {

	public static final Charset US_ASCII = Charset.forName("US-ASCII");
	public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
	public static final Charset UTF_8 = Charset.forName("UTF-8");
	public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
	public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
	public static final Charset UTF_16 = Charset.forName("UTF-16");
	
	public static final Charset DEFAULT_ENCODING = UTF_8;

	private UrlEncoder(){}
	
	public static String encode(String source) {
		return encode(source, DEFAULT_ENCODING);
	}

	public static String encode(String source, Charset encoding) {
		try {
			return FastUrlEncoder.urlEncode(source, encoding.name());
		} catch (UnsupportedEncodingException ueex) {
			throw new AssertionError(ueex);
		}
	}

	public static String decode(String source) {
		return decode(source, DEFAULT_ENCODING);
	}

	public static String decode(String source, Charset encoding) {
		try {
			return URLDecoder.decode(source, encoding.name());
		} catch (UnsupportedEncodingException uuex) {
			throw new AssertionError(uuex);
		}
	}
	
}