package com.ebay.eunit.mock.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

@SuppressWarnings("deprecation")
public class HttpSessionMock implements HttpSession {

   private int m_maxInactiveInterval = 0;

   private final Map<String, Object> m_attributes = new HashMap<String, Object>();

   private long m_creationTime = System.currentTimeMillis();

   private String m_id;

   private long m_lastAccessedTime = 0;

   private boolean m_new = true;

   private boolean m_isValid = true;

   private ServletContext m_servletContext;

   /**
    * 
    * Returns the object bound with the specified name in this session, or
    * <code>null</code> if no object is bound under the name.
    * 
    * @param name
    *           a string specifying the name of the object
    * 
    * @return the object with the specified name
    * 
    * @exception IllegalStateException
    *               if this method is called on an invalidated session
    * 
    */
   public Object getAttribute(String name) {
      return m_attributes.get(name);
   }

   /**
    * 
    * Returns an <code>Enumeration</code> of <code>String</code> objects
    * containing the names of all the objects bound to this session.
    * 
    * @return an <code>Enumeration</code> of <code>String</code> objects
    *         specifying the names of all the objects bound to this session
    * 
    * @exception IllegalStateException
    *               if this method is called on an invalidated session
    * 
    */
   public Enumeration<String> getAttributeNames() {
      return Collections.enumeration(m_attributes.keySet());
   }

   /**
    * 
    * Returns the time when this session was created, measured in milliseconds
    * since midnight January 1, 1970 GMT.
    * 
    * @return a <code>long</code> specifying when this session was created,
    *         expressed in milliseconds since 1/1/1970 GMT
    * 
    * @exception IllegalStateException
    *               if this method is called on an invalidated session
    * 
    */
   public long getCreationTime() {
      return m_creationTime;
   }

   /**
    * 
    * Returns a string containing the unique identifier assigned to this
    * session. The identifier is assigned by the servlet container and is
    * implementation dependent.
    * 
    * @return a string specifying the identifier assigned to this session
    * 
    * @exception IllegalStateException
    *               if this method is called on an invalidated session
    * 
    */
   public String getId() {
      return m_id; // "v4DummyHttpSessionId" ;
   }

   /**
    * 
    * Returns the last time the client sent a request associated with this
    * session, as the number of milliseconds since midnight January 1, 1970 GMT,
    * and marked by the time the container received the request.
    * 
    * <p>
    * Actions that your application takes, such as getting or setting a value
    * associated with the session, do not affect the access time.
    * 
    * @return a <code>long</code> representing the last time the client sent a
    *         request associated with this session, expressed in milliseconds
    *         since 1/1/1970 GMT
    * 
    * @exception IllegalStateException
    *               if this method is called on an invalidated session
    * 
    */
   public long getLastAccessedTime() {
      return m_lastAccessedTime;
   }

   /**
    * Returns the maximum time interval, in seconds, that the servlet container
    * will keep this session open between client accesses. After this interval,
    * the servlet container will invalidate the session. The maximum time
    * interval can be set with the <code>setMaxInactiveInterval</code> method. A
    * negative time indicates the session should never timeout.
    * 
    * 
    * @return an integer specifying the number of seconds this session remains
    *         open between client requests
    * 
    * @see #setMaxInactiveInterval
    * 
    * 
    */
   public int getMaxInactiveInterval() {
      return m_maxInactiveInterval;
   }

   /**
    * Returns the ServletContext to which this session belongs.
    * 
    * @return The ServletContext object for the web application
    * @since 2.3
    */
   public ServletContext getServletContext() {
      return m_servletContext;
   }

   /**
    * 
    * @deprecated As of Version 2.1, this method is deprecated and has no
    *             replacement. It will be removed in a future version of the
    *             Java Servlet API.
    * 
    */
   public HttpSessionContext getSessionContext() {
      return null;
   }

   /**
    * 
    * @deprecated As of Version 2.2, this method is replaced by
    *             {@link #getAttribute}.
    * 
    * @param name
    *           a string specifying the name of the object
    * 
    * @return the object with the specified name
    * 
    * @exception IllegalStateException
    *               if this method is called on an invalidated session
    * 
    */

   public Object getValue(String name) {
      return getAttribute(name);
   }

   /**
    * 
    * @deprecated As of Version 2.2, this method is replaced by
    *             {@link #getAttributeNames}
    * 
    * @return an array of <code>String</code> objects specifying the names of
    *         all the objects bound to this session
    * 
    * @exception IllegalStateException
    *               if this method is called on an invalidated session
    * 
    */
   public String[] getValueNames() {
      return null;
   }

   /**
    * 
    * Invalidates this session then unbinds any objects bound to it.
    * 
    * @exception IllegalStateException
    *               if this method is called on an already invalidated session
    * 
    */
   public void invalidate() {
      if (!m_isValid) {
         throw new IllegalStateException("Invalidate the invalid session");
      }

      m_attributes.clear();
      m_isValid = false;
   }

   /**
    * 
    * Returns <code>true</code> if the client does not yet know about the
    * session or if the client chooses not to join the session. For example, if
    * the server used only cookie-based sessions, and the client had disabled
    * the use of cookies, then a session would be new on each request.
    * 
    * @return <code>true</code> if the server has created a session, but the
    *         client has not yet joined
    * 
    * @exception IllegalStateException
    *               if this method is called on an already invalidated session
    * 
    */
   public boolean isNew() {
      return m_new;
   }

   /**
    * 
    * @deprecated As of Version 2.2, this method is replaced by
    *             {@link #setAttribute}
    * 
    * @param name
    *           the name to which the object is bound; cannot be null
    * 
    * @param value
    *           the object to be bound; cannot be null
    * 
    * @exception IllegalStateException
    *               if this method is called on an invalidated session
    * 
    */
   public void putValue(String name, Object value) {
      setAttribute(name, value);
   }

   /**
    * 
    * Removes the object bound with the specified name from this session. If the
    * session does not have an object bound with the specified name, this method
    * does nothing.
    * 
    * <p>
    * After this method executes, and if the object implements
    * <code>HttpSessionBindingListener</code>, the container calls
    * <code>HttpSessionBindingListener.valueUnbound</code>. The container then
    * notifies any <code>HttpSessionAttributeListener</code>s in the web
    * application.
    * 
    * 
    * 
    * @param name
    *           the name of the object to remove from this session
    * 
    * @exception IllegalStateException
    *               if this method is called on an invalidated session
    */
   public void removeAttribute(String name) {
      m_attributes.remove(name);
   }

   /**
    * 
    * @deprecated As of Version 2.2, this method is replaced by
    *             {@link #removeAttribute}
    * 
    * @param name
    *           the name of the object to remove from this session
    * 
    * @exception IllegalStateException
    *               if this method is called on an invalidated session
    */
   public void removeValue(String name) {
      removeAttribute(name);
   }

   /**
    * Binds an object to this session, using the name specified. If an object of
    * the same name is already bound to the session, the object is replaced.
    * 
    * <p>
    * After this method executes, and if the new object implements
    * <code>HttpSessionBindingListener</code>, the container calls
    * <code>HttpSessionBindingListener.valueBound</code>. The container then
    * notifies any <code>HttpSessionAttributeListener</code>s in the web
    * application.
    * 
    * <p>
    * If an object was already bound to this session of this name that
    * implements <code>HttpSessionBindingListener</code>, its
    * <code>HttpSessionBindingListener.valueUnbound</code> method is called.
    * 
    * <p>
    * If the value passed in is null, this has the same effect as calling
    * <code>removeAttribute()<code>.
    * 
    * 
    * @param name
    *           the name to which the object is bound; cannot be null
    * 
    * @param value
    *           the object to be bound
    * 
    * @exception IllegalStateException
    *               if this method is called on an invalidated session
    * 
    */
   public void setAttribute(String name, Object value) {
      m_attributes.put(name, value);
   }

   public void setCreationTime(int creationTime) {
      m_creationTime = creationTime;
   }

   public void setId(String id) {
      m_id = id;
   }

   public void setLastAccessedTime(long time) {
      m_lastAccessedTime = time;
   }

   /**
    * 
    * Specifies the time, in seconds, between client requests before the servlet
    * container will invalidate this session. A negative time indicates the
    * session should never timeout.
    * 
    * @param interval
    *           An integer specifying the number of seconds
    * 
    */
   public void setMaxInactiveInterval(int interval) {
      m_maxInactiveInterval = interval;
   }

   public void setNew(boolean isNew) {
      m_new = isNew;
   }

   //
   // Override(s) from Object
   //
   public String toString() {
      StringBuilder sb = new StringBuilder(256);

      sb.append(getClass().getSimpleName()).append('[');
      sb.append("maxInactiveInterval=").append(m_maxInactiveInterval);
      sb.append(", attributes=").append(m_attributes);
      sb.append(", creationTime=").append(m_creationTime);
      sb.append(", sessionId=").append(m_id);
      sb.append(", lastAccessedTime=").append(m_lastAccessedTime);
      sb.append(", isNew=").append(m_new);
      sb.append(']');

      return sb.toString();
   }
}
