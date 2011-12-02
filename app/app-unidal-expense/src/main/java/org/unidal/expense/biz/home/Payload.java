package org.unidal.expense.biz.home;

import org.unidal.expense.biz.ExpensePage;

import com.site.web.mvc.ActionContext;
import com.site.web.mvc.ActionPayload;
import com.site.web.mvc.payload.annotation.FieldMeta;

public class Payload implements ActionPayload<ExpensePage, Action> {
	private ExpensePage m_page;

	@FieldMeta("op")
	private Action m_action;

	public void setAction(Action action) {
		m_action = action;
	}

	@Override
	public Action getAction() {
		return m_action;
	}

	@Override
	public ExpensePage getPage() {
		return m_page;
	}

	@Override
	public void setPage(String page) {
		m_page = ExpensePage.getByName(page, ExpensePage.HOME);
	}

	@Override
	public void validate(ActionContext<?> ctx) {
	}
}