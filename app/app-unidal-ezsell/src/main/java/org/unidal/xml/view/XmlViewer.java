package org.unidal.xml.view;

import java.io.IOException;

import javax.servlet.ServletException;

import org.unidal.xml.XmlContext;
import org.unidal.xml.XmlModel;

public interface XmlViewer {
	public void view(XmlContext<?> ctx, XmlModel model) throws ServletException, IOException;
}
