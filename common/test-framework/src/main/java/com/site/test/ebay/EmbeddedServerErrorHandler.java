package com.site.test.ebay;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.servlet.ServletHandler;

/**
 * A handler that is registered at underlying implementation
 * context attributed and called by the HttpResponse.sendError method to write a
 * error page.
 */
public abstract class EmbeddedServerErrorHandler {
	HttpServletRequest 	m_request;
	
	/**
	 * Note: if resetting status to 200, need to call getResponse().setStatus(200);
	 * 
	 * @param request 	the original request
	 * @param writer 	the writer that has been opened to write the response
	 * @param code		the HTTP status code
	 * @param message	the status message
	 * @return true if page is handled, or false if the default error handler needs to be invoked
	 * @throws IOException
	 */
	protected abstract boolean handleError(HttpServletRequest request, Writer writer, int code, String message) throws IOException;
	
	public final HttpServletResponse getResponse() {
		// get the Response object, if required
		return HttpConnection.getCurrentConnection().getResponse();
	}
	
	public final Throwable getThrowable() {
		// get theThrowable, if required
		return (Throwable)m_request.getAttribute(ServletHandler.__J_S_ERROR_EXCEPTION);
	}

	final void setRequest(HttpServletRequest request) {
		m_request = request;
	}
}
