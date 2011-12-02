package org.unidal.expense.biz.trip;

import java.util.List;

import org.unidal.expense.biz.ExpensePage;
import org.unidal.expense.dal.Member;
import org.unidal.expense.dal.Trip;

import com.site.web.mvc.ViewModel;

public class Model extends ViewModel<ExpensePage, Action, Context> {
	private Action m_action;

	private List<Trip> m_trips;

	private Trip m_trip;

	private List<Integer> m_tripMemberIds;

	private List<Member> m_members;

	public Model(Context ctx) {
		super(ctx);
	}

	public Action getAction() {
		return m_action;
	}

	@Override
	public Action getDefaultAction() {
		return Action.LIST;
	}

	public List<Member> getMembers() {
		return m_members;
	}

	public Trip getTrip() {
		return m_trip;
	}

	public List<Integer> getTripMemberIds() {
		return m_tripMemberIds;
	}

	public List<Trip> getTrips() {
		return m_trips;
	}

	public void setAction(Action action) {
		m_action = action;
	}

	public void setMembers(List<Member> members) {
		m_members = members;
	}

	public void setTrip(Trip trip) {
		m_trip = trip;
	}

	public void setTripMemberIds(List<Integer> tripMemberIds) {
		m_tripMemberIds = tripMemberIds;
	}

	public void setTrips(List<Trip> trips) {
		m_trips = trips;
	}
}
