package com.site.dal.jdbc.query;

import java.lang.reflect.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.dianping.cat.message.Message;
import com.dianping.cat.message.MessageProducer;
import com.dianping.cat.message.Transaction;
import com.site.dal.jdbc.DalException;
import com.site.dal.jdbc.DalRuntimeException;
import com.site.dal.jdbc.DataField;
import com.site.dal.jdbc.DataObject;
import com.site.dal.jdbc.QueryDef;
import com.site.dal.jdbc.QueryType;
import com.site.dal.jdbc.annotation.Attribute;
import com.site.dal.jdbc.datasource.DataSourceManager;
import com.site.dal.jdbc.datasource.JdbcDataSourceConfiguration;
import com.site.dal.jdbc.engine.QueryContext;
import com.site.dal.jdbc.entity.DataObjectAccessor;
import com.site.dal.jdbc.entity.DataObjectAssembly;
import com.site.dal.jdbc.entity.EntityInfo;
import com.site.dal.jdbc.transaction.TransactionManager;
import com.site.lookup.annotation.Inject;

public class DefaultQueryExecutor implements QueryExecutor {
	@Inject
	private DataObjectAssembly m_assembly;

	@Inject
	private DataObjectAccessor m_accessor;

	@Inject
	private TransactionManager m_transactionManager;

	@Inject
	private DataSourceManager m_dataSourceManager;

	@Inject
	private MessageProducer m_cat;

	protected PreparedStatement createPreparedStatement(QueryContext ctx) throws SQLException {
		Connection conn = m_transactionManager.getConnection(ctx);
		QueryDef query = ctx.getQuery();
		QueryType type = query.getType();
		PreparedStatement ps;

		if (type == QueryType.SELECT) {
			if (query.isStoreProcedure()) {
				ps = conn.prepareCall(ctx.getSqlStatement(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			} else {
				ps = conn.prepareStatement(ctx.getSqlStatement(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			}
		} else {
			ps = conn.prepareStatement(ctx.getSqlStatement(), PreparedStatement.RETURN_GENERATED_KEYS);
		}

		return ps;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends DataObject> List<T> executeQuery(QueryContext ctx) throws DalException {
		Transaction t = m_cat.newTransaction("SQL", getQueryName(ctx));
		T proto = (T) ctx.getProto();
		PreparedStatement ps = null;

		t.addData(ctx.getSqlStatement());

		try {
			ps = createPreparedStatement(ctx);

			logCatEvent(ctx);

			// Set fetch size if have
			if (ctx.getFetchSize() > 0) {
				ps.setFetchSize(ctx.getFetchSize());
			}

			// Setup IN/OUT parameters
			setupInOutParameters(ps, ctx.getParameters(), proto);

			// Execute the query
			ResultSet rs = ps.executeQuery();

			// Fetch all rows
			List<T> rows = m_assembly.assemble(ctx, rs);

			// Get OUT parameters if have
			retrieveOutParameters(ps, ctx.getParameters(), proto);

			t.setStatus(Transaction.SUCCESS);
			return rows;
		} catch (SQLException e) {
			t.setStatus(e.getClass().getSimpleName());
			m_cat.logError(e);
			throw new DalException("Error when executing query(" + ctx.getSqlStatement() + ") failed, Proto: " + proto
			      + ", message: " + e, e);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					throw new DalRuntimeException("Error when closing PreparedStatement, message: " + e, e);
				}
			}

			m_transactionManager.closeConnection();
			t.complete();
		}
	}

	@Override
	public int executeUpdate(QueryContext ctx) throws DalException {
		Transaction t = m_cat.newTransaction("SQL", getQueryName(ctx));
		DataObject proto = ctx.getProto();
		PreparedStatement ps = null;

		t.addData(ctx.getSqlStatement());

		try {
			ps = createPreparedStatement(ctx);

			logCatEvent(ctx);

			// Call beforeSave() to do some custom data manipulation
			proto.beforeSave();

			// Setup IN/OUT parameters
			setupInOutParameters(ps, ctx.getParameters(), proto);

			// Execute the query
			int rowCount = ps.executeUpdate();

			// Get OUT parameters if have
			retrieveOutParameters(ps, ctx.getParameters(), proto);

			// Retrieve Generated Keys if have
			if (ctx.getQuery().getType() == QueryType.INSERT) {
				retrieveGeneratedKeys(ctx, ps, proto);
			}

			t.setStatus(Transaction.SUCCESS);
			return rowCount;
		} catch (SQLException e) {
			t.setStatus(e.getClass().getSimpleName());
			m_cat.logError(e);
			throw new DalException("Error when executing query(" + ctx.getSqlStatement() + ") failed, Proto: " + proto
			      + ", message: " + e, e);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					throw new DalRuntimeException("Error when closing PreparedStatement, message: " + e, e);
				}
			}

			m_transactionManager.closeConnection();
			t.complete();
		}
	}

	@Override
	public <T extends DataObject> int[] executeUpdateBatch(QueryContext ctx, T[] protos) throws DalException {
		Transaction t = m_cat.newTransaction("SQL", getQueryName(ctx));
		PreparedStatement ps = null;
		int[] rowCounts = new int[protos.length];
		boolean inTransaction = m_transactionManager.isInTransaction();
		boolean updated = false;

		t.addData(ctx.getSqlStatement());

		try {
			ps = createPreparedStatement(ctx);

			logCatEvent(ctx);

			if (ctx.getQuery().isStoreProcedure()) {
				if (!inTransaction) {
					ps.getConnection().setAutoCommit(false);
				}

				for (int i = 0; i < protos.length; i++) {
					// Call beforeSave() to do some custom data manipulation
					protos[i].beforeSave();

					// Setup IN/OUT parameters
					setupInOutParameters(ps, ctx.getParameters(), protos[i]);

					// Execute the query
					rowCounts[i] = ps.executeUpdate();

					updated = true;

					// Get OUT parameters if have
					retrieveOutParameters(ps, ctx.getParameters(), protos[i]);

					// Retrieve Generated Keys if have
					if (ctx.getQuery().getType() == QueryType.INSERT) {
						retrieveGeneratedKeys(ctx, ps, protos[i]);
					}
				}

				if (!inTransaction && updated) {
					ps.getConnection().commit();
					ps.getConnection().setAutoCommit(true);
				}
			} else {
				for (int i = 0; i < protos.length; i++) {
					// Call beforeSave() to do some custom data manipulation
					protos[i].beforeSave();

					// Setup IN/OUT parameters
					setupInOutParameters(ps, ctx.getParameters(), protos[i]);

					ps.addBatch();
				}

				rowCounts = ps.executeBatch();

				// Unfortunately, getGeneratedKeys() is not supported by
				// executeBatch()
			}

			t.setStatus(Transaction.SUCCESS);
			return rowCounts;
		} catch (SQLException e) {
			if (!inTransaction && updated) {
				try {
					ps.getConnection().rollback();
					ps.getConnection().setAutoCommit(true);
				} catch (SQLException sqle) {
					e.setNextException(sqle);
				}
			}

			t.setStatus(e.getClass().getSimpleName());
			m_cat.logError(e);
			throw new DalException("Error when executing query(" + ctx.getSqlStatement() + ") failed, Proto: "
			      + ctx.getProto() + ", message: " + e, e);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					throw new DalRuntimeException("Error when closing PreparedStatement, message: " + e, e);
				}
			}

			m_transactionManager.closeConnection();
			t.complete();
		}
	}

	protected String getQueryName(QueryContext ctx) {
		QueryDef query = ctx.getQuery();
		EntityInfo entity = ctx.getEntityInfo();

		return entity.getLogicalName() + "." + query.getName();
	}

	protected void logCatEvent(QueryContext ctx) {
		JdbcDataSourceConfiguration config = m_dataSourceManager.getDataSourceConfiguration(ctx.getDataSourceName());

		m_cat.logEvent("SQL.Method", ctx.getQuery().getType().name(), Message.SUCCESS, null);
		m_cat.logEvent("SQL.Database", config == null ? "no-url" : config.getUrl(), Message.SUCCESS, null);
	}

	protected void retrieveGeneratedKeys(QueryContext ctx, PreparedStatement ps, DataObject proto) throws SQLException {
		EntityInfo entityInfo = ctx.getEntityInfo();
		ResultSet generatedKeys = null;
		boolean retrieved = false;

		for (DataField field : entityInfo.getAttributeFields()) {
			Attribute attribute = entityInfo.getAttribute(field);

			if (attribute != null && attribute.autoIncrement()) {
				if (!retrieved) {
					generatedKeys = ps.getGeneratedKeys();
					retrieved = true;
				}

				if (generatedKeys != null && generatedKeys.next()) {
					Object key = generatedKeys.getObject(1);
					m_accessor.setFieldValue(proto, field, key);

					break;
				}
			}
		}
	}

	protected <T extends DataObject> void retrieveOutParameters(PreparedStatement ps, List<Parameter> parameters, T proto)
	      throws SQLException {
		if (ps instanceof CallableStatement) {
			int len = parameters.size();
			CallableStatement cs = (CallableStatement) ps;

			for (int i = 0; i < len; i++) {
				Parameter parameter = parameters.get(i);

				if (parameter.isOut()) {
					Object value = cs.getObject(i + 1);

					m_accessor.setFieldValue(proto, parameter.getField(), value);
				}
			}
		}
	}

	protected <T extends DataObject> void setupInOutParameters(PreparedStatement ps, List<Parameter> parameters, T proto)
	      throws SQLException {
		int len = parameters.size();

		if (len > 0) {
			int index = 1;

			for (int i = 0; i < len; i++, index++) {
				Parameter parameter = parameters.get(i);

				if (parameter.isIn()) {
					Object value = m_accessor.getFieldValue(proto, parameter.getField());

					if (parameter.isIterable()) { // Iterable
						Iterable<?> iterable = (Iterable<?>) value;

						for (Object item : iterable) {
							ps.setObject(index++, item);
						}

						index--;
					} else if (parameter.isArray()) { // Array
						int length = Array.getLength(value);

						for (int j = 0; j < length; j++) {
							ps.setObject(index++, Array.get(value, j));
						}

						index--;
					} else {
						ps.setObject(index, value);
					}
				}

				if (parameter.isOut() && ps instanceof CallableStatement) {
					int outType = parameter.getOutType();
					CallableStatement cs = (CallableStatement) ps;

					if (outType == Types.NUMERIC || outType == Types.DECIMAL) {
						cs.registerOutParameter(index, outType, parameter.getOutScale());
					} else {
						cs.registerOutParameter(index, outType);
					}
				}
			}
		}
	}
}
