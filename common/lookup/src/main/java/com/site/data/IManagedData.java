package com.site.data;

import java.util.Map;

public interface IManagedData<T> extends Map<T, Object> {
	public IManagedDataMeta<T> getMeta();

	// map to Object get(Object key)
	public IManagedData<?> getData(T key);

	// map to containsKey(Object key)
	public boolean hasData(T key);

	// map to Object put(Object key, Object value)
	public void setData(T key, IManagedData<?> value);

	// map to Object remove(Object key)
	public void unsetData(T key);
}
