package org.unidal.expense.biz.member.signin;

public enum JspFile {
	SIGNIN("/jsp/member/signin.jsp"),

	;

	private String m_path;

	private JspFile(String path) {
		m_path = path;
	}

	public String getPath() {
		return m_path;
	}
}
