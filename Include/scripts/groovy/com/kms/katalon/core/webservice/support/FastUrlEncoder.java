package com.kms.katalon.core.webservice.support;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

public final class FastUrlEncoder {
	
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static final BitSet SAFE_OCTETS = new BitSet(256);
	private static final BitSet URI_SAFE_OCTETS;

	private FastUrlEncoder(){}
	
	private static boolean urlEncode(String source, String encoding,
			BitSet safeOctets, boolean plusForSpace, Appendable out)
			throws UnsupportedEncodingException, IOException {
		byte[] data = source.getBytes(encoding);
		boolean containsSpace = false;
		int outputLength = 0;

		for (int index = 0; index < data.length; index++) {
			int ch = data[index];
			if (ch < 0)
				ch += 256;

			if (safeOctets.get(ch)) {
				out.append((char) ch);
				outputLength++;
			} else if ((plusForSpace) && (ch == 32)) {
				containsSpace = true;
				out.append('+');
				outputLength++;
			} else {
				out.append('%');
				out.append(HEX_DIGITS[(ch >> 4)]);
				out.append(HEX_DIGITS[(ch & 0xF)]);
				outputLength += 3;
			}
		}

		return (containsSpace) || (outputLength != source.length());
	}

	private static String urlEncode(String source, String encoding,
			BitSet safeOctets, boolean plusForSpace)
			throws UnsupportedEncodingException {
		StringBuilder out = new StringBuilder(source.length() * 2);
		boolean needsEncoding = false;
		try {
			needsEncoding = urlEncode(source, encoding, safeOctets,
					plusForSpace, out);
		} catch (IOException ioex) {
			throw new AssertionError(ioex);
		}

		return needsEncoding ? out.toString() : source;
	}

	public static String urlEncode(String source, String encoding)
			throws UnsupportedEncodingException {
		return urlEncode(source, encoding, SAFE_OCTETS, true);
	}

	public static String uriEncode(String source, String encoding)
			throws UnsupportedEncodingException {
		return urlEncode(source, encoding, URI_SAFE_OCTETS, false);
	}

	static {
		for (int index = 48; index <= 57; index++)
			SAFE_OCTETS.set(index);
		for (int index = 65; index <= 90; index++)
			SAFE_OCTETS.set(index);
		for (int index = 97; index <= 122; index++)
			SAFE_OCTETS.set(index);

		SAFE_OCTETS.set(45);
		SAFE_OCTETS.set(95);
		SAFE_OCTETS.set(46);
		SAFE_OCTETS.set(42);

		URI_SAFE_OCTETS = new BitSet(256);

		for (int index = 48; index <= 57; index++)
			URI_SAFE_OCTETS.set(index);
		for (int index = 65; index <= 90; index++)
			URI_SAFE_OCTETS.set(index);
		for (int index = 97; index <= 122; index++)
			URI_SAFE_OCTETS.set(index);

		URI_SAFE_OCTETS.set(45);
		URI_SAFE_OCTETS.set(95);
		URI_SAFE_OCTETS.set(46);
		URI_SAFE_OCTETS.set(126);
	}
}