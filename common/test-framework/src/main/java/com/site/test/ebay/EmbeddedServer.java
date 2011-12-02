package com.site.test.ebay;

import java.io.IOException;
import java.io.Writer;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.ErrorHandler;
import org.mortbay.jetty.security.SslSocketConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.servlet.ServletMapping;


/**
 * This embeded server can be used for testing env. It comes with a predefined
 * /admin servlet to show some server configures and stop the server instance.
 * 
 * It creates a single application context and open the server on a given port.
 * 
 * Any servlet and associated url mapping can be easily added in.
 */
public class EmbeddedServer {
	protected static final String ADMIN_CONTEXT_PATH = "/dervlet-admin";

	private static Map<String, EmbeddedServer> s_serverInstances 
		= new HashMap<String, EmbeddedServer>(1);
	protected EmbeddedServerConfig m_config;
	protected Server m_server;
	protected Context m_appContext;
	protected Context m_adminContext;
	protected Map<String,Servlet> m_nameToServlet = new HashMap<String,Servlet>(5);
//	protected EsfServlet m_esfServlet = new EsfServlet();

	protected static AtomicReference<String> s_localHostName = new AtomicReference<String>();
	
	//
	// Constructor(s)
	//
	protected EmbeddedServer() {
		// empty on purpose
	}

	/**
	 * Answer the EmbeddedServer instance for this port.  A server is not
	 * registered until it is started via start().  If the port is non-positive
	 * an exception is thrown.  If a server is not yet registered for the given
	 * port, one is created.  If the port is already in use, the next 100
	 * port values are attempted.
	 */
	public static EmbeddedServer create() {
		return create(EmbeddedServer.getUniquePort()) ;
	}
	
	public static EmbeddedServer create(int port) {
		return create(port, null, null);
	}

	/**
	 * Answer the EmbeddedServer instance for this port.  A server is not
	 * registered until it is started via start().  If the port is non-positive
	 * an exception is thrown.  If a server is not yet registered for the given
	 * port, one is created.  If the port is already in use, the next 100
	 * port values are attempted.
	 */
	public static EmbeddedServer create(String contextPath, String resourceBase){
		return create(EmbeddedServer.getUniquePort(), contextPath, resourceBase);
	}

	
	public static EmbeddedServer create(
		int port, String contextPath, String resourceBase)
	{
		EmbeddedServerConfig config = new EmbeddedServerConfig(
			port, contextPath, resourceBase);
		return create(config);
	}

	/**
	 * Answer the EmbeddedServer instance for this port.  A server is not
	 * registered until it is started via start().  If the port is non-positive
	 * an exception is thrown.  If a server is not yet registered for the given
	 * port, one is created.  If the port is already in use, the next 100
	 * port values are attempted.
	 */
	public static EmbeddedServer create(EmbeddedServerConfig config) {
		if (config == null) {
			throw new RuntimeException("Config must not be null") ;
		}
		
		EmbeddedServer server = getServer(config.getOriginalPort());
		if (server != null) {
			return server;
		}
		server = new EmbeddedServer();
		server.init(config);
		return server;
	}
	
	public EmbeddedServerConfig getConfig() {
		return m_config;
	}

	//
	// API
	//
	public EmbeddedServer addServletWithMapping(
		final Class<? extends HttpServlet> servletClz, final String pathSpec)
	{
		m_appContext.getServletHandler().addServletWithMapping(servletClz,
				pathSpec);
		return this;
	}

	public EmbeddedServer addDervlet(final CoreDervlet dervlet) {
		return addDervlet(dervlet, (Map<String,String>)null);
	}
	public EmbeddedServer addDervlet(
		final CoreDervlet dervlet,
		final Map<String,String> initParameters)
	{
		return addDervlet(dervlet, dervlet.getServletName(), initParameters) ;
	}

	public EmbeddedServer addDervlet(final CoreDervlet dervlet, final String name) {
		return addDervlet(dervlet, name, null);
	}
	public EmbeddedServer addDervlet(
		final CoreDervlet dervlet,
		final String name,
		final Map<String,String> initParameters)
	{
		addServlet(dervlet, name, dervlet.getPath(), initParameters);
		dervlet.setServer(this);
		return this;
	}

	public EmbeddedServer addServlet(
		final Servlet servlet,
		final String name,
		final String path)
	{
		return addServlet(servlet, name, path, null);
	}
	public EmbeddedServer addFilter(
		final Class<? extends Filter> filterClass,
		final String name,
		final String path,
		final Map<String,String> initParameters)
	{
		final FilterHolder holder = new FilterHolder(filterClass);
//		holder.setInitOrder(1);
		holder.setName(name);
		if (initParameters != null){
			holder.setInitParameters(initParameters);		
		}
		m_appContext.addFilter(holder, path, Handler.ALL);
		return this;		
	}

	public EmbeddedServer addFilter(
		final Filter filter,
		final String name,
		final String path,
		final Map<String,String> initParameters)
	{
		final FilterHolder holder = new FilterHolder(filter);
//		holder.setInitOrder(1);
		holder.setName(name);
		if (initParameters != null){
			holder.setInitParameters(initParameters);		
		}
		m_appContext.addFilter(holder, path, Handler.ALL);
		return this;		
	}

	public EmbeddedServer addServlet(
		final Servlet servlet,
		final String name,
		final String path,
		final Map<String,String> initParameters)
	{
		final ServletHolder servletHolder = new ServletHolder(servlet);
		servletHolder.setInitOrder(1);
		servletHolder.setName(name);
		if (initParameters != null){
			servletHolder.setInitParameters(initParameters);		
		}
		final ServletHandler servletHandler = m_appContext.getServletHandler();
		servletHandler.addServletWithMapping(servletHolder, path);
		m_nameToServlet.put(name, servlet);
		return this;
	}

	public EmbeddedServer addServlet(
		final Class<? extends Servlet> servletClass,
		final String name,
		final String path)
	{
		return addServlet(servletClass, name, path, null);
	}
	public EmbeddedServer addServlet(
		final Class<? extends Servlet> servletClass,
		final String name,
		final String path,
		final Map<String,String> initParameters)
	{
		final ServletHolder servletHolder = new ServletHolder(servletClass);
		servletHolder.setInitOrder(1);
		servletHolder.setName(name);
		if (initParameters != null){
			servletHolder.setInitParameters(initParameters);		
		}
		final ServletHandler servletHandler = m_appContext.getServletHandler();
		servletHandler.addServletWithMapping(servletHolder, path);
		return this;
	}

//	public EmbeddedServer addJs(JsResourceRef jsRef) {
//		JsResource resource = jsRef.getResource();
//		if (!(resource.getHandleProvider() instanceof JsDervlet.JsResourceHandleProvider)) {
//			resource.setHandleProvider(
//				new JsDervlet.JsResourceHandleProvider(this, resource));
//		}
//		return this;
//	}
//
//	public EmbeddedServer addCss(CssResourceRef cssRef) {
//		CssResource resource = cssRef.getResource();
//		if (!(resource.getHandleProvider() instanceof CssDervlet.CssResourceHandleProvider)) {
//			resource.setHandleProvider(
//				new CssDervlet.CssResourceHandleProvider(this, resource));
//		}
//		return this;
//	}

	/** Do not expose mortbay/jetty types.
	 * Answer the ServletHandler for this instance.
	 */
	private ServletHandler getHandler() {
		return m_appContext.getServletHandler();
	}
	public Object getServletContextAttribute(final String attributeName){
		final Object value =
			getHandler().getServletContext().getAttribute(attributeName);
		return value;
	}
	public void setServletContextAttribute(
		final String attributeName, final Object value)
	{
		getHandler().getServletContext().setAttribute(attributeName, value);
	}

	/**
	 * Answer the Dervlet instance for the given name.  Returns null if no
	 * Dervlet found.
	 * Note that this will not return a dervlet that was added via
	 * addServlet(servletClass,	final String name, final String path)
	 * It will only answer to ones that added via addDervlet()'s.
	 */
	public CoreDervlet getDervlet(String name) {
		final Servlet servlet = getServlet(name);
		if (servlet instanceof CoreDervlet){
			return (CoreDervlet)servlet;
		}
		return null;
	}
	public Servlet getServlet(final String name){
		final Servlet servlet = m_nameToServlet.get(name);
		return servlet;
	}
	public EmbeddedServer start() {
		try {
			m_server.start();
			s_serverInstances.put("" + m_config.getOriginalPort(), this);
		} catch (Exception e) {
			throw new RuntimeException("Can't start the server.", e);
		}
		return this;
	}

	/**
	 * Waits a maximum of 10 seconds for the server to start
	 */
	public void waitForStartup() {
		int seconds = 10;
		while(seconds > 0) {
			if(m_server.isRunning()) {
				break;
			}
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				// ignore it
			}
			seconds--;
		}
	}
	
	public EmbeddedServer stop() {
		try {
			m_server.stop();
		} catch (Exception e) {
			// do nothing;
		}
		s_serverInstances.remove(""+m_config.getOriginalPort());
		return this;
	}

	public EmbeddedServer join() {
		try {
			m_server.join();
		} catch (InterruptedException e) {
			// do nothing;
		}
		return this;
	}
	
	/**
	 * used by Bindox runtime
	 * @param error_handler the ErrorHandler object
	 */
	public void setErrorHandler(final EmbeddedServerErrorHandler error_handler) {
		ErrorHandler jetty_error_handler = new ErrorHandler() {
			@Override
			protected void handleErrorPage(HttpServletRequest request, Writer writer, int code, String message) throws IOException {
				error_handler.setRequest(request); // required to implement getThrowable()
		    	if (error_handler.handleError(request, writer, code, message)) {
		    		return;
		    	}
				super.handleErrorPage(request, writer, code, message);
			}
		};
		m_appContext.setErrorHandler(jetty_error_handler);
	}
	
	/** made private so that we do not expose jetty/mortbay types.
	 * @return
	 */
	private Context getAppContext() {
		return m_appContext;
	}

	public static String getAdminUrl(int port) {
		return getBaseUrl(port, false, ADMIN_CONTEXT_PATH);
	}

	public String getAdminUrl() {
		return getBaseUrl(m_config.getPort(), false, ADMIN_CONTEXT_PATH);
	}

	public String getBaseUrl() {
		return getBaseUrl(m_config.getPort());
	}
	
	/**
	 * returns the port value that should be used in URLs
	 * @return
	 */
	public int getPort() {
		return m_config.getPort();
	}
	
	/**
	 * return the same value as getPort() except when setPort() has been called after server creation.
	 * Should only be used if host-port mapping scheme is used.
	 * @return
	 */
	public int getRealServerPort() {
		return m_config.getOriginalPort();
	}

	public String getBaseSecureUrl() {
		return getBaseUrl(m_config.getSslPort(), true, null);
	}

	public static String getBaseUrl(int port) {
		return getBaseUrl(port, false, null);
	}
	
	public static String getBaseUrl(int port, boolean secure, String path) {
		final StringBuilder buf = new StringBuilder(100);
		
		if (secure){
			buf.append("https://");
		} else {
			buf.append("http://");
		}
		buf.append(getLocalHostName(port));
		
		if(path != null) {
			buf.append(path);
		}
		
		return buf.toString();		
	}

	public static String getLocalHostName(int port) {
		return getLocalHostName() + ((port != 80)? (":"+port) : "");		
	}
	
	public static String getLocalHostName() {
		// Note, cannot put InetAddress.getLocalHost() in a static{} initialization block
		// and don't want to use synchronize block here either.
		if (s_localHostName.get() == null) {	
			try {
				s_localHostName.set(InetAddress.getLocalHost().getCanonicalHostName());
			} catch(UnknownHostException e1) {
				s_localHostName.set("localhost");
			}
		}
		return s_localHostName.get();
	}

	/**
	 * only used in Bindox sample running to allow "virtualized servers".
	 * Bindox SXE will set child's embedded server hostname to pNNN.orighostname and will 
	 * map pNNN.orighostname:80 to orighostname:NNN, the the app code still needs to think
	 * it's pNNN.orighostname:80.
	 * 
	 * This is a static method to match the static method getLocalHostName().
	 * It will fail if the host name already been set.
	 * @param name
	 */
	public static void setLocalHostName(String name) {
		while (!s_localHostName.weakCompareAndSet(null, name)) {
			if (s_localHostName.get() != null)
				throw new RuntimeException("EmbeddedServer.setLocalHostName: Can only override local host name once!");
		}
	}
	
	public static String getAdminContextPath() {
		return ADMIN_CONTEXT_PATH;
	}

	public static EmbeddedServer getServer(int port) {
		return s_serverInstances.get("" + port);
	}
	public static interface IServletMapping {
		String getServletName();
		String [] getPathSpecs();
	}
	/** This returns all the servlet mappings for a port.
	 * This will create new objects on each call.
	 * @param port
	 * @return
	 */
	public static IServletMapping[] getServletMapping(int port) {
		EmbeddedServer server = EmbeddedServer.getServer(port);
		if (server == null) {
			return new IServletMapping[0];
		}
		final Context context = server.getAppContext();
		final ServletMapping [] jettyServletMappings =
			context.getServletHandler().getServletMappings();
		if (jettyServletMappings == null || jettyServletMappings.length == 0){
			return new IServletMapping[0];
		}
		final IServletMapping [] servletMappings =
			new IServletMapping[jettyServletMappings.length];
		for (int i=0; i < jettyServletMappings.length; i++){
			final ServletMapping jettyMapping = jettyServletMappings[i];
			final IServletMapping servletMapping = new IServletMapping(){
				public String[] getPathSpecs() {
					return jettyMapping.getPathSpecs();
				}
				public String getServletName() {
					return jettyMapping.getServletName();
				}
			};
			servletMappings[i] = servletMapping;
		}
		return servletMappings;
	}
	
	public static int getUniquePort() {
		String portStr = System.getProperty("server.port");
		int port = -1 ;
		if (portStr == null) {
			// http://www.iana.org/assignments/port-numbers
			// The Dynamic and/or Private Ports are those from 49152 through 65535
			// 65535 - 49152 = 16383
			port = (int) (Math.random() * 16383.0);
			port += 49152 ;
		}
		else {
			port = Integer.parseInt(portStr);
		}
		return port ;
	}

	public static String getContextPath(int port) {
		EmbeddedServer server = EmbeddedServer.getServer(port);
		if (server == null) {
			return "";
		}
		Context context = server.getAppContext();
		return context.getContextPath();
	}

	//
	// Private
	//	

	
	protected int getNextAvailablePort(int initialPort, int tries) {
		int answer = initialPort ;
		for(int i = 1; i <= tries; i++) {
			try {
				ServerSocket s = new ServerSocket(answer) ;
				s.close() ;
				return answer ;
			}
			catch(BindException be) {
				System.err.println("Attempt " + i + " of " + tries 
					+ " failed on port " + answer) ;
				answer++ ;
//				System.out.println("BindException: " + be.getMessage()) ;
			}
			catch(IOException ioe) {
				throw new RuntimeException(ioe.getMessage()) ;
			}
		}
		throw new RuntimeException(
			"*** Unable to get port after " + tries + " tries ***") ;
	}
	
	private void init(EmbeddedServerConfig config) {
		final int portIncrementAttempts = 100;
		
		int port = getNextAvailablePort(config.getPort(), portIncrementAttempts);
		config.resetPort(port);

		m_config = config;

		m_server = new Server(port);
		
		if (config.getSslEnabled() == true) {
			SslSocketConnector sslConnector = new SslSocketConnector();
			sslConnector.setPort(config.getSslPort());
			if (config.getSslKeystore() != null) {
				sslConnector.setKeystore(config.getSslKeystore());
			}
			if (config.getSslPassword() != null) {
				sslConnector.setPassword(config.getSslPassword());
			}
			if (config.getSslKeyPassword() != null) {
				sslConnector.setKeyPassword(config.getSslKeyPassword());
			}
			m_server.addConnector(sslConnector);
		}
		
		m_server.setStopAtShutdown(true);
		m_server.setSendServerVersion(true);

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		m_server.setHandler(contexts);

		m_appContext = new Context(contexts, m_config.getContextPath(),
				Context.SESSIONS);
		
		
		m_appContext.setResourceBase(m_config.getResourceBase());
		ClassLoader cl = m_config.getContextClassLoader();
		m_appContext.setClassLoader((cl != null)? cl :Thread.currentThread().getContextClassLoader());

		// add admin servlet
		m_adminContext = new Context(contexts, ADMIN_CONTEXT_PATH, Context.SESSIONS);
		m_adminContext.setClassLoader(Thread.currentThread().getContextClassLoader());
//		addAdminServletWithMapping(SimpleAdminServlet.class, "/*");
//		
//		// add other servlets to handle other types of requests
//		addServlet(LocalConsoleFrontController.class, "ConsoleFrontController", "/admin/v3console/*");
//		addServlet(m_esfServlet, "esf", "*.jsp");
//		addServlet(AppletResolver.class, "appletCodeBase", "*.class");
//		addServlet(AppletResolver.class, "appletArchive", "*.jar");
//		addServlet(JsResolver.class, "javaScript", "*.js");
//		addServlet(ClasspathResourceDervlet.class, "classpath", "/classpath/*");
//		addServlet(SimpleDsfDervlet.class, "simpledsf", "/simpledsf/*");
	}
	
	public void addAdminServletWithMapping(Class<?> adminServletClass, String path) {
		if (m_adminContext != null) {
			m_adminContext.getServletHandler().addServletWithMapping(adminServletClass, path);
		}
	}
	
//	protected static void chuck(String msg) { 
//		throw new DsfRuntimeException(msg) ;
//	}

//	public EsfServlet getEsfServlet() {
//		return m_esfServlet;
//	}
//
//	public void setEsfServlet(EsfServlet esfServlet) {
//		m_esfServlet = esfServlet;
//	}
}
