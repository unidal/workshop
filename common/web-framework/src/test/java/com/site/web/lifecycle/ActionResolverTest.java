package com.site.web.lifecycle;

import com.site.lookup.ComponentTestCase;
import com.site.test.mock.HttpServletRequestMock;
import com.site.web.mvc.payload.UrlEncodedParameterProvider;

public class ActionResolverTest extends ComponentTestCase {
	private void assertResolve(ActionResolver resolver, String uri) {
		final int pos = uri.indexOf('?');
		final String pathInfo = pos > 0 ? uri.substring(0, pos) : uri;
		final String queryString = pos > 0 ? uri.substring(pos + 1) : null;
		final String contextPath = null;

		HttpServletRequestMock request = new HttpServletRequestMock() {
			@Override
			public String getContextPath() {
				return contextPath;
			}

			@Override
			public String getServletPath() {
				return null;
			}

			@Override
			public String getPathInfo() {
				return pathInfo;
			}

			@Override
			public String getQueryString() {
				return queryString;
			}
		};

		UrlMapping mapping = resolver.parseUrl(new UrlEncodedParameterProvider(
				request));
		String actualUri = resolver.buildUrl(new UrlEncodedParameterProvider(
				request), mapping);
		String expectedUri = (contextPath == null ? "" : contextPath) + uri;

		assertEquals(expectedUri, actualUri);
	}

	public void notestDefault() throws Exception {
		ActionResolver resolver = lookup(ActionResolver.class);

		assertResolve(resolver, "/");
		assertResolve(resolver, "/book");
		assertResolve(resolver, "/book/");
		assertResolve(resolver, "/book/add?op=add");
		assertResolve(resolver, "/book/add/");
		assertResolve(resolver, "/book/add/1");
		assertResolve(resolver, "/book/add/name/");
		assertResolve(resolver, "/book/add/1/name?op=submit");
	}
}
