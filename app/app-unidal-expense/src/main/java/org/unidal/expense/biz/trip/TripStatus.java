package org.unidal.expense.biz.trip;

public enum TripStatus {
	OPEN(0),

	CLOSED(1),

	REMOVED(2),

	SUSPENDED(9),

	;

	private int m_id;

	private TripStatus(int id) {
		m_id = id;
	}

	public int getId() {
		return m_id;
	}
}
