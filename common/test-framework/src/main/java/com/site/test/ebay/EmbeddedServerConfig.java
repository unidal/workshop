package com.site.test.ebay;

import java.io.Serializable;

public class EmbeddedServerConfig implements Serializable, Cloneable {
	static final long serialVersionUID = 2080280717058448646L;


	private int m_port ;
	private int m_originalPort;
	private String m_contextPath ;
	private String m_resourceBase ;
	private boolean m_sslEnabled = false;
	private int m_sslPort = 8443;
	private String m_sslKeystore = null;
	private String m_sslPassword = null;
	private boolean m_isProxy = false;
	private String m_sslKeyPassword = null;
	final private ClassLoader	m_classLoader;
	
	//
	// Constructor(s)
	//
	public EmbeddedServerConfig(int port, String contextPath, String resourceBase) {
		this(port, contextPath, resourceBase, null);
	}

	/**
	 * Creates EmbeddedServerConfig config.
	 * @param port
	 * @param contextPath
	 * @param resourceBase
	 * @param classLoader custom class loader to be used for Servelt Context (i.e. to run JSP objects) or null for default.
	 */
	public EmbeddedServerConfig(
		int port, String contextPath, String resourceBase, ClassLoader classLoader)
	{
		if (port < 0) {
			throw new RuntimeException("Port must be a positive integer") ;
		}
		
		m_originalPort = m_port = port ;
		
		if (contextPath == null) {
			m_contextPath = "/" ;
		}
		else { 
			m_contextPath = contextPath ;
		}
		
		if (resourceBase == null) {
			m_resourceBase = "." ;
		}
		else {
			m_resourceBase = resourceBase ;
		}
		m_classLoader = classLoader;
	}

	//
	// API
	//
	/**
	 * returns port value to be used when creating server URLs
	 */
	public int getPort() {
		return m_port ;
	}
	public String getContextPath() {
		return m_contextPath ;
	}
	public String getResourceBase() {
		return m_resourceBase ;
	}

	public boolean isProxy() {
		return m_isProxy;
	}
		
	
	/**
	 * If called after Server creation, makes getPort() return different value from the real server port.
	 * Does not change the value of the real port that the server is using.
	 * This behaviour is used in Bindox sample running to allow port to host mapping.
	 * @param port
	 */
	public EmbeddedServerConfig setPort(int port) {
		m_port = port ;
		return this ;
	}
	
	//
	// Framework only
	//	
	
	/**
	 * set both m_oringinalPort and m_port. The m_port value can be changed for port mapping.
	 * keep this method package private.
	 */
	EmbeddedServerConfig resetPort(int port) {
		m_originalPort = m_port = port ;
		return this ;
	}
	
	/**
	 * the original port that is used for server creation. Should not be changed after server creation.
	 * @return
	 */
	int getOriginalPort() {
		return m_originalPort ;
	}
	
	EmbeddedServerConfig setContextPath(String path) {
		m_contextPath = path ;
		return this ;
	}
	
	EmbeddedServerConfig setResourceBase(String base) {
		m_resourceBase = base ;
		return this ;
	}
		
	ClassLoader getContextClassLoader() {
		return m_classLoader;
	}
	
	//
	// Override(s) from Object
	//
	@Override
	public String toString() {
		final String NL = System.getProperty("line.separator") ;
		final StringBuilder buf = new StringBuilder();
		buf.append("Port").append(": ").append(m_port).append(NL);
		buf.append("Context Path").append(": ").append(m_contextPath).append(NL);
		buf.append("Resource Base").append(": ").append(m_resourceBase).append(NL);
		return buf.toString();
	}
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false ;
		}
		if (o instanceof EmbeddedServerConfig == false) {
			return false ;
		}
		
		final EmbeddedServerConfig other = (EmbeddedServerConfig)o ;
		
		// We must have a unique port so this is our best "key"
		if (this.getPort() != other.getPort()) {
			return false ;
		}

//		if (this.getContextPath().equals(other.getContextPath()) == false) {
//			return false ;
//		}
//		
//		if (this.getResourceBase().equals(other.getResourceBase()) == false) {
//			return false ;
//		}

		return true ;
	}
	
	//
	// Private
	//
	public int getSslPort() {
		return m_sslPort;
	}

	public void setSslPort(int port) {
		m_sslPort = port;
	}

	public String getSslKeystore() {
		return m_sslKeystore;
	}

	public void setSslKeystore(String keystore) {
		m_sslKeystore = keystore;
	}

	public String getSslPassword() {
		return m_sslPassword;
	}

	public void setSslPassword(String password) {
		m_sslPassword = password;
	}
	
	public void setSslEnabled(boolean flag) {
		m_sslEnabled = flag;
	}
	
	public boolean getSslEnabled() {
		return m_sslEnabled;
	}

	public String getSslKeyPassword() {
		return m_sslKeyPassword;
	}

	public void setSslKeyPassword(String password) {
		m_sslKeyPassword = password;
	}
}
