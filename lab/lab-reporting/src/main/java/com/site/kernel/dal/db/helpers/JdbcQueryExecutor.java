package com.site.kernel.dal.db.helpers;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

import com.site.kernel.dal.DalException;
import com.site.kernel.dal.DalRuntimeException;
import com.site.kernel.dal.DataObjectField;
import com.site.kernel.dal.ValueType;
import com.site.kernel.dal.datasource.DataSourceException;
import com.site.kernel.dal.db.DataRow;
import com.site.kernel.dal.db.DataRowField;
import com.site.kernel.dal.db.Entity;
import com.site.kernel.logging.cal.CalFactory;
import com.site.kernel.logging.cal.CalTransaction;

@SuppressWarnings("rawtypes")
public class JdbcQueryExecutor {
	private static final JdbcQueryExecutor s_instance = new JdbcQueryExecutor();

	public static JdbcQueryExecutor getInstance() {
		return s_instance;
	}

	private JdbcQueryExecutor() {
	}

	private void closeStatement(PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			// Ignore it
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public <S extends DataRow> List<S> executeQuery(QueryContext ctx) throws DalException {
		PooledConnection conn = getConnection(ctx);
		DataRow protoDo = ctx.getProtoDo();
		PreparedStatement ps = null;
		CalTransaction cal = CalFactory.createCalTransaction("SQL", ctx.getQueryType().getName());

		cal.addData(ctx.getSqlStatement());

		try {
			Connection connection = conn.getConnection();

			if (ctx.isStoredProcedure()) {
				// Create a PreparedStatement
				ps = connection.prepareCall(ctx.getSqlStatement(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				setOutFields(ctx);
			} else {
				// Create a PreparedStatement
				ps = connection.prepareStatement(ctx.getSqlStatement(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			}

			// Set fetch size if have
			if (ctx.getFetchSize() > 0) {
				ps.setFetchSize(ctx.getFetchSize());
			}

			// Setup IN/OUT parameters
			setupInOutParameters(ps, ctx.getParameters(), protoDo);

			// Execute the query
			ResultSet rs = ps.executeQuery();

			// Fetch all rows
			List<S> rows = (List<S>) loadData(rs, ctx.getOutFields(), ctx.getEntity());

			// Get OUT parameters if have
			retrieveOutParameters(ps, ctx.getParameters(), protoDo);

			cal.setStatus("0");
			return rows;
		} catch (SQLException e) {
			cal.setStatus(e);
			throw new DalException("execute query(" + ctx.getSqlStatement() + ") failed, ProtoDo: " + protoDo + ", ERROR: " + e, e);
		} finally {
			closeStatement(ps);
			cal.complete();
			freeConnection(conn);
		}
	}

	public int executeUpdate(QueryContext ctx) throws DalException {
		PooledConnection conn = getConnection(ctx);
		DataRow protoDo = ctx.getProtoDo();
		PreparedStatement ps = null;
		CalTransaction cal = CalFactory.createCalTransaction("SQL", ctx.getQueryType().getName());

		cal.addData(ctx.getSqlStatement());

		try {
			Connection connection = conn.getConnection();

			if (ctx.isStoredProcedure()) {
				// Create a CallableStatement
				ps = connection.prepareCall(ctx.getSqlStatement());
			} else {
				// Create a PreparedStatement
				ps = connection.prepareStatement(ctx.getSqlStatement());
			}

			// Call beforeSave() to do some custom data manipulation
			protoDo.beforeSave();

			// Setup IN/OUT parameters
			setupInOutParameters(ps, ctx.getParameters(), protoDo);

			// Execute the query
			int rowCount = ps.executeUpdate();

			// Get OUT parameters if have
			retrieveOutParameters(ps, ctx.getParameters(), protoDo);

			if (ctx.isQueryInsert()) {
				retrieveGeneratedKeys(ps, protoDo);
			}

			cal.setStatus("0");
			return rowCount;
		} catch (SQLException e) {
			cal.setStatus(e);
			throw new DalException("execute query(" + ctx.getSqlStatement() + ") failed, ProtoDo: " + protoDo + ", ERROR: " + e, e);
		} finally {
			closeStatement(ps);
			cal.complete();
			freeConnection(conn);
		}
	}

	public int[] executeUpdateBatch(QueryContext ctx, DataRow[] protoDos) throws DalException {
		PooledConnection conn = getConnection(ctx);
		PreparedStatement ps = null;

		// Set manual commit mode
		Connection connection = null;
		boolean dirty = false;

		try {
			connection = conn.getConnection();

			connection.setAutoCommit(false);
		} catch (SQLException e) {
			throw new DataSourceException("Can't get connection, ERROR: " + e.getMessage(), e);
		}

		if (ctx.isStoredProcedure()) {
			try {
				int[] rowCounts = new int[protoDos.length];

				// Create a PreparedStatement
				ps = connection.prepareStatement(ctx.getSqlStatement());

				for (int i = 0; i < protoDos.length; i++) {
					dirty = true;

					// Call beforeSave() to do some custom data manipulation
					protoDos[i].beforeSave();

					// Setup IN parameters
					setupInOutParameters(ps, ctx.getParameters(), protoDos[i]);

					// Execute the query
					rowCounts[i] = ps.executeUpdate();

					// Get OUT parameters if have
					retrieveOutParameters(ps, ctx.getParameters(), protoDos[i]);

					if (ctx.isQueryInsert()) {
						retrieveGeneratedKeys(ps, protoDos[i]);
					}
				}

				// commit the batch transactions
				connection.commit();

				// Return the result
				return rowCounts;
			} catch (SQLException e) {
				try {
					if (dirty) {
						connection.rollback();
					}
				} catch (SQLException se) {
					// ignore the exception
					se.printStackTrace();
				}

				throw new DalException("execute query(" + ctx.getSqlStatement() + ") failed, ERROR: " + e, e);
			} finally {
				closeStatement(ps);
				freeConnection(conn);
			}
		} else {
			try {
				// Create a PreparedStatement
				ps = connection.prepareStatement(ctx.getSqlStatement(), PreparedStatement.RETURN_GENERATED_KEYS);

				for (int i = 0; i < protoDos.length; i++) {
					// Call beforeSave() to do some custom data manipulation
					protoDos[i].beforeSave();

					// Setup IN parameters
					setupInOutParameters(ps, ctx.getParameters(), protoDos[i]);

					// Add it for batch update
					ps.addBatch();
					dirty = true;
				}

				// Execute the batch query
				int[] rowCounts = ps.executeBatch();

				if (ctx.isQueryInsert()) {
					retrieveGeneratedKeys(ps, protoDos);
				}

				// commit the batch transactions
				connection.commit();

				// Return the result
				return rowCounts;
			} catch (SQLException e) {
				try {
					if (dirty) {
						connection.rollback();
					}
				} catch (SQLException se) {
					// ignore the exception
					se.printStackTrace();
				}

				throw new DalException("execute query(" + ctx.getSqlStatement() + ") failed, ERROR: " + e, e);
			} finally {
				closeStatement(ps);
				freeConnection(conn);
			}
		}
	}

	private void freeConnection(PooledConnection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			// Ignore it
			e.printStackTrace();
		}
	}

	private PooledConnection getConnection(QueryContext ctx) throws DataSourceException {
		ConnectionPoolDataSource dataSource = ctx.getDataSource();

		if (dataSource == null) {
			throw new DalRuntimeException("INTERNAL ERROR: No DataSource available for " + ctx.getLogicalDataSourceName());
		}

		try {
			return dataSource.getPooledConnection();
		} catch (SQLException e) {
			throw new DataSourceException("Can't get connection from DataSource(" + ctx.getLogicalDataSourceName() + ")", e);
		}
	}

	private Constructor<?> getDoConstructor(Class<?> clazz) {
		try {
			Constructor<?> c = clazz.getDeclaredConstructor((Class[]) null);

			c.setAccessible(true);
			return c;
		} catch (Exception e) {
			throw new DalRuntimeException(clazz + " must have a constructor without parameter");
		}
	}

	private DataRow getDoInstnace(Map<Entity, DataRow> map, DataRowField field) {
		Entity entity = field.getEntity();
		DataRow di = map.get(entity);

		if (di == null) {
			di = newInstance(getDoConstructor(entity.getDoClass()));

			map.put(entity, di);
		}

		return di;
	}

	private List<DataRow> loadData(ResultSet rs, List fields, Entity entity) throws SQLException {
		List<DataRow> results = new ArrayList<DataRow>();
		Map<Entity, DataRow> map = new HashMap<Entity, DataRow>();
		int size = fields.size();

		try {
			while (rs.next()) {
				DataRow row = newInstance(getDoConstructor(entity.getDoClass()));

				map.put(entity, row);

				for (int i = 0; i < size; i++) {
					DataRowField field = (DataRowField) fields.get(i);
					DataRow di = getDoInstnace(map, field);

					setDataRowFieldValue(rs, i, di, field);
				}

				// call afterLoad() to do some custom data manipulation
				row.afterLoad();
				results.add(row);
			}
		} catch (SQLException e) {
			if ("S1000".equals(e.getSQLState())) {
				// ignore it
			} else {
				throw e;
			}
		}

		return results;
	}

	private DataRow newInstance(Constructor<?> constructor) {
		try {
			return (DataRow) constructor.newInstance((Object[]) null);
		} catch (Exception e) {
			throw new DalRuntimeException("Failed to create new instance of " + constructor.getDeclaringClass());
		}
	}

	private void registerOutParameter(CallableStatement cs, int index, int type) throws SQLException {
		if (cs == null) {
			throw new DalRuntimeException("#{...} can't be used outside StoredProcedure statement");
		}

		cs.registerOutParameter(index, type);
	}

	private void retrieveGeneratedKeys(PreparedStatement ps, DataRow protoDo) throws SQLException {
		List<DataObjectField> fields = protoDo.getMetaData().getFields();
		ResultSet generatedKeys = null;
		boolean retrieved = false;

		for (DataObjectField f : fields) {
			DataRowField field = (DataRowField) f;

			if (field.isAutoIncrement()) {
				if (!retrieved) {
					generatedKeys = ps.getGeneratedKeys();
					retrieved = true;
				}

				if (generatedKeys != null && generatedKeys.next()) {
					setDataRowFieldValue(generatedKeys, 0, protoDo, field);
				}

				break;
			}
		}
	}

	private void retrieveGeneratedKeys(PreparedStatement ps, DataRow[] protoDos) throws SQLException {
		List<DataObjectField> fields = protoDos[0].getMetaData().getFields();
		ResultSet generatedKeys = null;
		boolean retrieved = false;

		for (int i = 0; i < protoDos.length; i++) {
			boolean first = true;

			for (int j = 0; j < fields.size(); j++) {
				DataRowField field = (DataRowField) fields.get(j);

				if (field.isAutoIncrement()) {
					if (!retrieved) {
						generatedKeys = ps.getGeneratedKeys();
						retrieved = true;
					}

					if (first) {
						if (!generatedKeys.next()) {
							break;
						}
					}

					first = false;
					setDataRowFieldValue(generatedKeys, j, protoDos[i], field);
				}
			}
		}
	}

	private void retrieveOutParameters(PreparedStatement ps, List parameters, DataRow protoDo) throws SQLException {
		int size = parameters.size();
		CallableStatement cs = null;

		if (ps instanceof CallableStatement) {
			cs = (CallableStatement) ps;
		} else {
			return;
		}

		for (int i = 0; i < size; i++) {
			Parameter parameter = (Parameter) parameters.get(i);
			DataRowField field = parameter.getField();
			ValueType type = field.getValueType();
			Object value = null;
			int index = i + 1;

			if (parameter.isOut()) {
				if (type == ValueType.STRING) {
					value = cs.getString(index);
				} else if (type == ValueType.LONG || type == ValueType.TIME) {
					value = new Long(cs.getLong(index));

					if (cs.wasNull()) {
						value = null;
					}
				} else if (type == ValueType.INT) {
					value = new Integer(cs.getInt(index));

					if (cs.wasNull()) {
						value = null;
					}
				} else if (type == ValueType.DOUBLE) {
					value = new Double(cs.getDouble(index));

					if (cs.wasNull()) {
						value = null;
					}
				} else if (type == ValueType.DATE) {
					Timestamp ts = cs.getTimestamp(index);

					if (ts != null) {
						value = new Date(ts.getTime());
					}
				} else if (type == ValueType.BOOLEAN) {
					value = Boolean.valueOf(cs.getBoolean(index));

					if (cs.wasNull()) {
						value = null;
					}
				}

				if (value != null) {
					protoDo.setFieldValue(field, value);
				}
			}
		}
	}

	private void setDataRowFieldValue(ResultSet rs, int i, DataRow protoDo, DataRowField field) throws SQLException {
		ValueType type = field.getValueType();
		int index = i + 1;

		if (type == ValueType.STRING) {
			String val = rs.getString(index);

			protoDo.setFieldValue(field, val);
		} else if (type == ValueType.LONG || type == ValueType.TIME) {
			long val = rs.getLong(index);

			if (!rs.wasNull()) {
				protoDo.setFieldValue(field, new Long(val));
			}
		} else if (type == ValueType.INT) {
			int val = rs.getInt(index);

			if (!rs.wasNull()) {
				protoDo.setFieldValue(field, new Integer(val));
			}
		} else if (type == ValueType.DOUBLE) {
			double val = rs.getDouble(index);

			if (!rs.wasNull()) {
				protoDo.setFieldValue(field, new Double(val));
			}
		} else if (type == ValueType.DATE) {
			Timestamp val = rs.getTimestamp(index);

			if (!rs.wasNull()) {
				protoDo.setFieldValue(field, new Date(val.getTime()));
			}
		} else if (type == ValueType.BOOLEAN) {
			boolean val = rs.getBoolean(index);

			if (!rs.wasNull()) {
				protoDo.setFieldValue(field, Boolean.valueOf(val));
			}
		}
	}

	private void setOutFields(QueryContext ctx) {
		List fields = ctx.getReadset().getFields();
		int size = fields.size();

		for (int i = 0; i < size; i++) {
			DataRowField field = (DataRowField) fields.get(i);

			// add out field for later ResultSet.getString(...)
			ctx.addOutField(field);
		}
	}

	private void setupInOutParameters(PreparedStatement ps, List parameters, DataRow protoDo) throws SQLException {
		int size = parameters.size();
		CallableStatement cs = null;

		if (ps instanceof CallableStatement) {
			cs = (CallableStatement) ps;
		}

		for (int i = 0; i < size; i++) {
			Parameter parameter = (Parameter) parameters.get(i);
			DataRowField field = parameter.getField();
			Object value = protoDo.getFieldValue(field);
			ValueType type = field.getValueType();
			int index = i + 1;

			// For array type parameter, use element's value instead
			if (type == ValueType.ARRAY) {
				int arrayIndex = parameter.getIndex();
				Object element = Array.get(value, arrayIndex);

				value = element;
				type = ValueType.getByDefinedClass(element.getClass());
			}

			if (type == ValueType.STRING) {
				if (parameter.isIn()) {
					if (value != null) {
						ps.setString(index, (String) value);
					} else {
						ps.setNull(index, Types.VARCHAR);
					}
				}

				if (parameter.isOut()) {
					registerOutParameter(cs, index, Types.VARCHAR);
				}
			} else if (type == ValueType.LONG || type == ValueType.TIME) {
				if (parameter.isIn()) {
					if (value != null) {
						ps.setLong(index, ((Long) value).longValue());
					} else {
						ps.setNull(index, Types.INTEGER);
					}
				}

				if (parameter.isOut()) {
					registerOutParameter(cs, index, Types.INTEGER);
				}
			} else if (type == ValueType.INT) {
				if (parameter.isIn()) {
					if (value != null) {
						ps.setInt(index, ((Integer) value).intValue());
					} else {
						ps.setNull(index, Types.INTEGER);
					}
				}

				if (parameter.isOut()) {
					registerOutParameter(cs, index, Types.INTEGER);
				}
			} else if (type == ValueType.DOUBLE) {
				if (parameter.isIn()) {
					if (value != null) {
						ps.setDouble(index, ((Double) value).doubleValue());
					} else {
						ps.setNull(index, Types.DOUBLE);
					}
				}

				if (parameter.isOut()) {
					registerOutParameter(cs, index, Types.DOUBLE);
				}
			} else if (type == ValueType.DATE) {
				if (parameter.isIn()) {
					if (value != null) {
						Date date = (Date) value;
						Timestamp ts = new Timestamp(date.getTime());

						ps.setTimestamp(index, ts);
					} else {
						ps.setNull(index, Types.TIMESTAMP);
					}
				}

				if (parameter.isOut()) {
					registerOutParameter(cs, index, Types.TIMESTAMP);
				}
			} else if (type == ValueType.BOOLEAN) {
				if (parameter.isIn()) {
					if (value != null) {
						ps.setBoolean(index, ((Boolean) value).booleanValue());
					} else {
						ps.setNull(index, Types.BOOLEAN);
					}
				}

				if (parameter.isOut()) {
					registerOutParameter(cs, index, Types.BOOLEAN);
				}
			}
		}
	}

}
