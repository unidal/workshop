package org.unidal.expense.biz.member;

import org.unidal.expense.biz.ExpensePage;

import com.site.web.mvc.ViewModel;

public class Model extends ViewModel<ExpensePage, Action, Context> {
	public Model(Context ctx) {
		super(ctx);
	}

	@Override
	public Action getDefaultAction() {
		return Action.PROFILE;
	}
}
