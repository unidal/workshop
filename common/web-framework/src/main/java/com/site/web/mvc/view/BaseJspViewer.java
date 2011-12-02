package com.site.web.mvc.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.site.web.mvc.Action;
import com.site.web.mvc.ActionContext;
import com.site.web.mvc.Page;
import com.site.web.mvc.ViewModel;

public abstract class BaseJspViewer<P extends Page, A extends Action, S extends ActionContext<?>, T extends ViewModel<P, A, S>>
		implements Viewer<P, A, S, T> {
	public void view(S ctx, T model) throws ServletException, IOException {
		HttpServletRequest req = ctx.getHttpServletRequest();
		HttpServletResponse res = ctx.getHttpServletResponse();

		req.setAttribute("ctx", ctx);
		req.setAttribute("payload", ctx.getPayload());
		req.setAttribute("model", model);

		req.getRequestDispatcher(getJspFilePath(ctx, model)).forward(req, res);
	}

	protected abstract String getJspFilePath(S ctx, T model);
}
