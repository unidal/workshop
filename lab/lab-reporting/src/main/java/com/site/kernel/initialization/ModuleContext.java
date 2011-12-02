package com.site.kernel.initialization;

import java.util.HashMap;
import java.util.Map;

import com.site.kernel.logging.Log;

public class ModuleContext {
	private Log s_log = Log.getLog(ModuleContext.class);

	private Map<String, Object> m_parameters;

	public ModuleContext() {
		m_parameters = new HashMap<String, Object>();
	}

	public Object getParameter(String name) {
		return m_parameters.get(name);
	}

	public void setParameter(String name, Object value) {
		Object oldValue = m_parameters.put(name, value);

		if (oldValue != null) {
			s_log.warn("Parameter(" + name + ") in ModuleContext was replaced by " + value);
		}
	}
}
