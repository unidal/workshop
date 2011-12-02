package org.unidal.expense.biz.member.signin;

import org.unidal.expense.dal.Member;

import com.site.web.mvc.ActionContext;

public class Context extends ActionContext<Payload> {
	private Member m_signinMember;

	public Member getSigninMember() {
		return m_signinMember;
	}

	public void setSigninMember(Member loginMember) {
		m_signinMember = loginMember;
	}
}