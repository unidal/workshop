package org.unidal.expense.biz.trip;

import org.unidal.expense.biz.ExpenseContext;
import org.unidal.expensel.view.UriBuilder;

public class Context extends ExpenseContext<Payload> {
	private int m_memberId;

	private int m_tripId;

	public int getMemberId() {
		return m_memberId;
	}

	public int getTripId() {
		return m_tripId;
	}

	public void redirect(Model model, Object path) {
		String uri = UriBuilder.uri(model, path);

		redirect(uri);
	}

	public void redirect(Model model, Object path, String qs) {
		String uri = UriBuilder.uri2(model, path, qs);

		redirect(uri);
	}

	public void setMemberId(int memberId) {
		m_memberId = memberId;
	}

	public void setTripId(int tripId) {
		m_tripId = tripId;
	}
}