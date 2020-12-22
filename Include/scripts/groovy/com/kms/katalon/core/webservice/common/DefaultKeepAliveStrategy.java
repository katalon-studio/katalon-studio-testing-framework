package com.kms.katalon.core.webservice.common;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

public class DefaultKeepAliveStrategy implements ConnectionKeepAliveStrategy {
	@Override
	public long getKeepAliveDuration(final HttpResponse response, final HttpContext context) {
		// copied from source
		Args.notNull(response, "HTTP response");
		final HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
		while (it.hasNext()) {
			final HeaderElement he = it.nextElement();
			final String param = he.getName();
			final String value = he.getValue();
			if (value != null && param.equalsIgnoreCase("timeout")) {
				try {
					return Long.parseLong(value) * 1000;
				} catch (final NumberFormatException ignore) {
				}
			}
		}
		// If the server indicates no timeout, then let it be 1ms so that
		// connection is not kept alive
		// indefinitely
		return 1;
	}
}
