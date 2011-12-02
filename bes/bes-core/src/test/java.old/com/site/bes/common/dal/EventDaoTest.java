package com.site.bes.common.dal;

import java.util.Date;
import java.util.List;

import com.site.bes.common.dal.EventDao;
import com.site.bes.common.dal.EventDo;
import com.site.bes.common.dal.EventEntity;
import com.site.kernel.common.BaseTestCase;
import com.site.kernel.dal.DalException;
import com.site.kernel.dal.datasource.DataSourceInitializer;
import com.site.kernel.dal.db.Ref;
import com.site.kernel.dal.db.SimpleTableProvider;
import com.site.kernel.dal.db.helpers.TableProviderManager;

public class EventDaoTest extends BaseTestCase {
	public static void initialize(String ds) {
      DataSourceInitializer.initialize();
		TableProviderManager m = TableProviderManager.getInstance();

		m.register(new SimpleTableProvider(ds, EventEntity.EVENT));
	}

	protected void setUp() throws Exception {
		super.setUp();

		initialize("infotree");
	}

	public void testCRUD() throws DalException {
		printMethodHeader();

		EventDo e1 = EventDao.getInstance().createLocal();

		e1.setCreationDate(new Date());
		e1.setEventType("E1");
		e1.setPayload("payload");
		e1.setPayloadType(0);
		e1.setProducerId("producerId");
		e1.setProducerType("producerType");
		EventDao.getInstance().insert(e1);

		assertTrue(e1.getEventId() != 0);

		EventDo e2 = EventDao.getInstance().findByPK(e1.getEventId(), EventEntity.READSET_FULL);

		e2.setPayloadType(1);

		EventDao.getInstance().updateByPK(e2, EventEntity.UPDATESET_FULL);

		EventDo e3 = EventDao.getInstance().findByPK(e1.getEventId(), EventEntity.READSET_FULL);

		assertTrue(e2.getPayloadType() != e3.getPayloadType());

		e3.setKeyEventId(e3.getEventId());
		EventDao.getInstance().deleteByPK(e3);
	}

	public void testFetch() throws DalException {
		printMethodHeader();

		Ref batchId = new Ref();
		List events = EventDao.getInstance().fetchEvents("E1", "C1", "consumer-id", batchId,
				EventEntity.READSET_FETCH_EVENTS);

		System.out.println("Batch Id: " + batchId.get() + ", Events fetched: " + events.size());
	}
}
