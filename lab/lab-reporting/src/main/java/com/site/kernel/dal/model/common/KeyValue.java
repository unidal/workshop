package com.site.kernel.dal.model.common;

public class KeyValue {
	private int m_index;
	private Object[] m_values;

	public KeyValue(Object[] values) {
		m_values = values;
	}

	public Object[] getValues() {
		return m_values;
	}
	
	public int getIndex() {
		return m_index;
	}

	public void setIndex(int index) {
		m_index = index;
	}

	public int hashCode() {
		int hash = 1;

		for (int i = 0; i < m_values.length; i++) {
			hash = hash * 31 + (m_values[i] == null ? 0 : m_values[i].hashCode());
		}

		return hash;
	}

	public boolean equals(Object obj) {
		if (obj instanceof KeyValue) {
			KeyValue key = (KeyValue) obj;
			Object[] values = key.getValues();

			if (m_values.length == values.length) {
				for (int i = 0; i < m_values.length; i++) {
					if (m_values[i] == null) {
						if (values[i] == null) {
							continue;
						} else {
							return false;
						}
					} else if (!m_values[i].equals(values[i])) {
						return false;
					}
				}

				return true;
			}
		}

		return false;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer(64);

		sb.append("Key[index=").append(m_index).append(",values={");

		for (int i = 0; i < m_values.length; i++) {
			if (i > 0) {
				sb.append(',');
			}

			sb.append(m_values[i]);
		}

		sb.append("}]");

		return sb.toString();
	}
}
