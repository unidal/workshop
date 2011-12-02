package org.unidal.expense.biz.member.signin;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.unidal.expense.biz.ExpenseContext;
import org.unidal.expense.biz.ExpensePage;
import org.unidal.expense.dal.Member;

import com.site.app.tag.function.Encoder;
import com.site.lookup.annotation.Inject;
import com.site.web.mvc.ActionContext;
import com.site.web.mvc.ErrorObject;
import com.site.web.mvc.PageHandler;
import com.site.web.mvc.annotation.InboundActionMeta;
import com.site.web.mvc.annotation.OutboundActionMeta;
import com.site.web.mvc.annotation.PayloadMeta;

public class Handler implements PageHandler<Context>, LogEnabled {
	@Inject
	private JspViewer m_jspViewer;

	@Inject
	private SigninService m_signinService;

	private Logger m_logger;

	private SigninContext createSigninContext(Context ctx) {
		return new SigninContext(ctx.getHttpServletRequest(), ctx.getHttpServletResponse());
	}

	public void enableLogging(Logger logger) {
		m_logger = logger;
	}

	@Override
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "signin")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		Payload payload = ctx.getPayload();
		Action action = payload.getAction();

		if (payload.isSubmit() && action.isSignin()) {
			String account = payload.getAccount();
			String password = payload.getPassword();

			if (!isEmpty(account) && !isEmpty(password)) {
				SigninContext sc = createSigninContext(ctx);
				Credential credential = new Credential(account, password);
				Session session = m_signinService.signin(sc, credential);

				if (session == null) {
					ctx.addError(new ErrorObject("biz.login"));
				} else {
					ctx.redirect(payload.getRtnUrl());
					ctx.stopProcess();
					return;
				}
			} else {
				ctx.addError(new ErrorObject("biz.login.input").setArguments("account", account, "password", password));
			}
		} else if (action.isSignout()) {
			SigninContext sc = createSigninContext(ctx);

			m_signinService.signout(sc);
			ctx.redirect(payload.getRtnUrl());
			ctx.stopProcess();
			return;
		} else {
			SigninContext sc = createSigninContext(ctx);
			Session session = m_signinService.validate(sc);

			if (session != null) {
				ActionContext<?> parent = ctx.getParent();

				if (parent instanceof ExpenseContext) {
					ExpenseContext<?> context = (ExpenseContext<?>) parent;
					Member member = session.getMember();

					context.setSigninMember(member);
					logAccess(ctx, member);
					return;
				} else if (parent != null) {
					throw new RuntimeException(String.format("%s should extend %s!", ctx.getClass(), ExpenseContext.class));
				}
			}
		}

		// skip actual action, show sign-in form
		ctx.skipAction();
	}

	@Override
	@OutboundActionMeta(name = "signin")
	public void handleOutbound(Context ctx) throws ServletException, IOException {
		Model model = new Model(ctx);
		Payload payload = ctx.getPayload();

		model.setPage(ExpensePage.SIGNIN);

		if (ctx.getParent() != null && isEmpty(payload.getRtnUrl())) {
			HttpServletRequest request = ctx.getHttpServletRequest();
			String qs = request.getQueryString();
			String requestURI = request.getRequestURI();

			if (qs != null) {
				payload.setRtnUrl(requestURI + "?" + qs);
			} else {
				payload.setRtnUrl(requestURI);
			}
		}

		m_jspViewer.view(ctx, model);
	}

	private boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	@SuppressWarnings("unchecked")
	private void logAccess(Context ctx, Member member) {
		StringBuilder sb = new StringBuilder(256);
		SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
		HttpServletRequest request = ctx.getHttpServletRequest();
		String actionUri = ctx.getRequestContext().getActionUri();

		sb.append(dateFormat.format(new Date()));
		sb.append(" ").append(member.getAccount()).append('/').append(member.getId()).append(' ');

		if (request.getMethod().equalsIgnoreCase("post")) {
			Enumeration<String> names = request.getParameterNames();
			boolean hasQuestion = actionUri.indexOf('?') >= 0;

			sb.append(actionUri);

			while (names.hasMoreElements()) {
				String name = names.nextElement();
				String[] attributes = request.getParameterValues(name);

				for (String attribute : attributes) {
					if (attribute.length() > 0) {
						if (!hasQuestion) {
							sb.append('?');
							hasQuestion = true;
						} else {
							sb.append('&');
						}

						sb.append(name).append('=').append(Encoder.urlEncode(attribute));
					}
				}
			}
		} else {
			sb.append(actionUri);
		}

		m_logger.info(sb.toString());
	}
}