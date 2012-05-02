package com.site.dal.jdbc.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.site.dal.jdbc.DalException;
import com.site.dal.jdbc.DataObject;
import com.site.dal.jdbc.QueryDef;
import com.site.dal.jdbc.QueryEngine;
import com.site.dal.jdbc.Readset;
import com.site.dal.jdbc.Updateset;
import com.site.dal.jdbc.entity.EntityInfo;
import com.site.dal.jdbc.entity.EntityInfoManager;
import com.site.dal.jdbc.query.QueryExecutor;
import com.site.dal.jdbc.query.QueryResolver;
import com.site.dal.jdbc.transaction.TransactionManager;
import com.site.lookup.ContainerHolder;

public class DefaultQueryEngine extends ContainerHolder implements QueryEngine {
	private EntityInfoManager m_entityManager;

	private QueryResolver m_queryResolver;

	private QueryExecutor m_queryExecutor;

	private TransactionManager m_transactionManager;

	public static final String HINT_QUERY_TYPE = "QUERY_TYPE";

	public <T extends DataObject> void commitTransaction(QueryDef query, T proto) throws DalException {
		QueryContext ctx = createContext(query, proto);

		try {
			m_transactionManager.commitTransaction(ctx);
		} finally {
			release(ctx);
		}
	}

	protected <T extends DataObject> QueryContext createContext(QueryDef query, T proto) {
		QueryContext ctx = lookup(QueryContext.class);

		try {
			EntityInfo enityInfo = m_entityManager.getEntityInfo(query.getEntityClass());
			Map<String, Object> queryHints = getQueryHints(query, proto);

			ctx.setQuery(query);
			ctx.setProto(proto);
			ctx.setEntityInfo(enityInfo);
			ctx.setQueryHints(queryHints);

			return ctx;
		} finally {
			release(ctx);
		}
	}

	public <T extends DataObject> int[] deleteBatch(QueryDef query, T[] protos) throws DalException {
		if (protos.length == 0) {
			return new int[0];
		}

		QueryContext ctx = createContext(query, protos[0]);

		try {
			m_queryResolver.resolve(ctx);
			return m_queryExecutor.executeUpdateBatch(ctx, protos);
		} finally {
			release(ctx);
		}
	}

	public <T extends DataObject> int deleteSingle(QueryDef query, T proto) throws DalException {
		QueryContext ctx = createContext(query, proto);

		try {
			m_queryResolver.resolve(ctx);
			return m_queryExecutor.executeUpdate(ctx);
		} finally {
			release(ctx);
		}
	}

	protected Map<String, Object> getQueryHints(QueryDef query, DataObject proto) {
		Map<String, Object> hints = proto.getQueryHints();

		if (hints == null) {
			hints = new HashMap<String, Object>(3);
		}

		hints.put(HINT_QUERY_TYPE, query.getType());

		return hints;
	}

	public <T extends DataObject> int[] insertBatch(QueryDef query, T[] protos) throws DalException {
		if (protos.length == 0) {
			return new int[0];
		}

		QueryContext ctx = createContext(query, protos[0]);

		try {
			m_queryResolver.resolve(ctx);
			return m_queryExecutor.executeUpdateBatch(ctx, protos);
		} finally {
			release(ctx);
		}
	}

	public <T extends DataObject> int insertSingle(QueryDef query, T proto) throws DalException {
		QueryContext ctx = createContext(query, proto);

		try {
			m_queryResolver.resolve(ctx);
			return m_queryExecutor.executeUpdate(ctx);
		} finally {
			release(ctx);
		}
	}

	public <T extends DataObject> List<T> queryMultiple(QueryDef query, T proto, Readset<?> readset) throws DalException {
		QueryContext ctx = createContext(query, proto);

		try {
			ctx.setReadset(readset);
			m_queryResolver.resolve(ctx);
			return m_queryExecutor.executeQuery(ctx);
		} finally {
			release(ctx);
		}
	}

	public <T extends DataObject> T querySingle(QueryDef query, T proto, Readset<?> readset) throws DalException {
		QueryContext ctx = createContext(query, proto);

		try {
			ctx.setReadset(readset);
			ctx.setFetchSize(1);
			m_queryResolver.resolve(ctx);

			List<T> results = m_queryExecutor.executeQuery(ctx);

			if (results.isEmpty()) {
				throw new DalException("No record has been found for " + proto);
			} else {
				return results.get(0);
			}
		} finally {
			release(ctx);
		}
	}

	public <T extends DataObject> void rollbackTransaction(QueryDef query, T proto) throws DalException {
		QueryContext ctx = createContext(query, proto);

		try {
			m_transactionManager.rollbackTransaction(ctx);
		} finally {
			release(ctx);
		}
	}

	public <T extends DataObject> void startTransaction(QueryDef query, T proto) throws DalException {
		QueryContext ctx = createContext(query, proto);

		try {
			m_transactionManager.startTransaction(ctx);
		} finally {
			release(ctx);
		}
	}

	public <T extends DataObject> int[] updateBatch(QueryDef query, T[] protos, Updateset<?> updateset)
	      throws DalException {
		if (protos.length == 0) {
			return new int[0];
		}

		QueryContext ctx = createContext(query, protos[0]);

		try {
			ctx.setUpdateset(updateset);
			m_queryResolver.resolve(ctx);
			return m_queryExecutor.executeUpdateBatch(ctx, protos);
		} finally {
			release(ctx);
		}
	}

	public <T extends DataObject> int updateSingle(QueryDef query, T proto, Updateset<?> updateset) throws DalException {
		QueryContext ctx = createContext(query, proto);

		try {
			ctx.setUpdateset(updateset);
			m_queryResolver.resolve(ctx);
			return m_queryExecutor.executeUpdate(ctx);
		} finally {
			release(ctx);
		}
	}
}
