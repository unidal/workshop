package com.site.test.user.dal;

import com.site.dal.jdbc.DalException;
import com.site.dal.jdbc.QueryEngine;
import com.site.dal.jdbc.Readset;

public class UserDao {
	private QueryEngine m_queryEngine;

	public User createLocal() {
		User user = new User();

		return user;
	}

	public boolean delete(User user) throws DalException {
		int rows = m_queryEngine.deleteSingle(UserEntity.DELETE_BY_PK, user);

		return rows > 0;
	}

	public User findByPK(int userId, Readset<User> readset) throws DalException {
		User proto = new User();

		proto.setKeyUserId(userId);

		User user = m_queryEngine.querySingle(UserEntity.FIND_BY_PK, proto,
				readset);

		return user;
	}

	public User findWithSubObjectsByPK(int userId, Readset<User> readset) throws DalException {
		User proto = new User();
		
		proto.setKeyUserId(userId);
		
		User user = m_queryEngine.querySingle(UserEntity.FIND_WITH_SUBOBJECTS_BY_PK, proto,
				readset);
		
		return user;
	}
	
	public boolean insert(User user) throws DalException {
		int rows = m_queryEngine.insertSingle(UserEntity.INSERT, user);

		return rows > 0;
	}
}
