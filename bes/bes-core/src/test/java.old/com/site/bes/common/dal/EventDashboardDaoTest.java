package com.site.bes.common.dal;

import java.util.Date;

import com.site.bes.common.dal.EventDashboardDao;
import com.site.bes.common.dal.EventDashboardDo;
import com.site.bes.common.dal.EventDashboardEntity;
import com.site.kernel.common.BaseTestCase;
import com.site.kernel.dal.DalException;
import com.site.kernel.dal.db.SimpleTableProvider;
import com.site.kernel.dal.db.helpers.TableProviderManager;

public class EventDashboardDaoTest extends BaseTestCase {
	public static void initialize(String ds) {
		TableProviderManager m = TableProviderManager.getInstance();

		m.register(new SimpleTableProvider(ds,
				EventDashboardEntity.EVENT_DASHBOARD));
	}

	protected void setUp() throws Exception {
		super.setUp();

		initialize("infotree");
	}

	public void testCRUD() throws DalException {
		printMethodHeader();

		EventDashboardDo e1 = EventDashboardDao.getInstance().createLocal();

		e1.setCreationDate(new Date());
		e1.setLastModifiedDate(new Date());
		e1.setEventType("eventType");
		e1.setConsumerType("consumerType");
      e1.setBatchTimeout(300);
		e1.setLastFetchedId(0);
		e1.setLastScheduledDate(new Date());
		
		EventDashboardDao.getInstance().insert(e1);

		EventDashboardDo e2 = EventDashboardDao.getInstance().findByPK(
				e1.getEventType(), e1.getConsumerType(),
				EventDashboardEntity.READSET_FULL);

		e2.setLastFetchedId(1);

		EventDashboardDao.getInstance().updateByPK(e2,
				EventDashboardEntity.UPDATESET_FULL);

		EventDashboardDo e3 = EventDashboardDao.getInstance().findByPK(
				e1.getEventType(), e1.getConsumerType(),
				EventDashboardEntity.READSET_FULL);

		assertTrue(e2.getLastFetchedId() != e3.getLastFetchedId());

		e3.setKeyEventType(e3.getEventType());
		e3.setKeyConsumerType(e3.getConsumerType());
		EventDashboardDao.getInstance().deleteByPK(e3);
	}
}
