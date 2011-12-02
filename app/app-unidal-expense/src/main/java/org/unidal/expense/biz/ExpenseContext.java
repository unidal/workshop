package org.unidal.expense.biz;

import org.unidal.expense.dal.Member;

import com.site.web.mvc.Action;
import com.site.web.mvc.ActionContext;
import com.site.web.mvc.ActionPayload;
import com.site.web.mvc.Page;

public class ExpenseContext<T extends ActionPayload<? extends Page, ? extends Action>> extends ActionContext<T> {
	private Member m_signinMember;

	public Member getSigninMember() {
		return m_signinMember;
	}

	public void setSigninMember(Member signinMember) {
		m_signinMember = signinMember;
	}
}
