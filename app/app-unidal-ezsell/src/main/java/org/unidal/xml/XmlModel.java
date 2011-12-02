package org.unidal.xml;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.site.web.mvc.Action;
import com.site.web.mvc.ViewModel;

public class XmlModel extends ViewModel<XmlPage, Action, XmlContext<?>> {
	private String m_content;

	private List<String> m_namespaces = new ArrayList<String>();

	private List<String> m_packages = new ArrayList<String>();

	public XmlModel(XmlContext<?> ctx) {
		super(ctx);
	}

	public void addNamespace(String namespace) {
		m_namespaces.add(namespace);
	}

	public void addPackage(String _package) {
		m_packages.add(_package);
	}

	public String getContent() {
		return StringEscapeUtils.escapeHtml(m_content);
	}

	@Override
	public Action getDefaultAction() {
		return null;
	}

	public List<String> getNamespaces() {
		return m_namespaces;
	}

	public List<String> getPackages() {
		return m_packages;
	}

	public void setContent(String content) {
		m_content = content;
	}
}
