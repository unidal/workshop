package org.unidal.expense.biz.trip;

public enum Action implements com.site.web.mvc.Action {
	LIST("list"),

	ADD("add"),

	VIEW("view"),

	EDIT("edit"),

	MEMBER("member"),

	EXPENSE("expense"),

	;

	private String m_name;

	public static Action getByName(String name, Action defaultAction) {
		for (Action action : Action.values()) {
			if (action.getName().equals(name)) {
				return action;
			}
		}

		return defaultAction;
	}

	private Action(String name) {
		m_name = name;
	}

	@Override
	public String getName() {
		return m_name;
	}

	public boolean isAdd() {
		return this == ADD;
	}

	public boolean isEdit() {
		return this == EDIT;
	}

	public boolean isList() {
		return this == LIST;
	}

	public boolean isMember() {
		return this == MEMBER;
	}
	
	public boolean isExpense() {
		return this == EXPENSE;
	}

	public boolean isView() {
		return this == VIEW;
	}
}