//package com.site.data.bean;
//
//import java.util.Map;
//import java.util.Set;
//
//import com.site.data.IManagedData;
//import com.site.data.IManagedDataMeta;
//
//public class MapMd<K, V> extends BaseMd<K, MapMd.MapMeta<K>> {
//	private Map<K, V> m_map;
//
//	@SuppressWarnings("unchecked")
//	public MapMd(Map<K, V> map) {
//		super(new MapMeta<K>((Map<K, Object>) map));
//
//		m_map = map;
//	}
//
//	public Map<K, V> map() {
//		return m_map;
//	}
//
//	public IManagedData<?> getData(K key) {
//		return (IManagedData<?>) m_map.get(key);
//	}
//
//	public boolean hasData(K key) {
//		return m_map.containsKey(key);
//	}
//
//	public void setData(K key, Object value) {
//		m_map.put(key, (V) value);
//	}
//
//	public void unsetData(Object key) {
//		m_map.remove(key);
//	}
//
//	public static class MapMeta<K> implements IManagedDataMeta<K> {
//		private Map<K, Object> m_map;
//
//		protected MapMeta(Map<K, Object> map) {
//			m_map = map;
//		}
//
//		public Set<K> getKeys() {
//			return m_map.keySet();
//		}
//
//		public boolean isNullable(Object key) {
//			return true;
//		}
//	}
//}
