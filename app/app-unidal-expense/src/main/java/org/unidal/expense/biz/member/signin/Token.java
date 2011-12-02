package org.unidal.expense.biz.member.signin;

import org.unidal.signin.IToken;

public class Token implements IToken {
	private int m_adminId;

	private int m_memberId;

	public Token(int memberId) {
		m_memberId = memberId;
	}

	public Token(int adminId, int memberId) {
		m_adminId = adminId;
		m_memberId = memberId;
	}

	public int getAdminId() {
		return m_adminId;
	}

	public int getMemberId() {
		return m_memberId;
	}

	@Override
	public String getName() {
		return "token";
	}
}
