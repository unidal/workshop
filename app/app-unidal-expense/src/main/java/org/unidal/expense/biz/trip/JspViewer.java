package org.unidal.expense.biz.trip;

import org.unidal.expense.biz.ExpensePage;

import com.site.web.mvc.view.BaseJspViewer;

public class JspViewer extends BaseJspViewer<ExpensePage, Action, Context, Model> {
	@Override
	protected String getJspFilePath(Context ctx, Model model) {
		Action action = model.getAction();

		switch (action) {
		case LIST:
			return JspFile.LIST.getPath();
		case ADD:
			return JspFile.ADD.getPath();
		case VIEW:
			return JspFile.VIEW.getPath();
		case MEMBER:
			return JspFile.MEMBER.getPath();
		case EXPENSE:
			return JspFile.EXPENSE.getPath();
		}

		throw new RuntimeException("Unknown action: " + action);
	}
}
