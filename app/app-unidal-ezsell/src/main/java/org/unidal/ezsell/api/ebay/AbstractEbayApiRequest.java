package org.unidal.ezsell.api.ebay;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RoutedRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpRoute;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.codehaus.plexus.util.IOUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.site.lookup.annotation.Inject;

public abstract class AbstractEbayApiRequest implements Initializable, LogEnabled {
	@Inject
	private String m_gatewayUri;

	@Inject
	private String m_appId;

	@Inject
	private String m_ebayAuthToken;

	@Inject
	private int m_maxRetries = 2;

	private JsonHelper m_jsonHelper = new JsonHelper();

	private Logger m_logger;

	protected JSONObject doCallForJson(RouteInfo info, String callSpecificInputFields) throws HttpException, IOException {
		RoutedRequest request;

		try {
			request = makeJsonRequest(info, callSpecificInputFields);
		} catch (Exception e) {
			throw new RuntimeException("Error when constructing request URL for " + info.getApiName() + " with "
					+ callSpecificInputFields + ".", e);
		}

		HttpClient httpClient = makeHttpClient();
		HttpResponse response = null;
		int retried = 0;

		while (true) {
			try {
				m_logger.info("Invoking request to " + request.getRoute().getTargetHost());
				m_logger.info("Request line: " + request.getRequest().getRequestLine());

				response = httpClient.execute(request);

				m_logger.info("Response status: " + response.getStatusLine());
				break;
			} catch (ConnectException e) {
				m_logger.warn(e.toString() + ", try again.");
				retried++;

				if (retried >= m_maxRetries) {
					throw e;
				}
			}
		}

		HttpEntity entity = response.getEntity();

		if (entity != null) {
			m_logger.info("Content-Length: " + entity.getContentLength() + ", Content-Type: " + entity.getContentType());

			String encoding = "utf-8";

			try {
				JSONObject json = new JSONObject(IOUtil.toString(entity.getContent(), encoding));

				return json;
			} catch (Exception e) {
				throw new RuntimeException("Error when retrieving and parsing response: " + e, e);
			} finally {
				entity.consumeContent();
			}
		}

		return null;
	}

	/**
	 * 
	 <?xml version="1.0" encoding="utf-8"?> <GeteBayOfficialTimeRequest
	 * xmlns="urn:ebay:apis:eBLBaseComponents"> <RequesterCredentials>
	 * <eBayAuthToken> Token goes here </eBayAuthToken> </RequesterCredentials>
	 * <Version>383</Version> </GeteBayOfficialTimeRequest>
	 * 
	 * @param info
	 * @param callSpecificInputFields
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	protected String doCallForXml(RouteInfo info, JSONObject payload) throws HttpException, IOException {
		RoutedRequest request;

		try {
			request = makeXmlRequest(info, payload);
		} catch (Exception e) {
			throw new RuntimeException("Error when constructing request URL for " + info.getApiName() + " with " + payload + ".");
		}

		HttpClient httpClient = makeHttpClient();
		HttpResponse response = null;
		int retried = 0;
		long startTime;

		while (true) {
			startTime = System.currentTimeMillis();

			try {
				m_logger.info("Invoking request to " + request.getRoute().getTargetHost());
				m_logger.info("Request line: " + request.getRequest().getRequestLine());

				response = httpClient.execute(request);

				m_logger.info("Response status: " + response.getStatusLine());
				break;
			} catch (ConnectException e) {
				m_logger.warn(e.toString() + ", try again.");
				retried++;

				if (retried >= m_maxRetries) {
					throw e;
				}
			}
		}

		HttpEntity entity = response.getEntity();

		if (entity != null) {
			m_logger.info("Content-Length: " + entity.getContentLength() + ", Content-Type: " + entity.getContentType());

			String encoding = "utf-8";

			try {
				return IOUtil.toString(entity.getContent(), encoding);
			} catch (Exception e) {
				throw new RuntimeException("Error when retrieving and parsing response: " + e, e);
			} finally {
				entity.consumeContent();

				long endTime = System.currentTimeMillis();
				m_logger.info("Time elapsed: " + (endTime - startTime) + " ms");
			}
		}

		return null;
	}

	public void enableLogging(Logger logger) {
		m_logger = logger;
	}

	public void initialize() throws InitializationException {
		if (m_gatewayUri == null || !m_gatewayUri.contains("ebay.com")) {
			throw new InitializationException("gatewayUri must be set correctly.");
		}
	}

	protected DefaultHttpClient makeHttpClient() {
		DefaultHttpClient client = new DefaultHttpClient();

		client.getParams().setParameter("http.socket.timeout", new Integer(60000));
		return client;
	}

	protected RoutedRequest makeJsonRequest(RouteInfo info, String callSpecificInputFields) throws MalformedURLException,
			URISyntaxException {
		URL url = makeJsonUrl(info, callSpecificInputFields);
		HttpHost targetHost = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
		HttpRoute route = new HttpRoute(targetHost, null, url.getProtocol().toLowerCase().endsWith("s")); // https
		HttpGet get = new HttpGet(url.toURI());
		RoutedRequest.Impl request = new RoutedRequest.Impl(get, route);

		return request;
	}

	protected URL makeJsonUrl(RouteInfo info, String callSpecificInputFields) throws MalformedURLException {
		if (m_appId == null || m_appId.length() < 10) {
			throw new RuntimeException("appId must be set correctly.");
		}

		StringBuilder sb = new StringBuilder(2048);

		sb.append(m_gatewayUri);

		if (m_gatewayUri.indexOf('?') > 0) {
			sb.append('&');
		} else {
			sb.append('?');
		}

		sb.append("responseencoding=JSON");
		sb.append("&appid=").append(m_appId);
		sb.append("&callname=").append(info.getApiName());
		sb.append("&siteid=").append(info.getSiteId());
		sb.append("&version=").append(info.getVersion());

		if (callSpecificInputFields != null) {
			sb.append('&').append(callSpecificInputFields);
		}

		return new URL(sb.toString());
	}

	protected RoutedRequest makeXmlRequest(RouteInfo info, JSONObject payload) throws MalformedURLException, URISyntaxException {
		URL url = new URL(m_gatewayUri);
		HttpHost targetHost = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
		HttpRoute route = new HttpRoute(targetHost, null, url.getProtocol().toLowerCase().endsWith("s")); // https
		HttpPost post = new HttpPost(url.toURI());

		post.setEntity(new XmlRequestEntity(info, payload));

		RoutedRequest.Impl request = new RoutedRequest.Impl(post, route);
		HttpRequest http = request.getRequest();

		http.addHeader("X-EBAY-API-CALL-NAME", info.getApiName());
		http.addHeader("X-EBAY-API-SITEID", String.valueOf(info.getSiteId()));
		http.addHeader("X-EBAY-API-COMPATIBILITY-LEVEL", String.valueOf(info.getVersion()));

		return request;
	}

	protected JSONObject newJSONObject(JSONObject parent, String key) throws JSONException {
		JSONObject child = new JSONObject();

		parent.put(key, child);
		return child;
	}

	public void setAppId(String appId) {
		m_appId = appId;
	}

	public void setEbayAuthToken(String ebayAuthToken) {
		m_ebayAuthToken = ebayAuthToken;
	}

	public void setGatewayUri(String gatewayUri) {
		m_gatewayUri = gatewayUri;
	}

	public void setMaxRetries(int maxRetries) {
		m_maxRetries = maxRetries;
	}

	private static final class JsonHelper {
		@SuppressWarnings("unchecked")
		public void makeXmlContent(StringBuilder sb, JSONObject payload) {
			List<String> list = new ArrayList<String>();
			Iterator<String> keys = payload.keys();

			while (keys.hasNext()) {
				list.add(keys.next());
			}

			Collections.sort(list);

			for (String key : list) {
				Object value = payload.opt(key);

				makeXmlContent(sb, key, value);
			}
		}

		private void makeXmlContent(StringBuilder sb, String key, Object value) {
			if (value instanceof JSONObject) {
				sb.append('<').append(key).append(">\r\n");
				makeXmlContent(sb, (JSONObject) value);
				sb.append("</").append(key).append(">\r\n");
			} else if (value instanceof JSONArray) {
				JSONArray array = (JSONArray) value;

				for (int i = 0; i < array.length(); i++) {
					Object child = array.opt(i);

					makeXmlContent(sb, key, child);
				}
			} else if (value == null) {
				sb.append('<').append(key).append("/>\r\n");
			} else {
				sb.append('<').append(key).append(">").append(value).append("</").append(key).append(">\r\n");
			}
		}

	}

	protected static class RouteInfo {
		private String m_apiName;

		private int m_siteId;

		private int m_version;

		public RouteInfo(String apiName, int siteId, int version) {
			m_apiName = apiName;
			m_siteId = siteId;
			m_version = version;
		}

		public String getApiName() {
			return m_apiName;
		}

		public int getSiteId() {
			return m_siteId;
		}

		public int getVersion() {
			return m_version;
		}
	}

	private class XmlRequestEntity extends AbstractHttpEntity {
		private byte[] m_content;

		public XmlRequestEntity(RouteInfo info, JSONObject payload) {
			String content = makeXmlContent(info, payload);

			try {
				m_content = content.getBytes("utf-8");
			} catch (UnsupportedEncodingException e) {
				m_content = content.getBytes();
			}
		}

		public InputStream getContent() throws IOException, IllegalStateException {
			return new ByteArrayInputStream(m_content);
		}

		public long getContentLength() {
			return m_content.length;
		}

		public boolean isRepeatable() {
			return true;
		}

		public boolean isStreaming() {
			return false;
		}

		private String makeXmlContent(RouteInfo info, JSONObject payload) {
			StringBuilder sb = new StringBuilder(2048);

			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
			sb.append('<').append(info.getApiName()).append("Request xmlns=\"urn:ebay:apis:eBLBaseComponents\">\r\n");
			sb.append("<RequesterCredentials>\r\n");
			sb.append("   <eBayAuthToken>").append(m_ebayAuthToken).append("</eBayAuthToken>\r\n");
			sb.append("</RequesterCredentials>\r\n");
			sb.append("<Version>").append(info.getVersion()).append("</Version>\r\n");

			m_jsonHelper.makeXmlContent(sb, payload);

			sb.append("</").append(info.getApiName()).append("Request>\r\n");

			return sb.toString();
		}

		public void writeTo(OutputStream out) throws IOException {
			out.write(m_content);
		}
	}
}
