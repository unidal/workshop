package com.site.app.tracking;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.logging.Logger;

import com.site.app.tracking.analysis.Model;
import com.site.app.tracking.analysis.Payload;
import com.site.app.tracking.analysis.PayloadProvider;
import com.site.app.tracking.analysis.Processor;
import com.site.app.tracking.analysis.Viewer;
import com.site.lookup.ContainerLoader;

public class AnalysisServlet extends HttpServlet {
	private static final long serialVersionUID = 2862627854543806343L;

	private PayloadProvider m_provider;

	private Processor m_processor;

	private Viewer m_viewer;

	private Logger m_logger;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		try {
			PlexusContainer container = ContainerLoader.getDefaultContainer();

			// set up Logger
			m_logger = ((DefaultPlexusContainer) container).getLoggerManager().getLoggerForComponent(getClass().getName());

			m_provider = (PayloadProvider) container.lookup(PayloadProvider.class);
			m_processor = (Processor) container.lookup(Processor.class);
			m_viewer = (Viewer) container.lookup(Viewer.class);
		} catch (Exception e) {
			if (m_logger != null) {
				m_logger.error("Servlet initializing failed. " + e, e);
			} else {
				System.err.println("Servlet initializing failed. " + e);
				e.printStackTrace();
			}

			throw new ServletException("Servlet initializing failed. " + e, e);
		}
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			Payload payload = m_provider.getPayload(req);

			if (payload != null) {
				Model model = m_processor.process(payload);

				m_viewer.view(req, res, payload, model);
			} else {
				res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad analytics request!");
			}
		} catch (Exception e) {
			m_logger.error("Error when handling request. " + e, e);

			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error!");
		}
	}
}
