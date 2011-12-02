package com.site.bes.engine;

import com.site.bes.EventBatch;
import com.site.bes.EventConsumer;
import com.site.kernel.dal.DalException;
import com.site.kernel.logging.Log;

public class EventThread extends Thread {
	private static final Log s_log = Log.getLog(EventThread.class);

	private EventTransporter m_transporter;

	private EventConsumer m_consumer;

	private EventBatch m_batch;

	private long m_checkInterval;

	public void run() {
		EventStatistics stat = new EventStatistics();

		try {
			while (true) {
				long start = System.currentTimeMillis();

				stat.init();
				m_batch.clear();
				m_transporter.fetchEvents(m_batch, stat);

				if (m_batch.length() > 0) {
					try {
						m_consumer.processEvents(m_batch);
					} catch (RuntimeException e) {
						s_log.log(Log.ERROR, "Error occurs when processing events, consumer: " + m_consumer.getClass(), e);
					}

					m_transporter.collectFailedEvents(m_batch);
					m_transporter.updateBatchLog(stat);
				}

				long timeLeft = m_checkInterval - System.currentTimeMillis() + start;

				if (timeLeft > 0) {
					Thread.sleep(timeLeft);
				}
			}
		} catch (InterruptedException e) {
			// ignore it
		} catch (DalException e) {
			s_log.log(e);
		}
	}

	public void setEventConsumer(EventConsumer consumer) {
		m_consumer = consumer;
	}

	public void setTransporter(EventTransporter transporter) {
		m_transporter = transporter;
	}

	public void setQueue(EventBatch queue) {
		m_batch = queue;
	}

	public void setCheckInterval(long checkInterval) {
		m_checkInterval = checkInterval;
	}

}
