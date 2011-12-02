package com.site.dal.jdbc.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.site.dal.jdbc.DataObject;
import com.site.dal.jdbc.engine.QueryContext;

public interface DataObjectAssembly {
   public <T extends DataObject> List<T> assemble(QueryContext ctx, ResultSet rs) throws SQLException;
}
