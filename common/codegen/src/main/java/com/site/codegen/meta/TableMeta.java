package com.site.codegen.meta;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.jdom.Element;

public interface TableMeta {
   public Element getMeta(DatabaseMetaData meta, String table) throws SQLException;
}
