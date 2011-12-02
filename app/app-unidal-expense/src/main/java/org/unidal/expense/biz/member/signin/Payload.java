package org.unidal.expense.biz.member.signin;

import org.unidal.expense.biz.ExpensePage;

import com.site.web.mvc.ActionContext;
import com.site.web.mvc.ActionPayload;
import com.site.web.mvc.payload.annotation.FieldMeta;

public class Payload implements ActionPayload<ExpensePage, Action> {
	private ExpensePage m_page;

	@FieldMeta("op")
	private Action m_action;

	@FieldMeta("rtnUrl")
	private String m_rtnUrl;

	@FieldMeta("account")
	private String m_account;

	@FieldMeta("password")
	private String m_password;

	@FieldMeta("login")
	private boolean m_submit;

	public void setAction(String action) {
		m_action = Action.getByName(action, Action.SIGNIN);
	}

	@Override
	public Action getAction() {
		return m_action;
	}

	public String getPassword() {
		return m_password;
	}

	public void setPassword(String password) {
		m_password = password;
	}

	public String getAccount() {
		return m_account;
	}

	public void setAccount(String account) {
		m_account = account;
	}

	@Override
	public ExpensePage getPage() {
		return m_page;
	}

	public String getRtnUrl() {
		return m_rtnUrl;
	}

	public void setRtnUrl(String rtnUrl) {
		m_rtnUrl = rtnUrl;
	}

	public boolean isSubmit() {
		return m_submit;
	}

	public void setSubmit(boolean submit) {
		m_submit = submit;
	}

	@Override
	public void setPage(String page) {
		m_page = ExpensePage.getByName(page, ExpensePage.SIGNIN);
	}

	@Override
	public void validate(ActionContext<?> ctx) {
		if (m_action == null) {
			m_action = Action.SIGNIN;
		}
	}
}