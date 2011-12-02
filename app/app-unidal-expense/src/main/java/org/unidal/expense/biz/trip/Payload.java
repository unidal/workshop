package org.unidal.expense.biz.trip;

import org.unidal.expense.biz.ExpensePage;

import com.site.web.mvc.ActionContext;
import com.site.web.mvc.ActionPayload;
import com.site.web.mvc.payload.annotation.FieldMeta;
import com.site.web.mvc.payload.annotation.PathMeta;

public class Payload implements ActionPayload<ExpensePage, Action> {
	private ExpensePage m_page;

	@FieldMeta(value = "op")
	private Action m_action;

	@PathMeta("tripId")
	private int m_tripId = -1;

	@FieldMeta("mid")
	private Integer[] m_memberIds;

	@FieldMeta("title")
	private String m_title;

	@FieldMeta("save")
	private boolean m_save;

	@Override
	public Action getAction() {
		return m_action;
	}

	public Integer[] getMemberIds() {
		return m_memberIds;
	}

	@Override
	public ExpensePage getPage() {
		return m_page;
	}

	public String getTitle() {
		return m_title;
	}

	public int getTripId() {
		return m_tripId;
	}

	public boolean isSave() {
		return m_save;
	}

	public void setAction(String action) {
		m_action = Action.getByName(action, Action.LIST);
	}

	public void setMemberIds(Integer[] memberIds) {
		m_memberIds = memberIds;
	}

	@Override
	public void setPage(String page) {
		m_page = ExpensePage.getByName(page, ExpensePage.TRIP);
	}

	public void setSave(boolean save) {
		m_save = save;
	}

	public void setTitle(String title) {
		m_title = title;
	}

	public void setTripId(String[] parts) {
		if (parts.length > 0) {
			try {
				m_tripId = Integer.parseInt(parts[0]);
			} catch (NumberFormatException e) {
				// ignore it
			}
		}
	}

	@Override
	public void validate(ActionContext<?> ctx) {
		if (m_action == null) {
			if (m_tripId < 0) {
				m_action = Action.LIST;
			} else {
				m_action = Action.VIEW;
			}
		}
	}
}