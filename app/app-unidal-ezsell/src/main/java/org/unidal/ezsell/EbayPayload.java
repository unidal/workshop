package org.unidal.ezsell;

import com.site.web.mvc.ActionContext;
import com.site.web.mvc.ActionPayload;
import com.site.web.mvc.NormalAction;
import com.site.web.mvc.payload.annotation.FieldMeta;

public class EbayPayload implements ActionPayload<EbayPage, NormalAction> {
	private EbayPage m_page;

	@FieldMeta("op")
	private NormalAction m_action = NormalAction.NONE;

	@FieldMeta("lastUrl")
	private String m_lastUrl;

	public NormalAction getAction() {
		return m_action;
	}

	public String getLastUrl() {
		return m_lastUrl;
	}

	public EbayPage getPage() {
		return m_page;
	}

	public void setAction(String name) {
		m_action = NormalAction.getByName(name, NormalAction.NONE);
	}

	public void setLastUrl(String lastUrl) {
		m_lastUrl = lastUrl;
	}

	public void setNextPage(EbayPage page) {
		m_page = page;
	}

	public void setPage(String action) {
		m_page = EbayPage.getByName(action, EbayPage.HOME);
	}

	public void validate(ActionContext<?> ctx) {
	}
}
