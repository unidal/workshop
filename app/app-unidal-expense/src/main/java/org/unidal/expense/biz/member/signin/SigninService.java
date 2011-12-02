package org.unidal.expense.biz.member.signin;

import org.unidal.signin.ISigninService;

import com.site.lookup.annotation.Inject;

public class SigninService implements ISigninService<SigninContext, Credential, Session> {
	private static final String TOKEN = "token";

	@Inject
	private TokenManager m_tokenManager;

	@Inject
	private SessionManager m_sessionManager;

	@Override
	public Session signin(SigninContext ctx, Credential credential) {
		Token token = m_sessionManager.authenticate(credential);

		if (token != null) {
			Session session = m_sessionManager.validate(token);

			if (session != null) {
				m_tokenManager.setToken(ctx, token);
			}

			return session;
		} else {
			return null;
		}
	}

	@Override
	public void signout(SigninContext ctx) {
		m_tokenManager.removeToken(ctx, TOKEN);
	}

	@Override
	public Session validate(SigninContext ctx) {
		Token token = m_tokenManager.getToken(ctx, TOKEN);

		if (token != null) {
			Session session = m_sessionManager.validate(token);

			return session;
		} else {
			return null;
		}
	}
}
