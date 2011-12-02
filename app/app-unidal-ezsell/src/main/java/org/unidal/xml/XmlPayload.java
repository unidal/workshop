package org.unidal.xml;

import com.site.web.mvc.ActionContext;
import com.site.web.mvc.ActionPayload;
import com.site.web.mvc.NormalAction;
import com.site.web.mvc.payload.annotation.FieldMeta;

public class XmlPayload implements ActionPayload<XmlPage, NormalAction> {
	private XmlPage m_page;

	@FieldMeta("op")
	private NormalAction m_action = NormalAction.NONE;

	@FieldMeta("content")
	private String m_content;

	@FieldMeta("namespace")
	private String[] m_namespaces;

	@FieldMeta("package")
	private String[] m_packages;

	@FieldMeta("previous")
	private boolean m_previous;

	@FieldMeta("next")
	private boolean m_next;

	@FieldMeta("download")
	private boolean m_download;

	@FieldMeta("downloadJar")
	private boolean m_downloadJar;

	public XmlPage getPage() {
		return m_page;
	}

	public String getContent() {
		return m_content;
	}

	public String[] getNamespaces() {
		return m_namespaces;
	}

	public String[] getPackages() {
		return m_packages;
	}

	public boolean isDownload() {
		return m_download;
	}

	public boolean isDownloadJar() {
		return m_downloadJar;
	}

	public boolean isNext() {
		return m_next;
	}

	public boolean isPrevious() {
		return m_previous;
	}

	public void setPage(String action) {
		m_page = XmlPage.getByName(action, XmlPage.SOURCE);
	}

	public void setContent(String content) {
		m_content = content;
	}

	public void setDownload(String download) {
		m_download = download != null;
	}

	public void setDownloadJar(String downloadJar) {
		m_downloadJar = downloadJar != null;
	}

	public void setNamespaces(String[] namespaces) {
		m_namespaces = namespaces;
	}

	public void setNext(String next) {
		m_next = next != null;
	}

	public void setPackages(String[] packages) {
		m_packages = packages;
	}

	public void setPrevious(String previous) {
		m_previous = previous != null;
	}

	public NormalAction getAction() {
		return m_action;
	}

	public void setAction(String name) {
		m_action = NormalAction.getByName(name, NormalAction.NONE);
	}

	public void validate(ActionContext<?> ctx) {
	}
}
