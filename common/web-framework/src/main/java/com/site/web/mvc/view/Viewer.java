package com.site.web.mvc.view;

import java.io.IOException;

import javax.servlet.ServletException;

import com.site.web.mvc.Action;
import com.site.web.mvc.ActionContext;
import com.site.web.mvc.Page;
import com.site.web.mvc.ViewModel;

public interface Viewer<P extends Page, A extends Action, S extends ActionContext<?>, T extends ViewModel<P, A, S>> {
	public void view(S ctx, T model) throws ServletException, IOException;
}
