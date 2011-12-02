package org.unidal.expense.biz;

import com.site.web.mvc.Page;

public enum ExpensePage implements Page {
	HOME("home", "Home", true),
	
	TRIP("trip", "Trip", true),
	
	SIGNIN("signin", "Signin", false),
	
	MEMBER("member", "Member", true),

	;

	private String m_name;

	private String m_description;

	private boolean m_realPage;

	private ExpensePage(String name, String description, boolean realPage) {
		m_name = name;
		m_description = description;
		m_realPage = realPage;
	}

	public static ExpensePage getByName(String name, ExpensePage defaultPage) {
		for (ExpensePage action : ExpensePage.values()) {
			if (action.getName().equals(name)) {
				return action;
			}
		}

		return defaultPage;
	}

	public String getName() {
		return m_name;
	}

	public String getDescription() {
		return m_description;
	}

	public boolean isRealPage() {
		return m_realPage;
	}

	public ExpensePage[] getValues() {
		return ExpensePage.values();
	}
}
