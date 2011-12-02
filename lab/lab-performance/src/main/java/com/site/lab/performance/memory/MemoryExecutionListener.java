package com.site.lab.performance.memory;

import com.site.lab.performance.ExecutionContext;
import com.site.lab.performance.ExecutionListener;
import com.site.lab.performance.model.Case;

public class MemoryExecutionListener implements ExecutionListener {
	private MemoryMeta m_meta;

	private Object[] m_objects;

	// hold some memory so that System.gc() can work much more correctly
	@SuppressWarnings("unused")
	private Object m_garbage;

	private long m_heap0;

	private long m_heap1;

	private long m_initGcCount;

	private long m_initGcAmount;

	private long m_initGcTime;

	private GarbageCollection m_gc = new GarbageCollection();

	private int m_overhead = 0;

	public MemoryExecutionListener() {
		m_garbage = makeObjects(100000);
	}

	private void destroy() {
		final Object[] objs = m_objects;

		for (int i = objs.length - 1; i >= 0; i--) {
			objs[i] = null;
		}

		m_meta = null;
		m_objects = null;
	}

	public long getInitGcAmount() {
		return m_initGcAmount;
	}

	public long getInitGcCount() {
		return m_initGcCount;
	}

	public long getInitGcTime() {
		return m_initGcTime;
	}

	public boolean isEligible(final ExecutionContext ctx) {
		m_meta = ctx.getMethod().getAnnotation(MemoryMeta.class);

		if (m_meta != null) {
			m_objects = new Object[m_meta.loops()];

			ctx.setExecutions(m_meta.loops());
			ctx.setWarmups(m_meta.warmups());

			return true;
		} else {
			return false;
		}
	}

	private Object[] makeObjects(final int count) {
		final Object[] objects = new Object[count];

		for (int i = 0; i < count; i++) {
			objects[i] = new Object();
		}

		return objects;
	}

	public void onAfterExecutions(final ExecutionContext ctx) {
		m_gc.runGC();

		final long heap = m_gc.usedMemory() - m_heap1 - m_overhead;
		// final long gcCount = m_gc.getGcCount() - m_initGcCount;
		// final long gcAmount = m_gc.getGcAmount() - m_initGcAmount;
		// final long gcTime = m_gc.getGcTime() - m_initGcTime;
		final int size = Math.round(((float) heap) / ctx.getExecutions());
		final Case c = ctx.getCase();

		c.setMemoryLoops(ctx.getExecutions());
		c.setMemoryFootprint(size);
		c.setMemoryPermanentFootprint(m_heap1 - m_heap0);
		c.setMemoryTotalFootprint(heap);
		// c.setMemoryGcCount(gcCount);
		// c.setMemoryGcAmount(gcAmount);
		// c.setMemoryGcTime(gcTime);

		// destroy it
		destroy();
	}

	public void onBeforeExecutions(final ExecutionContext ctx) {
		m_gc.runGC();
		m_heap1 = m_gc.usedMemory();

		m_initGcCount = m_gc.getGcCount();
		m_initGcAmount = m_gc.getGcAmount();
		m_initGcTime = m_gc.getGcTime();
	}

	public void onBeforeWarmups(final ExecutionContext ctx) {
		m_gc.runGC();
		m_heap0 = m_gc.usedMemory();
	}

	public void onExecution(final ExecutionContext ctx) {
		// Do not introduce any local temporary variables here
		m_objects[ctx.getCurrentIndex()] = ctx.getCurrentInstance();
	}

	public boolean shouldForkForGclog(ExecutionContext ctx) {
		m_meta = ctx.getMethod().getAnnotation(MemoryMeta.class);

		return m_meta != null && m_meta.gcinfo();
	}
}
