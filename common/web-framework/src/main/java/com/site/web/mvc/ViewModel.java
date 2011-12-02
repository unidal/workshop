package com.site.web.mvc;

import com.site.web.mvc.lifecycle.RequestContext;

public abstract class ViewModel<P extends Page, A extends Action, M extends ActionContext<?>> {
	private M m_actionContext;

	private P m_page;

	private A m_action;

	public ViewModel(M actionContext) {
		m_actionContext = actionContext;
	}

	private String buildPageUri(String action, String pathInfo) {
		RequestContext requestContext = m_actionContext.getRequestContext();

		return requestContext.getActionUri(action, pathInfo);
	}

	public A getAction() {
		return m_action;
	}

	public M getActionContext() {
		return m_actionContext;
	}

	public abstract A getDefaultAction();

	public String getModuleUri() {
		return buildPageUri(null, null);
	}

	public P getPage() {
		return m_page;
	}

	public String getPageUri() {
		return buildPageUri(m_page.getName(), null);
	}

	public String getWebapp() {
		return m_actionContext.getHttpServletRequest().getContextPath();
	}

	public void setAction(A action) {
		m_action = action;
	}

	public void setPage(P action) {
		m_page = action;
	}
}
