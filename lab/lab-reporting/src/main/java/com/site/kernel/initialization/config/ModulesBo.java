package com.site.kernel.initialization.config;

import java.util.List;

public class ModulesBo extends ModulesDo {

	static {
		init();
	}

	public void addModuleBo(ModuleBo module) {
		addModuleDo(module);
	}

	public List getModuleBos() {
		return getModuleDos();
	}

	public void initialize() {
		List modules = getModuleBos();
		int size = modules.size();

		for (int i = 0; i < size; i++) {
			ModuleBo module = (ModuleBo) modules.get(i);

			module.initialize();
		}
	}
}
