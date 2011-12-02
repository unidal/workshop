package com.ebay.eunit.mock.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class HttpServletResponseMock extends ServletResponseMock implements HttpServletResponse {
   private List<Cookie> m_cookies = new ArrayList<Cookie>(2);

   private Map<String, Object> m_headers = new HashMap<String, Object>(2);

   private int m_status = 200;

   private SimpleDateFormat m_format;

   public HttpServletResponseMock() {
      super();
   }

   public HttpServletResponseMock(OutputStream outputStream) {
      super(outputStream);
   }

   public HttpServletResponseMock(OutputStream outputStream, Map<Locale, String> localeCharsetMap) {
      super(outputStream, localeCharsetMap);
   }

   public HttpServletResponseMock(PrintWriter printWriter) {
      super(printWriter);
   }

   /**
    * Adds the specified cookie to the response. This method can be called
    * multiple times to set more than one cookie.
    * 
    * @param cookie
    *           the Cookie to return to the client
    * 
    */
   public void addCookie(Cookie cookie) {
      m_cookies.add(cookie);
   }

   /**
    * 
    * Adds a response header with the given name and date-value. The date is
    * specified in terms of milliseconds since the epoch. This method allows
    * response headers to have multiple values.
    * 
    * @param name
    *           the name of the header to set
    * @param date
    *           the additional date value
    * 
    * @see #setDateHeader
    */
   public void addDateHeader(String name, long date) {
      addHeader(name, formatDate(date));
   }

   /**
    * Adds a response header with the given name and value. This method allows
    * response headers to have multiple values.
    * 
    * @param name
    *           the name of the header
    * @param value
    *           the additional header value If it contains octet string, it
    *           should be encoded according to RFC 2047
    *           (http://www.ietf.org/rfc/rfc2047.txt)
    * 
    * @see #setHeader
    */

   @SuppressWarnings("unchecked")
   public void addHeader(String name, String value) {
      Object obj = m_headers.get(name);
      List<String> list = null;
      if (obj != null && obj instanceof List) {
         list = (List<String>) obj;
      } else {
         list = new ArrayList<String>(2);
         if (obj != null) {
            list.add((String) obj);
         }
      }
      list.add(value);
      m_headers.put(name, list);
      return;
   }

   /**
    * Adds a response header with the given name and integer value. This method
    * allows response headers to have multiple values.
    * 
    * @param name
    *           the name of the header
    * @param value
    *           the assigned integer value
    * 
    * @see #setIntHeader
    */

   public void addIntHeader(String name, int value) {
      addHeader(name, String.valueOf(value));
   }

   /**
    * Returns a boolean indicating whether the named response header has already
    * been set.
    * 
    * @param name
    *           the header name
    * @return <code>true</code> if the named response header has already been
    *         set; <code>false</code> otherwise
    */
   public boolean containsHeader(String name) {
      return m_headers.containsKey(name);
   }

   /**
    * @deprecated As of version 2.1, use encodeRedirectURL(String url) instead
    * 
    * @param url
    *           the url to be encoded.
    * @return the encoded URL if encoding is needed; the unchanged URL
    *         otherwise.
    */
   public String encodeRedirectUrl(String url) {
      return encodeRedirectURL(url);
   }

   /**
    * Encodes the specified URL for use in the <code>sendRedirect</code> method
    * or, if encoding is not needed, returns the URL unchanged. The
    * implementation of this method includes the logic to determine whether the
    * session ID needs to be encoded in the URL. Because the rules for making
    * this determination can differ from those used to decide whether to encode
    * a normal link, this method is separated from the <code>encodeURL</code>
    * method.
    * 
    * <p>
    * All URLs sent to the <code>HttpServletResponse.sendRedirect</code> method
    * should be run through this method. Otherwise, URL rewriting cannot be used
    * with browsers which do not support cookies.
    * 
    * @param url
    *           the url to be encoded.
    * @return the encoded URL if encoding is needed; the unchanged URL
    *         otherwise.
    * 
    * @see #sendRedirect
    * @see #encodeUrl
    */
   public String encodeRedirectURL(String url) {
      return url;
   }

   /**
    * @deprecated As of version 2.1, use encodeURL(String url) instead
    * 
    * @param url
    *           the url to be encoded.
    * @return the encoded URL if encoding is needed; the unchanged URL
    *         otherwise.
    */
   public String encodeUrl(String url) {
      return encodeURL(url);
   }

   /**
    * Encodes the specified URL by including the session ID in it, or, if
    * encoding is not needed, returns the URL unchanged. The implementation of
    * this method includes the logic to determine whether the session ID needs
    * to be encoded in the URL. For example, if the browser supports cookies, or
    * session tracking is turned off, URL encoding is unnecessary.
    * 
    * <p>
    * For robust session tracking, all URLs emitted by a servlet should be run
    * through this method. Otherwise, URL rewriting cannot be used with browsers
    * which do not support cookies.
    * 
    * @param url
    *           the url to be encoded.
    * @return the encoded URL if encoding is needed; the unchanged URL
    *         otherwise.
    */
   public String encodeURL(String url) {
      return url;
   }

   private String formatDate(long date) {
      if (m_format == null) {
         m_format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
         m_format.setTimeZone(TimeZone.getTimeZone("GMT"));
      }
      return m_format.format(new Date(date));
   }

   public List<Cookie> getCookies() {
      return m_cookies;
   }

   public Map<String, Object> getHeaders() {
      return m_headers;
   }

   public int getStatus() {
      return m_status;
   }

   /**
    * Sends an error response to the client using the specified status code and
    * clearing the buffer.
    * <p>
    * If the response has already been committed, this method throws an
    * IllegalStateException. After using this method, the response should be
    * considered to be committed and should not be written to.
    * 
    * @param sc
    *           the error status code
    * @exception IOException
    *               If an input or output exception occurs
    * @exception IllegalStateException
    *               If the response was committed before this method call
    */
   public void sendError(int sc) throws IOException {
      sendError(sc, "");
   }

   /**
    * Sends an error response to the client using the specified status. The
    * server defaults to creating the response to look like an HTML-formatted
    * server error page containing the specified message, setting the content
    * type to "text/html", leaving cookies and other headers unmodified.
    * 
    * If an error-page declaration has been made for the web application
    * corresponding to the status code passed in, it will be served back in
    * preference to the suggested msg parameter.
    * 
    * <p>
    * If the response has already been committed, this method throws an
    * IllegalStateException. After using this method, the response should be
    * considered to be committed and should not be written to.
    * 
    * @param sc
    *           the error status code
    * @param msg
    *           the descriptive message
    * @exception IOException
    *               If an input or output exception occurs
    * @exception IllegalStateException
    *               If the response was committed
    */
   public void sendError(int sc, String msg) throws IOException {
      throw new RuntimeException("Send Error to Client: status = " + sc + " msg = " + msg);
   }

   /**
    * Sends a temporary redirect response to the client using the specified
    * redirect location URL. This method can accept relative URLs; the servlet
    * container must convert the relative URL to an absolute URL before sending
    * the response to the client. If the location is relative without a leading
    * '/' the container interprets it as relative to the current request URI. If
    * the location is relative with a leading '/' the container interprets it as
    * relative to the servlet container root.
    * 
    * <p>
    * If the response has already been committed, this method throws an
    * IllegalStateException. After using this method, the response should be
    * considered to be committed and should not be written to.
    * 
    * @param location
    *           the redirect location URL
    * @exception IOException
    *               If an input or output exception occurs
    * @exception IllegalStateException
    *               If the response was committed or if a partial URL is given
    *               and cannot be converted into a valid URL
    */
   public void sendRedirect(String location) throws IOException {
      throw new RuntimeException("sendRedirect(String location) is not support.");
   }

   /**
    * 
    * Sets a response header with the given name and date-value. The date is
    * specified in terms of milliseconds since the epoch. If the header had
    * already been set, the new value overwrites the previous one. The
    * <code>containsHeader</code> method can be used to test for the presence of
    * a header before setting its value.
    * 
    * @param name
    *           the name of the header to set
    * @param date
    *           the assigned date value
    * 
    * @see #containsHeader
    * @see #addDateHeader
    */
   public void setDateHeader(String name, long date) {
      setHeader(name, formatDate(date));
   }

   /**
    * 
    * Sets a response header with the given name and value. If the header had
    * already been set, the new value overwrites the previous one. The
    * <code>containsHeader</code> method can be used to test for the presence of
    * a header before setting its value.
    * 
    * @param name
    *           the name of the header
    * @param value
    *           the header value If it contains octet string, it should be
    *           encoded according to RFC 2047
    *           (http://www.ietf.org/rfc/rfc2047.txt)
    * 
    * @see #containsHeader
    * @see #addHeader
    */
   public void setHeader(String name, String value) {
      m_headers.put(name, value);
   }

   /**
    * Sets a response header with the given name and integer value. If the
    * header had already been set, the new value overwrites the previous one.
    * The <code>containsHeader</code> method can be used to test for the
    * presence of a header before setting its value.
    * 
    * @param name
    *           the name of the header
    * @param value
    *           the assigned integer value
    * 
    * @see #containsHeader
    * @see #addIntHeader
    */
   public void setIntHeader(String name, int value) {
      setHeader(name, String.valueOf(value));
   }

   /**
    * Sets the status code for this response. This method is used to set the
    * return status code when there is no error (for example, for the status
    * codes SC_OK or SC_MOVED_TEMPORARILY). If there is an error, and the caller
    * wishes to invoke an error-page defined in the web application, the
    * <code>sendError</code> method should be used instead.
    * <p>
    * The container clears the buffer and sets the Location header, preserving
    * cookies and other headers.
    * 
    * @param sc
    *           the status code
    * 
    * @see #sendError
    */
   public void setStatus(int sc) {
      m_status = sc;
   }

   /**
    * @deprecated As of version 2.1, due to ambiguous meaning of the message
    *             parameter. To set a status code use
    *             <code>setStatus(int)</code>, to send an error with a
    *             description use <code>sendError(int, String)</code>.
    * 
    *             Sets the status code and message for this response.
    * 
    * @param sc
    *           the status code
    * @param sm
    *           the status message
    */
   public void setStatus(int sc, String sm) {
      setStatus(sc);
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder(1024);

      sb.append(getClass().getSimpleName());
      sb.append('[');
      super.asString(sb);
      sb.append(", cookies=").append(m_cookies);
      sb.append(", format=").append(m_format);
      sb.append(", headers=").append(m_headers);
      sb.append(", status=").append(m_status);
      sb.append(']');

      return sb.toString();
   }
}
