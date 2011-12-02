package com.site.data.bean;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.site.data.IManagedData;
import com.site.data.IManagedDataMeta;

public abstract class BaseMd<T, M extends IManagedDataMeta<T>> implements IManagedData<T> {
	private M m_meta;

	protected BaseMd(M meta) {
		m_meta = meta;
	}

	public void clear() {
		for (T key : keySet()) {
			remove(key);
		}
	}

	public boolean containsKey(Object key) {
		return keySet().contains(key);
	}

	public boolean containsValue(Object val) {
		Set<T> keys = keySet();

		for (T key : keys) {
			Object value = get(key);

			if (val == null && value == null) {
				return true;
			} else if (val != null && value != null) {
				if (value.equals(val)) {
					return true;
				}
			}
		}

		return false;
	}

	public Set<Entry<T, Object>> entrySet() {
		Set<T> keys = keySet();
		Set<Entry<T, Object>> entries = new HashSet<Entry<T, Object>>(keys.size() * 4 / 3 + 1);

		for (T key : keys) {
			Object value = get(key);

			entries.add(new SimpleEntry<T, Object>(key, value));
		}

		return entries;
	}

	@SuppressWarnings("unchecked")
	public Object get(Object key) {
		return unwrapChild(getData((T) key));
	}

	public M getMeta() {
		return m_meta;
	}

	public boolean isEmpty() {
		return keySet().size() == 0;
	}

	public Set<T> keySet() {
		return m_meta.getKeys();
	}

	public Object put(T key, Object value) {
		setData(key, wrapChild(value));
		return null;
	}

	public void putAll(Map<? extends T, ?> m) {
		for (Map.Entry<? extends T, ?> entry : m.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@SuppressWarnings("unchecked")
	public Object remove(Object key) {
		unsetData((T) key);
		return null;
	}

	public int size() {
		return keySet().size();
	}

	protected abstract Object unwrapChild(IManagedData<?> value);

	public Collection<Object> values() {
		Set<T> keys = keySet();
		List<Object> values = new ArrayList<Object>(keys.size());

		for (T key : keys) {
			Object value = get(key);

			values.add(value);
		}

		return values;
	}

	protected abstract IManagedData<?> wrapChild(Object value);
}
