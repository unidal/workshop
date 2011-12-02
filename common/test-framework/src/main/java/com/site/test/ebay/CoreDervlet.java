package com.site.test.ebay;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class CoreDervlet extends HttpServlet implements IDervlet {	
	private final String m_name;
	private final String m_path;
	protected final Map<String,String> m_defaultUrlParameters = new HashMap<String,String>(5);
	private EmbeddedServer m_server ;
//	private IIndenter m_indenter = IIndenter.COMPACT;
//	private DapHandler m_apmHandler = null;
	private String m_defaultText = "hello from Dervlet";
	private boolean m_isHttpsRequired=false;
	
	public CoreDervlet() {
		this(null, (String)null);
	}
	
	public CoreDervlet(final String name) {
		this(name, (String)null);
	}
	
	public CoreDervlet(final String name, final Map<String,String> defaultUrlParameters) {
		this(name);
		m_defaultUrlParameters.putAll(defaultUrlParameters);
	}
	
	public CoreDervlet(final String name, final String path) {
		if (name == null) {
			m_name = getClass().getSimpleName();
		}
		else {
			m_name = name;
		}
		if (path == null) {
			m_path = "/" + m_name;
		}
		else {
			m_path = path;
		}
//		String host = EmbeddedServer.getLocalHostName(80);
//		ExeMode dapMode = ExeMode.WEB;
//		String dapEnv = System.getProperty("dapMode");
//		if ("A".equalsIgnoreCase(dapEnv)) {
//			dapMode = ExeMode.ACTIVE;
//		}
//		else if ("T".equalsIgnoreCase(dapEnv)) {
//			dapMode = ExeMode.TRANSLATE;
//		}
//		m_apmHandler = new DapHandler(host, dapMode);
	}
	
	public CoreDervlet(final String name, final String path, final Map<String,String> defaultUrlParameters) {
		this(name, path);
		m_defaultUrlParameters.putAll(defaultUrlParameters);
	}
	
	//
	// API
	//
	public String getPath() {
		return m_path;
	}
	@Override
	public String getServletName(){
		return m_name;
	}

	public EmbeddedServer getServer() {
		return m_server ;
	}
	
	public Map<String,String> getDefaultUrlParameters() {
		return m_defaultUrlParameters;
	}
	
//	public void setIndenter(IIndenter indenter) {
//		m_indenter = indenter;
//	}
	
	/**
	 * Executes this dervlet as a standalone web application
	 */
	public void runAsServer() {
		final EmbeddedServer server = EmbeddedServer.create();
		server.addDervlet(this);
		server.start();
	}

	public String getDervletUrl(){
		final String baseUrl = m_isHttpsRequired ?
			m_server.getBaseSecureUrl() : m_server.getBaseUrl();
		final String url = baseUrl + getDervletUri();
		return url;
	}
	
	/**
	 * Retrives an entry point for the Dervlet based on either the mapping
	 * of default parameters or the Dervlet name
	 */
	public String getDervletUri() {
		String dervletUri;
		if(m_defaultUrlParameters == null || m_defaultUrlParameters.isEmpty()) {
			dervletUri = "/" + getServletName();
		}
		else {
			dervletUri = "/" + getServletName() + "?";
			boolean firstParam = true;
			for(String key : getDefaultUrlParameters().keySet()) {
				if(!firstParam) {
					dervletUri += "&";
				}
				else {
					firstParam = false;
				}
				dervletUri += key + "=" 
					+ getDefaultUrlParameters().get(key);
			}
		}
		return dervletUri;
	}
	
	
	//
	// Framework
	//
	public void setServer(EmbeddedServer server) {
		m_server = server ;
	}
	
	@Override
	protected void doGet(
		final HttpServletRequest request,
		final HttpServletResponse response)
			throws ServletException, IOException
	{
		doRequest(request, response);
	}

	@Override
	protected void doPost(
		final HttpServletRequest request,
		final HttpServletResponse response)
			throws ServletException, IOException
	{
		doRequest(request, response);
	}
	
//	protected DNode createFragment() {
//		final DBody body = new DBody();
//		body.add("default fraglet text, servlet name:" + m_name);
//		return body;
//	}
//	
//	@SuppressWarnings("unused")
//	protected DNode createFragment(final HttpServletRequest request) {
//		return createFragment() ;
//	}
	
	protected void doRequest(
		final HttpServletRequest request,
		final HttpServletResponse response)
			throws ServletException, IOException
	{
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		final OutputStreamWriter writer 
			= createOutputStreamWriter(response.getOutputStream());
		writer.write(m_defaultText);
		writer.flush();
	}

//	protected void displayFragment(
//		final DNode node,
//		final HttpServletResponse response)
//			throws IOException
//	{
//		final DHtmlDocument doc =
//			NodeToDHtmlDocument.createHtmlDocumentContaining(node, false);
//		if (!(node instanceof DHtmlDocument)) {
//			new DocProcessor(ProductionCtx.ctx().getPlan()).process(doc);
//		}
//		
//		m_apmHandler.handleResponse(doc, response, m_indenter);
//	}

	protected CoreDervlet getDervlet(String name) {
		EmbeddedServer server = getServer() ; 
		if (server == null) {
			throw new RuntimeException("Dervlet is not running in a server yet") ;
		}
		CoreDervlet answer = server.getDervlet(name) ;
		return answer ;
	}
	
	protected String getDervletPath(String name) {
		return getDervlet(name).getPath() ;
	}
	
	//
	// Override(s) from Object
	//
	@Override
	public String toString() {
		final String NL = System.getProperty("line.separator") ;
		final StringBuilder buf = new StringBuilder();
		buf.append("Name").append(": ").append(m_name).append(NL);
		buf.append("Path").append(": ").append(m_path).append(NL);
		buf.append("SECURE_TRANSPORT").append(": ").append(m_path).append(NL);
		return buf.toString();
	}
	
	//
	// Private
	//
	protected OutputStreamWriter createOutputStreamWriter(
		final OutputStream outputStream)
	{
		final OutputStreamWriter writer;
		try {
			writer = new OutputStreamWriter(outputStream, "utf-8");
			return writer;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
//	@SuppressWarnings("unchecked")
//	protected void setupDarwinCtx(HttpServletRequest request){
//		ProcessingCtx.reset();
//		
//		DarwinRequestInfo reqInfo = new DarwinRequestInfo();
//		reqInfo.setServerName(request.getServerName());
//		reqInfo.setServerPort(request.getServerPort());
//		reqInfo.setRequestUri(request.getRequestURI());
//		reqInfo.setQueryString(request.getQueryString());
//		reqInfo.setProtocal(request.getProtocol());
//		reqInfo.setCommandName(getCommandName(request));
//		
//		DefaultInputDataProvider inputProvider = new DefaultInputDataProvider(request.getParameterMap());
//		
//		DarwinCtx darwinCtx = new DarwinCtx();
//		darwinCtx.setRequestInfo( reqInfo );
//		darwinCtx.setInputDataProvider( inputProvider );
//		
//		DsfCtx.ctx().setAppCtx( darwinCtx );
//		m_apmHandler.handleRequest(request, JsRuntimeCtx.ctx().getServiceEngine());
//	}
	
	protected String getCommandName(HttpServletRequest request){
		String uri = request.getRequestURI();
		int index = uri.indexOf("?");
		if (index > 0){
			uri = uri.substring(0, index);
		}
		index = uri.lastIndexOf("/");
		String cmdName = uri.substring(index+1, uri.length());
		return cmdName;
	}

	public String getDefaultText() {
		return m_defaultText;
	}

	public void setDefaultText(String defaultText) {
		m_defaultText = defaultText;
	}
}
