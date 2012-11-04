package com.site.lookup.tuple;

/**
 * Tuple to hold three elements: first, middle and last.
 * 
 * @param <F>
 *           first
 * @param <M>
 *           middle
 * @param <L>
 *           last
 */
public class Triple<F, M, L> {
	private F m_first;

	private M m_middle;

	private L m_last;

	public Triple() {
	}

	public Triple(F first, M middle, L last) {
		m_first = first;
		m_middle = middle;
		m_last = last;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof Triple) {
			Triple<Object, Object, Object> o = (Triple<Object, Object, Object>) obj;

			if (m_first == null) {
				if (o.m_first != null) {
					return false;
				}
			} else if (!m_first.equals(o.m_first)) {
				return false;
			}

			if (m_middle == null) {
				if (o.m_middle != null) {
					return false;
				}
			} else if (!m_middle.equals(o.m_middle)) {
				return false;
			}

			if (m_last == null) {
				if (o.m_last != null) {
					return false;
				}
			} else if (!m_last.equals(o.m_last)) {
				return false;
			}

			return true;
		}

		return false;
	}

	public F getFirst() {
		return m_first;
	}

	public L getLast() {
		return m_last;
	}

	public M getMiddle() {
		return m_middle;
	}

	@Override
	public int hashCode() {
		int hash = 0;

		hash = hash * 31 + (m_first == null ? 0 : m_first.hashCode());
		hash = hash * 31 + (m_middle == null ? 0 : m_middle.hashCode());
		hash = hash * 31 + (m_last == null ? 0 : m_last.hashCode());

		return hash;
	}

	public void setFirst(F first) {
		m_first = first;
	}

	public void setLast(L last) {
		m_last = last;
	}

	public void setMiddle(M middle) {
		m_middle = middle;
	}

	@Override
	public String toString() {
		return String.format("Triple[first=%s, middle=%s, last=%s]", m_first, m_middle, m_last);
	}
}
