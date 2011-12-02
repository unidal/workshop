package org.unidal.expense.biz.member.signin;

import org.unidal.expense.dal.Member;
import org.unidal.signin.ISession;

public class Session implements ISession {
	private Member m_member;

	public Session(Member member) {
		m_member = member;
	}

	public Member getMember() {
		return m_member;
	}
 }
