package org.unidal.expense.biz.member;

public enum JspFile {
	PROFILE("/jsp/member/profile.jsp"),

	;

	private String m_path;

	private JspFile(String path) {
		m_path = path;
	}

	public String getPath() {
		return m_path;
	}
}
