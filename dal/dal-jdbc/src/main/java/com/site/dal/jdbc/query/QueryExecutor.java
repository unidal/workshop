package com.site.dal.jdbc.query;

import java.util.List;

import com.site.dal.jdbc.DalException;
import com.site.dal.jdbc.DataObject;
import com.site.dal.jdbc.engine.QueryContext;

public interface QueryExecutor {

   public <T extends DataObject> List<T> executeQuery(QueryContext ctx) throws DalException;

   public int executeUpdate(QueryContext ctx) throws DalException;

   public <T extends DataObject> int[] executeUpdateBatch(QueryContext ctx, T[] protos) throws DalException;
}
