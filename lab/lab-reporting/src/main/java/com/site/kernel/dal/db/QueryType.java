package com.site.kernel.dal.db;

import com.site.kernel.common.BaseEnum;

public class QueryType extends BaseEnum {
	public static final QueryType SELECT = new QueryType(1, "SELECT");
	public static final QueryType INSERT = new QueryType(2, "INSERT");
	public static final QueryType UPDATE = new QueryType(3, "UPDATE");
	public static final QueryType DELETE = new QueryType(4, "DELETE");

	private QueryType(int id, String name) {
		super(id, name);
	}
}
