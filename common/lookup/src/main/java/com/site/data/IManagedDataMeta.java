package com.site.data;

import java.util.Set;

public interface IManagedDataMeta<T> {
	public Set<T> getKeys();

	public boolean isNullable(T key);
}
