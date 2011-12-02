package com.site.bes.engine;

import com.site.kernel.common.BaseEnum;

public class EventProcessStatus extends BaseEnum {
	// ready for processing
	public static final EventProcessStatus PROCESSING = new EventProcessStatus(0, "processing");

	// all events are processed successful, no missing, no skipped
	public static final EventProcessStatus ALL_SUCCESS = new EventProcessStatus(1, "all success");

	// all events are processed with failure
	public static final EventProcessStatus ALL_FAILURE = new EventProcessStatus(-1, "all failure");

	// some events are failure, some events are successful
	public static final EventProcessStatus PARTIAL_FAILURE = new EventProcessStatus(-2, "partial failure");

	private EventProcessStatus(int id, String name) {
		super(id, name);
	}

}
