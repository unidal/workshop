package org.unidal.expense.biz.member;

import java.io.IOException;

import javax.servlet.ServletException;

import org.unidal.expense.biz.ExpensePage;

import com.site.lookup.annotation.Inject;
import com.site.web.mvc.PageHandler;
import com.site.web.mvc.annotation.InboundActionMeta;
import com.site.web.mvc.annotation.OutboundActionMeta;
import com.site.web.mvc.annotation.PayloadMeta;
import com.site.web.mvc.annotation.PreInboundActionMeta;

public class Handler implements PageHandler<Context> {
	@Inject
	private JspViewer m_jspViewer;

	@Override
	@PreInboundActionMeta("signin")
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "member")
	public void handleInbound(Context ctx) throws ServletException, IOException {
	
	}

	@Override
	@OutboundActionMeta(name = "member")
	public void handleOutbound(Context ctx) throws ServletException, IOException {
		Model model = new Model(ctx);
		Payload payload = ctx.getPayload();

		model.setPage(ExpensePage.MEMBER);

		if (payload.getAction().isProfile()) {
			model.setAction(Action.PROFILE);
		}

		m_jspViewer.view(ctx, model);
	}
}