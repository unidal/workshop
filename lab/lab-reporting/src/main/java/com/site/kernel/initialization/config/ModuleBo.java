package com.site.kernel.initialization.config;

import java.lang.reflect.Field;
import java.util.Stack;

import com.site.kernel.initialization.BaseModule;
import com.site.kernel.initialization.ModuleContext;
import com.site.kernel.util.ClazzLoader;

public class ModuleBo extends ModuleDo {

	static {
		init();
	}

	public ConfigurationBo getConfigurationBo() {
		return (ConfigurationBo) getConfigurationDo();
	}

	public void setConfigurationBo(ConfigurationBo configuration) {
		setConfigurationDo(configuration);
	}

	public void initialize() {
		ConfigurationBo config = getConfigurationBo();
		BaseModule module = getModule();
		ModuleContext ctx = new ModuleContext();

		if (config != null) {
			config.setParameters(ctx);
		}

		module.doInitialization(ctx, new Stack<BaseModule>(), isVerbose());
	}

	/**
	 * i.e. com.site.kernel.Module.FULL
	 */
	private BaseModule getModule() {
		String name = getName();
		int index = name.lastIndexOf('.');

		try {
			Class clazz = ClazzLoader.loadClass(name.substring(0, index));
			Field field = clazz.getField(name.substring(index + 1));

			return (BaseModule) field.get(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
