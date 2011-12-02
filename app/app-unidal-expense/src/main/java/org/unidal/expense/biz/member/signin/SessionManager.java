package org.unidal.expense.biz.member.signin;

import org.unidal.expense.dal.Member;
import org.unidal.expense.dal.MemberDao;
import org.unidal.expense.dal.MemberEntity;
import org.unidal.signin.ISessionManager;

import com.site.dal.jdbc.DalException;
import com.site.lookup.annotation.Inject;

public class SessionManager implements ISessionManager<Session, Token, Credential> {
	@Inject
	private MemberDao m_memberDao;

	@Override
	public Token authenticate(Credential credential) {
		String account = credential.getAccount();
		String password = credential.getPassword();
		int pos = account.indexOf('\\');

		if (pos > 0) { // piggyback
			String adminAccount = account.substring(0, pos);
			String userAccount = account.substring(pos + 1);

			try {
				Member admin = m_memberDao.authenticate(adminAccount, password, MemberEntity.READSET_FULL);

				if (true) { // TODO is admin?
					Member member = m_memberDao.findByAccount(userAccount, MemberEntity.READSET_FULL);

					return new Token(admin.getId(), member.getId());
				}
			} catch (DalException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Member member = m_memberDao.authenticate(account, password, MemberEntity.READSET_FULL);

				return new Token(member.getId());
			} catch (DalException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override
	public Session validate(Token token) {
		try {
			Member member = m_memberDao.findByPK(token.getMemberId(), MemberEntity.READSET_FULL);

			return new Session(member);
		} catch (DalException e) {
			e.printStackTrace();
		}

		return null;
	}
}
