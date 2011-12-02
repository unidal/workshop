package org.unidal.expense.biz.member;

public enum Action implements com.site.web.mvc.Action {
	PROFILE("profile");

	private String m_name;

	private Action(String name) {
		m_name = name;
	}

	public static Action getByName(String name, Action defaultAction) {
		for (Action action : Action.values()) {
			if (action.getName().equals(name)) {
				return action;
			}
		}

		return defaultAction;
	}

	@Override
	public String getName() {
		return m_name;
	}

	public boolean isProfile() {
		return this == PROFILE;
	}
}