package org.unidal.expense.biz.member;

import org.unidal.expense.biz.ExpensePage;

import com.site.web.mvc.view.BaseJspViewer;

public class JspViewer extends BaseJspViewer<ExpensePage, Action, Context, Model> {
	@Override
	protected String getJspFilePath(Context ctx, Model model) {
		return JspFile.PROFILE.getPath();
	}
}
