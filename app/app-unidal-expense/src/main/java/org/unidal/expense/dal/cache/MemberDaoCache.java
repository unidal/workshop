package org.unidal.expense.dal.cache;

import org.unidal.expense.dal.Member;
import org.unidal.expense.dal.MemberDao;

import com.site.dal.jdbc.DalException;
import com.site.dal.jdbc.Readset;
import com.site.dal.jdbc.Updateset;
import com.site.lookup.annotation.Inject;

public class MemberDaoCache extends MemberDao {
	@Inject
	private CacheManager<Integer, Member> m_cacheManager;

	@Override
	public int deleteByPK(Member proto) throws DalException {
		m_cacheManager.evictFromCache(proto.getKeyId());

		return super.deleteByPK(proto);
	}

	@Override
	public Member findByPK(int keyMemberId, Readset<Member> readset) throws DalException {
		Member seller = m_cacheManager.getFromCache(keyMemberId, readset);

		if (seller == null) {
			seller = super.findByPK(keyMemberId, readset);
			m_cacheManager.putToCache(keyMemberId, readset, seller);
		}

		return seller;
	}

	@Override
	public int updateByPK(Member proto, Updateset<Member> updateset) throws DalException {
		m_cacheManager.evictFromCache(proto.getKeyId());

		return super.updateByPK(proto, updateset);
	}
}
