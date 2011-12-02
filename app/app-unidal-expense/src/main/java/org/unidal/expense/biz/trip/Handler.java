package org.unidal.expense.biz.trip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.unidal.expense.biz.ExpensePage;
import org.unidal.expense.dal.Member;
import org.unidal.expense.dal.MemberDao;
import org.unidal.expense.dal.MemberEntity;
import org.unidal.expense.dal.Trip;
import org.unidal.expense.dal.TripDao;
import org.unidal.expense.dal.TripEntity;
import org.unidal.expense.dal.TripMember;
import org.unidal.expense.dal.TripMemberDao;
import org.unidal.expense.dal.TripMemberEntity;

import com.site.dal.jdbc.DalException;
import com.site.helper.Lists;
import com.site.helper.Lists.Factor;
import com.site.helper.Lists.Segregation;
import com.site.helper.Transformers;
import com.site.helper.Transformers.IBuilder;
import com.site.lookup.annotation.Inject;
import com.site.web.mvc.ErrorObject;
import com.site.web.mvc.PageHandler;
import com.site.web.mvc.annotation.InboundActionMeta;
import com.site.web.mvc.annotation.OutboundActionMeta;
import com.site.web.mvc.annotation.PayloadMeta;
import com.site.web.mvc.annotation.PreInboundActionMeta;

public class Handler implements PageHandler<Context> {
	@Inject
	private JspViewer m_jspViewer;

	@Inject
	private TripDao m_tripDao;

	@Inject
	private MemberDao m_memberDao;

	@Inject
	private TripMemberDao m_tripMemberDao;

	private void doAdd(Context ctx, Payload payload) {
		String title = payload.getTitle();

		if (title == null || title.isEmpty()) {
			ctx.addError(new ErrorObject("biz.trip.add.required").setArguments("title", title));
		} else {
			Trip trip = new Trip();

			trip.setOwnerId(ctx.getMemberId());
			trip.setTitle(title);
			trip.setStatus(TripStatus.OPEN.getId());
			trip.setAuthCode(getUniqueAuthCode());

			try {
				m_tripDao.insert(trip);
				ctx.setTripId(trip.getId());
			} catch (DalException e) {
				ctx.addError(new ErrorObject("dal.trip.add", e));
			}
		}
	}

	private void doMember(Context ctx, Payload payload) {
		int tripId = payload.getTripId();
		Integer[] ids = payload.getMemberIds();

		if (ids.length == 0) {
			ctx.addError(new ErrorObject("biz.trip.member.required").setArguments("memberIds", ids));
		} else {
			List<TripMember> members = new ArrayList<TripMember>(ids.length);

			for (Integer id : ids) {
				TripMember member = new TripMember();

				member.setTripId(tripId);
				member.setMemberId(id);
				members.add(member);
			}

			List<TripMember> oldMembers;

			try {
				oldMembers = m_tripMemberDao.findAllByTripId(tripId, TripMemberEntity.READSET_FULL);

				for (TripMember t : oldMembers) {
					t.setKeyTripId(t.getTripId());
					t.setKeyMemberId(t.getMemberId());
				}
			} catch (DalException e) {
				ctx.addError(new ErrorObject("dal.trip.member.existing", e));
				return;
			}

			Segregation<TripMember> result = Lists.segregate(members, oldMembers, new Factor<TripMember>() {
				@Override
				public Object getId(TripMember t) {
					return t.getTripId() * 31 + t.getMemberId();
				}

				@Override
				public TripMember merge(TripMember t1, TripMember t2) {
					return t1;
				}
			});

			if (!result.getInsert().isEmpty()) {
				try {
					m_tripMemberDao.insert(Lists.toArray(TripMember.class, result.getInsert()));
				} catch (DalException e) {
					ctx.addError(new ErrorObject("dal.trip.member.add", e));
				}
			}

			if (!result.getDelete().isEmpty()) {
				try {
					m_tripMemberDao.deleteByPK(Lists.toArray(TripMember.class, result.getDelete()));
				} catch (DalException e) {
					ctx.addError(new ErrorObject("dal.trip.member.remove", e));
				}
			}

			if (!ctx.hasErrors()) {
				ctx.setTripId(tripId);
			}
		}
	}

	private String getAuthCode() {
		int len = 6;
		StringBuilder sb = new StringBuilder(6);

		for (int i = 0; i < len; i++) {
			int digit = (int) (10 * Math.random());

			sb.append(digit);
		}

		return sb.toString();
	}

	private String getUniqueAuthCode() {
		while (true) {
			String authCode = getAuthCode();

			try {
				m_tripDao.findByAuthCode(authCode, TripEntity.READSET_ID);
				// existed, try another one
			} catch (DalException e) {
				// not existed, good one
				return authCode;
			}
		}
	}

	@Override
	@PreInboundActionMeta("signin")
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "trip")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		if (!ctx.hasErrors()) {
			Payload payload = ctx.getPayload();
			Action action = payload.getAction();

			if (payload.isSave() && action.isAdd()) {
				doAdd(ctx, payload);
			} else if (payload.isSave() && action.isMember()) {
				doMember(ctx, payload);
			}
		}
	}

	@Override
	@OutboundActionMeta(name = "trip")
	public void handleOutbound(Context ctx) throws ServletException, IOException {
		Model model = new Model(ctx);
		Payload payload = ctx.getPayload();
		Action action = payload.getAction();

		model.setPage(ExpensePage.TRIP);
		model.setAction(action);

		if (action.isList()) {
			showList(ctx, model);
		} else if (action.isAdd() && !payload.isSave()) {
			// nothing to prepare
		} else if (action.isMember() && !payload.isSave()) {
			showMember(ctx, model, payload);
		} else if (action.isView()) {
			showView(ctx, model, payload);
		} else if (!ctx.hasErrors()) {
			if (payload.isSave() && action.isAdd()) {
				model.setAction(Action.MEMBER);
				ctx.redirect(model, ctx.getTripId());
				return;
			} else if (payload.isSave() && action.isMember()) {
				model.setAction(Action.EXPENSE);
				ctx.redirect(model, ctx.getTripId());
				return;
			}
		}

		m_jspViewer.view(ctx, model);
	}

	private void showList(Context ctx, Model model) {
		try {
			List<TripMember> tripMembers = m_tripMemberDao.findAllByMemberId(ctx.getMemberId(), TripMemberEntity.READSET_FULL);
			List<Integer> tripIds = Transformers.forList().transform(tripMembers, new IBuilder<TripMember, Integer>() {
				@Override
				public Integer build(TripMember from) {
					return from.getTripId();
				}
			});
			List<Trip> trips = m_tripDao.findAllByIds(Lists.toArray(Integer.class, tripIds), TripEntity.READSET_FULL);

			model.setTrips(trips);
		} catch (DalException e) {
			ctx.addError(new ErrorObject("dal.trip.list", e));
		}
	}

	private void showMember(Context ctx, Model model, Payload payload) {
		int tripId = payload.getTripId();

		try {
			Trip trip = m_tripDao.findByPK(tripId, TripEntity.READSET_FULL);

			model.setTrip(trip);
		} catch (DalException e) {
			ctx.addError(new ErrorObject("dal.trip.trip", e));
		}

		try {
			List<TripMember> tripMembers = m_tripMemberDao.findAllByTripId(tripId, TripMemberEntity.READSET_FULL);
			List<Integer> memberIds = Transformers.forList().transform(tripMembers, new IBuilder<TripMember, Integer>() {
				@Override
				public Integer build(TripMember from) {
					return from.getMemberId();
				}
			});

			model.setTripMemberIds(memberIds);
		} catch (DalException e) {
			ctx.addError(new ErrorObject("dal.trip.member.existing", e));
		}

		try {
			List<Member> members = m_memberDao.findAllByMemberId(ctx.getMemberId(), MemberEntity.READSET_FULL);

			model.setMembers(members);
		} catch (DalException e) {
			ctx.addError(new ErrorObject("dal.trip.member.all", e));
		}
	}

	private void showView(Context ctx, Model model, Payload payload) {
		int id = payload.getTripId();

		try {
			Trip trip = m_tripDao.findByPK(id, TripEntity.READSET_FULL);

			model.setTrip(trip);
			model.setAction(Action.VIEW);
		} catch (DalException e) {
			ctx.addError(new ErrorObject("dal.trip.view", e));
		}
	}
}