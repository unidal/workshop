package com.site.bes.common.helpers;

import java.util.ArrayList;
import java.util.List;

import com.site.bes.EventPayload;
import com.site.bes.EventPayloadFormat;
import com.site.kernel.SystemInjection;

public abstract class BasePayloadFormatter {
	public abstract EventPayloadFormat getPayloadType();

	public abstract void parse(List<String> names, List<String> values, String data);

	public final void assemble(EventPayload payload, String data) {
		List<String> names = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		parse(names, values, data);
		SystemInjection.injectProperties(payload, names, values);
	}

}
