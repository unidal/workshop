package com.site.bes.engine;

import com.site.bes.EventState;

public class EventStatistics {
	private int m_batchId;

	private int m_fetchedRows;

	private int m_successCount;

	private int m_failureCount;

	private long m_fetchTime;

	private int m_processCount;

	private long[] m_waitTimes;

	private long[] m_processTimes;

	public void addProcessTime(long processTime) {
		m_processTimes[m_processCount] = processTime;
		m_processCount++;
	}

	public void addWaitTime(long waitTime) {
		m_waitTimes[m_processCount] = waitTime;
	}

	public void calculateEventState(EventState state) {
		if (state == EventState.COMPLETED || state == EventState.SKIPPED) {
			m_successCount++;
		} else {
			m_failureCount++;
		}
	}

	public long getAvgProcessTime() {
		long sum = 0;

		for (int i = 0; i < m_processCount; i++) {
			sum += m_processTimes[i];
		}

		return m_processCount == 0 ? 0 : sum / m_processCount;
	}

	public long getAvgWaitTime() {
		long sum = 0;

		for (int i = 0; i < m_processCount; i++) {
			sum += m_waitTimes[i];
		}

		return m_processCount == 0 ? 0 : sum / m_processCount;
	}

	public int getBatchId() {
		return m_batchId;
	}

	public int getFailureCount() {
		return m_failureCount;
	}

	public int getFetchedRows() {
		return m_fetchedRows;
	}

	public long getFetchTime() {
		return m_fetchTime;
	}

	public long getMaxProcessTime() {
		long max = 0;

		for (int i = 0; i < m_processCount; i++) {
			if (m_processTimes[i] > max) {
				max = m_processTimes[i];
			}
		}

		return max;
	}

	public long getMaxWaitTime() {
		long max = 0;

		for (int i = 0; i < m_processCount; i++) {
			if (m_waitTimes[i] > max) {
				max = m_waitTimes[i];
			}
		}

		return max;
	}

	public EventProcessStatus getProcessStatus() {
		EventProcessStatus status;

		if (m_successCount == m_fetchedRows) {
			status = EventProcessStatus.ALL_SUCCESS;
		} else if (m_successCount == 0) {
			status = EventProcessStatus.ALL_FAILURE;
		} else {
			status = EventProcessStatus.PARTIAL_FAILURE;
		}

		return status;
	}

	public int getSuccessCount() {
		return m_successCount;
	}

	public void init() {
		m_fetchedRows = 0;
		m_successCount = 0;
		m_failureCount = 0;
		m_fetchTime = 0;
		m_processCount = 0;
		m_waitTimes = new long[256];
		m_processTimes = new long[256];
	}

	public void setBatchId(int batchId) {
		m_batchId = batchId;
	}

	public void setFetchedRows(int fetchedRows) {
		m_fetchedRows = fetchedRows;
	}

	public void setFetchTime(long fetchTime) {
		m_fetchTime = fetchTime;
	}

}
