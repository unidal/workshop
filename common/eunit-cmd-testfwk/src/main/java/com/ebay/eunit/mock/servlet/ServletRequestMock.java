package com.ebay.eunit.mock.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;

import com.ebay.eunit.helper.Joiners;

public class ServletRequestMock implements ServletRequest {
   private Map<String, Object> m_attributes = new HashMap<String, Object>();

   private String m_characterEncoding = null;

   private int m_contentLength = -1;

   private String m_contentType = null;

   private String m_localAddr = "127.0.0.1";

   private Locale m_locale = Locale.getDefault();

   private Vector<Locale> m_locales = null;

   private String m_localName = "localhost";

   private int m_localPort = 80;

   private Map<String, String[]> m_parameters = new HashMap<String, String[]>();

   private String m_protocol = "HTTP 1.1";

   private String m_remoteAddr = "127.0.0.1";

   private String m_remoteHost = "127.0.0.1";

   private int m_remotePort;

   private String m_scheme = "HTTP";

   private String m_serverName;

   private int m_serverPort;

   private boolean m_secure = false;

   protected void asString(StringBuilder sb) {
      sb.append("attributes=").append(m_attributes);
      sb.append(", characterEncoding=").append(getCharacterEncoding());
      sb.append(", contentLength=").append(getContentLength());
      sb.append(", contentType=").append(getContentType());
      sb.append(", localAddr=").append(getLocalAddr());
      sb.append(", locale=").append(getLocale());
      sb.append(", locales=").append(m_locales);
      sb.append(", localName=").append(getLocalName());
      sb.append(", localPort=").append(getLocalPort());
      sb.append(", parameters=").append(getParameterMap());
      sb.append(", protocol=").append(getProtocol());
      sb.append(", remoteAddr=").append(getRemoteAddr());
      sb.append(", remoteHost=").append(getRemoteHost());
      sb.append(", remotePort=").append(getRemotePort());
      sb.append(", scheme=").append(getScheme());
      sb.append(", secure=").append(isSecure());
      sb.append(", serverName=").append(getServerName());
      sb.append(", serverPort=").append(getServerPort());
   }

   /**
    * 
    * Returns the value of the named attribute as an <code>Object</code>, or
    * <code>null</code> if no attribute of the given name exists.
    * 
    * <p>
    * Attributes can be set two ways. The servlet container may set attributes
    * to make available custom information about a request. For example, for
    * requests made using HTTPS, the attribute
    * <code>javax.servlet.request.X509Certificate</code> can be used to retrieve
    * information on the certificate of the client. Attributes can also be set
    * programatically using {@link ServletRequest#setAttribute}. This allows
    * information to be embedded into a request before a
    * {@link RequestDispatcher} call.
    * 
    * <p>
    * Attribute names should follow the same conventions as package names. This
    * specification reserves names matching <code>java.*</code>,
    * <code>javax.*</code>, and <code>sun.*</code>.
    * 
    * @param name
    *           a <code>String</code> specifying the name of the attribute
    * 
    * @return an <code>Object</code> containing the value of the attribute, or
    *         <code>null</code> if the attribute does not exist
    * 
    */
   public Object getAttribute(String name) {
      return m_attributes.get(name);
   }

   /**
    * Returns an <code>Enumeration</code> containing the names of the attributes
    * available to this request. This method returns an empty
    * <code>Enumeration</code> if the request has no attributes available to it.
    * 
    * 
    * @return an <code>Enumeration</code> of strings containing the names of the
    *         request's attributes
    * 
    */
   public Enumeration<String> getAttributeNames() {
      return Collections.enumeration(m_attributes.keySet());
   }

   /**
    * Returns the name of the character encoding used in the body of this
    * request. This method returns <code>null</code> if the request does not
    * specify a character encoding
    * 
    * 
    * @return a <code>String</code> containing the name of the character
    *         encoding, or <code>null</code> if the request does not specify a
    *         character encoding
    * 
    */
   public String getCharacterEncoding() {
      return m_characterEncoding;
   }

   /**
    * Returns the length, in bytes, of the request body and made available by
    * the input stream, or -1 if the length is not known. For HTTP servlets,
    * same as the value of the CGI variable CONTENT_LENGTH.
    * 
    * @return an integer containing the length of the request body or -1 if the
    *         length is not known
    * 
    */
   public int getContentLength() {
      return m_contentLength;
   }

   /**
    * Returns the MIME type of the body of the request, or <code>null</code> if
    * the type is not known. For HTTP servlets, same as the value of the CGI
    * variable CONTENT_TYPE.
    * 
    * @return a <code>String</code> containing the name of the MIME type of the
    *         request, or null if the type is not known
    * 
    */

   public String getContentType() {
      return m_contentType;
   }

   /**
    * Retrieves the body of the request as binary data using a
    * {@link ServletInputStream}. Either this method or {@link #getReader} may
    * be called to read the body, not both.
    * 
    * @return a {@link ServletInputStream} object containing the body of the
    *         request
    * 
    * @exception IllegalStateException
    *               if the {@link #getReader} method has already been called for
    *               this request
    * 
    * @exception IOException
    *               if an input or output exception occurred
    * 
    */

   public ServletInputStream getInputStream() throws IOException {
      throw new IOException("This method is not supported in Java Main mode");
   }

   /**
    * Returns the Internet Protocol (IP) address of the interface on which the
    * request was received.
    * 
    * @return a <code>String</code> containing the IP address on which the
    *         request was received.
    * 
    * @since 2.4
    * 
    */
   public String getLocalAddr() {
      return m_localAddr; // "127.0.0.1" ;
   }

   /**
    * 
    * Returns the preferred <code>Locale</code> that the client will accept
    * content in, based on the Accept-Language header. If the client request
    * doesn't provide an Accept-Language header, this method returns the default
    * locale for the server.
    * 
    * 
    * @return the preferred <code>Locale</code> for the client
    * 
    */
   public Locale getLocale() {
      return m_locale; // Locale.getDefault() ;
   }

   /**
    * 
    * Returns an <code>Enumeration</code> of <code>Locale</code> objects
    * indicating, in decreasing order starting with the preferred locale, the
    * locales that are acceptable to the client based on the Accept-Language
    * header. If the client request doesn't provide an Accept-Language header,
    * this method returns an <code>Enumeration</code> containing one
    * <code>Locale</code>, the default locale for the server.
    * 
    * 
    * @return an <code>Enumeration</code> of preferred <code>Locale</code>
    *         objects for the client
    * 
    */
   public Enumeration<Locale> getLocales() {
      if (m_locales == null) {
         m_locales = new Vector<Locale>();
         m_locales.add(Locale.getDefault());
      }

      return m_locales.elements();
   }

   /**
    * Returns the host name of the Internet Protocol (IP) interface on which the
    * request was received.
    * 
    * @return a <code>String</code> containing the host name of the IP on which
    *         the request was received.
    * 
    * @since 2.4
    */
   public String getLocalName() {
      return m_localName;
   }

   /**
    * Returns the Internet Protocol (IP) port number of the interface on which
    * the request was received.
    * 
    * @return an integer specifying the port number
    * 
    * @since 2.4
    */
   public int getLocalPort() {
      return m_localPort;
   }

   /**
    * Returns the value of a request parameter as a <code>String</code>, or
    * <code>null</code> if the parameter does not exist. Request parameters are
    * extra information sent with the request. For HTTP servlets, parameters are
    * contained in the query string or posted form data.
    * 
    * <p>
    * You should only use this method when you are sure the parameter has only
    * one value. If the parameter might have more than one value, use
    * {@link #getParameterValues}.
    * 
    * <p>
    * If you use this method with a multivalued parameter, the value returned is
    * equal to the first value in the array returned by
    * <code>getParameterValues</code>.
    * 
    * <p>
    * If the parameter data was sent in the request body, such as occurs with an
    * HTTP POST request, then reading the body directly via
    * {@link #getInputStream} or {@link #getReader} can interfere with the
    * execution of this method.
    * 
    * @param name
    *           a <code>String</code> specifying the name of the parameter
    * 
    * @return a <code>String</code> representing the single value of the
    *         parameter
    * 
    * @see #getParameterValues
    * 
    */
   /*
    * Try to fix
    * com.sun.ts.tests.jsp.api.javax_servlet.jsp.pagecontext.URLClient
    * #pageContextForwardContextPathTest
    * com.sun.ts.tests.jsp.api.javax_servlet.jsp
    * .pagecontext.URLClient#pageContextForwardIOExceptionTest
    * com.sun.ts.tests.jsp
    * .api.javax_servlet.jsp.pagecontext.URLClient#pageContextForwardPagePathTest
    * com.sun.ts.tests.jsp.api.javax_servlet.jsp.pagecontext.URLClient#
    * pageContextForwardServletExceptionTest
    * com.sun.ts.tests.jsp.spec.core_syntax
    * .actions.param.URLClient#jspParamForwardTest
    * com.sun.ts.tests.jsp.spec.core_syntax
    * .actions.param.URLClient#jspParamIncludeTest
    * com.sun.ts.tests.jsp.spec.core_syntax
    * .actions.usebean.URLClient#positiveApplicationScopedObjectTest
    * com.sun.ts.tests
    * .jsp.spec.core_syntax.actions.usebean.URLClient#positivePageScopedObjectTest
    * com.sun.ts.tests.jsp.spec.core_syntax.actions.usebean.URLClient#
    * positiveRequestScopedObjectTest
    * com.sun.ts.tests.jsp.spec.core_syntax.actions
    * .usebean.URLClient#positiveSessionScopedObjectTest
    */

   public String getParameter(String name) {
      // New value take precedence over old values
      String[] params = m_parameters.get(name);

      if (params != null) {
         return Joiners.by(',').join(params);
      }

      return null;
   }

   /**
    * Returns a java.util.Map of the parameters of this request. Request
    * parameters are extra information sent with the request. For HTTP servlets,
    * parameters are contained in the query string or posted form data.
    * 
    * @return an immutable java.util.Map containing parameter names as keys and
    *         parameter values as map values. The keys in the parameter map are
    *         of type String. The values in the parameter map are of type String
    *         array.
    * 
    */

   /*
    * Try to fix
    * com.sun.ts.tests.jsp.api.javax_servlet.jsp.pagecontext.URLClient
    * #pageContextForwardContextPathTest
    * com.sun.ts.tests.jsp.api.javax_servlet.jsp
    * .pagecontext.URLClient#pageContextForwardIOExceptionTest
    * com.sun.ts.tests.jsp
    * .api.javax_servlet.jsp.pagecontext.URLClient#pageContextForwardPagePathTest
    * com.sun.ts.tests.jsp.api.javax_servlet.jsp.pagecontext.URLClient#
    * pageContextForwardServletExceptionTest
    * com.sun.ts.tests.jsp.spec.core_syntax
    * .actions.param.URLClient#jspParamForwardTest
    * com.sun.ts.tests.jsp.spec.core_syntax
    * .actions.param.URLClient#jspParamIncludeTest
    * com.sun.ts.tests.jsp.spec.core_syntax
    * .actions.usebean.URLClient#positiveApplicationScopedObjectTest
    * com.sun.ts.tests
    * .jsp.spec.core_syntax.actions.usebean.URLClient#positivePageScopedObjectTest
    * com.sun.ts.tests.jsp.spec.core_syntax.actions.usebean.URLClient#
    * positiveRequestScopedObjectTest
    * com.sun.ts.tests.jsp.spec.core_syntax.actions
    * .usebean.URLClient#positiveSessionScopedObjectTest
    */
   public Map<String, String> getParameterMap() {
      Map<String, String> map = new HashMap<String, String>();

      for (Map.Entry<String, String[]> e : m_parameters.entrySet()) {
         map.put(e.getKey(), Joiners.by(',').join(e.getValue()));
      }

      return map;
   }

   /**
    * 
    * Returns an <code>Enumeration</code> of <code>String</code> objects
    * containing the names of the parameters contained in this request. If the
    * request has no parameters, the method returns an empty
    * <code>Enumeration</code>.
    * 
    * @return an <code>Enumeration</code> of <code>String</code> objects, each
    *         <code>String</code> containing the name of a request parameter; or
    *         an empty <code>Enumeration</code> if the request has no parameters
    * 
    */
   /*
    * Try to fix
    * com.sun.ts.tests.jsp.api.javax_servlet.jsp.pagecontext.URLClient
    * #pageContextForwardContextPathTest
    * com.sun.ts.tests.jsp.api.javax_servlet.jsp
    * .pagecontext.URLClient#pageContextForwardIOExceptionTest
    * com.sun.ts.tests.jsp
    * .api.javax_servlet.jsp.pagecontext.URLClient#pageContextForwardPagePathTest
    * com.sun.ts.tests.jsp.api.javax_servlet.jsp.pagecontext.URLClient#
    * pageContextForwardServletExceptionTest
    * com.sun.ts.tests.jsp.spec.core_syntax
    * .actions.param.URLClient#jspParamForwardTest
    * com.sun.ts.tests.jsp.spec.core_syntax
    * .actions.param.URLClient#jspParamIncludeTest
    * com.sun.ts.tests.jsp.spec.core_syntax
    * .actions.usebean.URLClient#positiveApplicationScopedObjectTest
    * com.sun.ts.tests
    * .jsp.spec.core_syntax.actions.usebean.URLClient#positivePageScopedObjectTest
    * com.sun.ts.tests.jsp.spec.core_syntax.actions.usebean.URLClient#
    * positiveRequestScopedObjectTest
    * com.sun.ts.tests.jsp.spec.core_syntax.actions
    * .usebean.URLClient#positiveSessionScopedObjectTest
    */
   public Enumeration<String> getParameterNames() {
      return Collections.enumeration(m_parameters.keySet());
   }

   /**
    * Returns an array of <code>String</code> objects containing all of the
    * values the given request parameter has, or <code>null</code> if the
    * parameter does not exist.
    * 
    * <p>
    * If the parameter has a single value, the array has a length of 1.
    * 
    * @param name
    *           a <code>String</code> containing the name of the parameter whose
    *           value is requested
    * 
    * @return an array of <code>String</code> objects containing the parameter's
    *         values
    * 
    * @see #getParameter
    * 
    */
   /*
    * Try to fix
    * com.sun.ts.tests.jsp.api.javax_servlet.jsp.pagecontext.URLClient
    * #pageContextForwardContextPathTest
    * com.sun.ts.tests.jsp.api.javax_servlet.jsp
    * .pagecontext.URLClient#pageContextForwardIOExceptionTest
    * com.sun.ts.tests.jsp
    * .api.javax_servlet.jsp.pagecontext.URLClient#pageContextForwardPagePathTest
    * com.sun.ts.tests.jsp.api.javax_servlet.jsp.pagecontext.URLClient#
    * pageContextForwardServletExceptionTest
    * com.sun.ts.tests.jsp.spec.core_syntax
    * .actions.param.URLClient#jspParamForwardTest
    * com.sun.ts.tests.jsp.spec.core_syntax
    * .actions.param.URLClient#jspParamIncludeTest
    * com.sun.ts.tests.jsp.spec.core_syntax
    * .actions.usebean.URLClient#positiveApplicationScopedObjectTest
    * com.sun.ts.tests
    * .jsp.spec.core_syntax.actions.usebean.URLClient#positivePageScopedObjectTest
    * com.sun.ts.tests.jsp.spec.core_syntax.actions.usebean.URLClient#
    * positiveRequestScopedObjectTest
    * com.sun.ts.tests.jsp.spec.core_syntax.actions
    * .usebean.URLClient#positiveSessionScopedObjectTest
    */
   public String[] getParameterValues(String name) {
      return m_parameters.get(name);
   }

   /**
    * Returns the name and version of the protocol the request uses in the form
    * <i>protocol/majorVersion.minorVersion</i>, for example, HTTP/1.1. For HTTP
    * servlets, the value returned is the same as the value of the CGI variable
    * <code>SERVER_PROTOCOL</code>.
    * 
    * @return a <code>String</code> containing the protocol name and version
    *         number
    * 
    */
   public String getProtocol() {
      return m_protocol;
   }

   /**
    * Retrieves the body of the request as character data using a
    * <code>BufferedReader</code>. The reader translates the character data
    * according to the character encoding used on the body. Either this method
    * or {@link #getInputStream} may be called to read the body, not both.
    * 
    * 
    * @return a <code>BufferedReader</code> containing the body of the request
    * 
    * @exception UnsupportedEncodingException
    *               if the character set encoding used is not supported and the
    *               text cannot be decoded
    * 
    * @exception IllegalStateException
    *               if {@link #getInputStream} method has been called on this
    *               request
    * 
    * @exception IOException
    *               if an input or output exception occurred
    * 
    * @see #getInputStream
    * 
    */
   public BufferedReader getReader() throws IOException {
      throw new IOException("This method is not supported in Java Main mode");
   }

   /**
    * 
    * @deprecated As of Version 2.1 of the Java Servlet API, use
    *             {@link ServletContext#getRealPath} instead.
    * 
    */
   public String getRealPath(String path) {
      return null;
   }

   /**
    * Returns the Internet Protocol (IP) address of the client or last proxy
    * that sent the request. For HTTP servlets, same as the value of the CGI
    * variable <code>REMOTE_ADDR</code>.
    * 
    * @return a <code>String</code> containing the IP address of the client that
    *         sent the request
    * 
    */
   public String getRemoteAddr() {
      return m_remoteAddr; // "127.0.0.1" ;
   }

   /**
    * Returns the fully qualified name of the client or the last proxy that sent
    * the request. If the engine cannot or chooses not to resolve the hostname
    * (to improve performance), this method returns the dotted-string form of
    * the IP address. For HTTP servlets, same as the value of the CGI variable
    * <code>REMOTE_HOST</code>.
    * 
    * @return a <code>String</code> containing the fully qualified name of the
    *         client
    * 
    */
   public String getRemoteHost() {
      return m_remoteHost; // "127.0.0.1" ;
   }

   /**
    * Returns the Internet Protocol (IP) source port of the client or last proxy
    * that sent the request.
    * 
    * @return an integer specifying the port number
    * 
    * @since 2.4
    */
   public int getRemotePort() {
      return m_remotePort;
   }

   /**
    * 
    * Returns a {@link RequestDispatcher} object that acts as a wrapper for the
    * resource located at the given path. A <code>RequestDispatcher</code>
    * object can be used to forward a request to the resource or to include the
    * resource in a response. The resource can be dynamic or static.
    * 
    * <p>
    * The pathname specified may be relative, although it cannot extend outside
    * the current servlet context. If the path begins with a "/" it is
    * interpreted as relative to the current context root. This method returns
    * <code>null</code> if the servlet container cannot return a
    * <code>RequestDispatcher</code>.
    * 
    * <p>
    * The difference between this method and
    * {@link ServletContext#getRequestDispatcher} is that this method can take a
    * relative path.
    * 
    * @param path
    *           a <code>String</code> specifying the pathname to the resource.
    *           If it is relative, it must be relative against the current
    *           servlet.
    * 
    * @return a <code>RequestDispatcher</code> object that acts as a wrapper for
    *         the resource at the specified path, or <code>null</code> if the
    *         servlet container cannot return a <code>RequestDispatcher</code>
    * 
    * @see RequestDispatcher
    * @see ServletContext#getRequestDispatcher
    * 
    */
   public RequestDispatcher getRequestDispatcher(String path) {
      return new RequestDispatcherMock(path);
   }

   /**
    * Returns the name of the scheme used to make this request, for example,
    * <code>http</code>, <code>https</code>, or <code>ftp</code>. Different
    * schemes have different rules for constructing URLs, as noted in RFC 1738.
    * 
    * @return a <code>String</code> containing the name of the scheme used to
    *         make this request
    * 
    */
   public String getScheme() {
      return m_scheme;
   }

   /**
    * Returns the host name of the server to which the request was sent. It is
    * the value of the part before ":" in the <code>Host</code> header value, if
    * any, or the resolved server name, or the server IP address.
    * 
    * @return a <code>String</code> containing the name of the server
    */
   public String getServerName() {
      return m_serverName;
   }

   /**
    * Returns the port number to which the request was sent. It is the value of
    * the part after ":" in the <code>Host</code> header value, if any, or the
    * server port where the client connection was accepted on.
    * 
    * @return an integer specifying the port number
    * 
    */

   public int getServerPort() {
      return m_serverPort;
   }

   /**
    * 
    * Returns a boolean indicating whether this request was made using a secure
    * channel, such as HTTPS.
    * 
    * 
    * @return a boolean indicating if the request was made using a secure
    *         channel
    * 
    */
   public boolean isSecure() {
      return m_secure;
   }

   /**
    * 
    * Removes an attribute from this request. This method is not generally
    * needed as attributes only persist as long as the request is being handled.
    * 
    * <p>
    * Attribute names should follow the same conventions as package names. Names
    * beginning with <code>java.*</code>, <code>javax.*</code>, and
    * <code>com.sun.*</code>, are reserved for use by Sun Microsystems.
    * 
    * 
    * @param name
    *           a <code>String</code> specifying the name of the attribute to
    *           remove
    * 
    */
   public void removeAttribute(String name) {
      m_attributes.remove(name);
   }

   /**
    * 
    * Stores an attribute in this request. Attributes are reset between
    * requests. This method is most often used in conjunction with
    * {@link RequestDispatcher}.
    * 
    * <p>
    * Attribute names should follow the same conventions as package names. Names
    * beginning with <code>java.*</code>, <code>javax.*</code>, and
    * <code>com.sun.*</code>, are reserved for use by Sun Microsystems. <br>
    * If the object passed in is null, the effect is the same as calling
    * {@link #removeAttribute}. <br>
    * It is warned that when the request is dispatched from the servlet resides
    * in a different web application by <code>RequestDispatcher</code>, the
    * object set by this method may not be correctly retrieved in the caller
    * servlet.
    * 
    * 
    * @param name
    *           a <code>String</code> specifying the name of the attribute
    * 
    * @param o
    *           the <code>Object</code> to be stored
    * 
    */
   public void setAttribute(String name, Object value) {
      m_attributes.put(name, value);
   }

   /**
    * Overrides the name of the character encoding used in the body of this
    * request. This method must be called prior to reading request parameters or
    * reading input using getReader().
    * 
    * 
    * @param env
    *           a <code>String</code> containing the name of the character
    *           encoding.
    * @throws java.io.UnsupportedEncodingException
    *            if this is not a valid encoding
    */
   public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
      m_characterEncoding = env;
   }

   public void setContentLength(int contentLength) {
      m_contentLength = contentLength;
   }

   public void setContentType(String contentType) {
      m_contentType = contentType;
   }

   public void setLocalAddr(String localAddr) {
      m_localAddr = localAddr;
   }

   public void setLocale(Locale locale) {
      m_locale = locale;
   }

   public void setLocales(Vector<Locale> locales) {
      m_locales = locales;
   }

   public void setLocalName(String localName) {
      m_localName = localName;
   }

   public void setLocalPort(int localPort) {
      m_localPort = localPort;
   }

   public void setParameter(String name, String value) {
      String[] params = m_parameters.get(name);

      if (params == null) {
         m_parameters.put(name, new String[] { value });
      } else {
         int len = params.length;
         String[] newParams = new String[len + 1];

         System.arraycopy(params, 0, newParams, 0, len);
         newParams[len] = value;
         m_parameters.put(name, newParams);
      }
   }

   public void setProtocol(String protocol) {
      m_protocol = protocol;
   }

   public void setRemoteAddr(String remoteAddr) {
      m_remoteAddr = remoteAddr;
   }

   public void setRemoteHost(String remoteHost) {
      m_remoteHost = remoteHost;
   }

   public void setRemotePort(int remotePort) {
      m_remotePort = remotePort;
   }

   public void setScheme(String scheme) {
      m_scheme = scheme;
   }

   public void setSecure(boolean secure) {
      m_secure = secure;
   }

   public void setServerName(String serverName) {
      m_serverName = serverName;
   }

   public void setServerPort(int serverPort) {
      m_serverPort = serverPort;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder(1024);

      sb.append(getClass().getSimpleName());
      sb.append('[');
      asString(sb);
      sb.append(']');

      return sb.toString();
   }
}
