package com.site.web;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dianping.cat.Cat;
import com.site.web.lifecycle.RequestLifecycle;

public class MVC extends AbstractContainerServlet {
	private static final long serialVersionUID = 1L;

	private RequestLifecycle m_handler;

	@Override
	protected void initComponents(ServletConfig config) throws Exception {
		String catClientXml = config.getInitParameter("cat-client-xml");

		Cat.initialize(getContainer(), catClientXml == null ? null : new File(catClientXml));
		m_handler = lookup(RequestLifecycle.class, "mvc");
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		try {
			m_handler.handle(request, response);
		} catch (Throwable t) {
			String message = "Error occured when handling uri: " + request.getRequestURI();

			getLogger().error(message, t);

			if (!response.isCommitted()) {
				response.sendError(500, message);
			}
		}
	}
}
