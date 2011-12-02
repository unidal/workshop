package org.unidal.xml.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.unidal.xml.XmlContext;
import org.unidal.xml.XmlModel;
import org.unidal.xml.XmlPayload;

public class XmlJspViewer implements XmlViewer {
	public void view(XmlContext<?> ctx, XmlModel model) throws ServletException, IOException {
		HttpServletRequest req = ctx.getHttpServletRequest();
		HttpServletResponse res = ctx.getHttpServletResponse();
		XmlPayload payload = (XmlPayload) ctx.getPayload();

		req.setAttribute("payload", payload);
		req.setAttribute("model", model);

		switch (model.getPage()) {
		case SOURCE:
			req.getRequestDispatcher(XmlJspFile.SOURCE.getPath()).forward(req, res);
			break;
		case MAPPING:
			req.getRequestDispatcher(XmlJspFile.MAPPING.getPath()).forward(req, res);
			break;
		case DOWNLOAD:
			req.getRequestDispatcher(XmlJspFile.DOWNLOAD.getPath()).forward(req, res);
			break;
		}
	}
}
