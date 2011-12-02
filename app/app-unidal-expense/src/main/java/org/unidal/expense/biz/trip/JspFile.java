package org.unidal.expense.biz.trip;

public enum JspFile {
	LIST("/jsp/trip/list.jsp"),
	
	ADD("/jsp/trip/add.jsp"),

	VIEW("/jsp/trip/view.jsp"),
	
	MEMBER("/jsp/trip/member.jsp"),
	
	EXPENSE("/jsp/trip/expense.jsp"),

	;

	private String m_path;

	private JspFile(String path) {
		m_path = path;
	}

	public String getPath() {
		return m_path;
	}
}
