package com.site.bes.common.dal;

import java.util.Date;

import com.site.bes.common.dal.EventDao;
import com.site.bes.common.dal.EventDo;
import com.site.bes.common.dal.EventEntity;
import com.site.kernel.common.BaseTestCase;
import com.site.kernel.dal.DalException;
import com.site.kernel.dal.db.SimpleTableProvider;
import com.site.kernel.dal.db.helpers.TableProviderManager;

public class AddBatchEvents extends BaseTestCase {
	public static void initialize(String ds) {
		TableProviderManager m = TableProviderManager.getInstance();

		m.register(new SimpleTableProvider(ds, EventEntity.EVENT));
	}

	protected void setUp() throws Exception {
		super.setUp();

		initialize("infotree");
	}

	public void testAddBatchEvents() throws DalException {
		printMethodHeader();

		for (int i = 0; i < 10000; i++) {
			EventDo e = EventDao.getInstance().createLocal();

			e.setCreationDate(new Date());
			e.setEventType("User.New");
			e.setPayload("id=" + (i+1));
			e.setPayloadType(1);
			e.setProducerId("producerId");
			e.setProducerType("producerType");
         
			EventDao.getInstance().insert(e);
		}
	}
}
