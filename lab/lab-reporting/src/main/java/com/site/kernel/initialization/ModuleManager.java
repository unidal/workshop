package com.site.kernel.initialization;

import java.io.File;

import com.site.kernel.SystemPathFinder;
import com.site.kernel.dal.model.DefaultHandler;
import com.site.kernel.dal.model.ModelRegistry;
import com.site.kernel.initialization.config.ModulesBo;

public class ModuleManager {
	private static final String MODULE = "module.xml";

	private static void initModels() {
		ModelRegistry.register(com.site.kernel.initialization.config.ModulesDo.class, com.site.kernel.initialization.config.ModulesBo.class);
		ModelRegistry.register(com.site.kernel.initialization.config.ModuleDo.class, com.site.kernel.initialization.config.ModuleBo.class);
		ModelRegistry.register(com.site.kernel.initialization.config.ConfigurationDo.class, com.site.kernel.initialization.config.ConfigurationBo.class);
	}

	public static void initialize() {
		initModels();

		ModulesBo modules = loadModules();
		modules.initialize();
	}

	private static ModulesBo loadModules() {
		File xmlFile = new File(SystemPathFinder.getAppConfig(), MODULE);

		try {
			DefaultHandler parser = new DefaultHandler(ModulesBo.class);

			parser.parse(xmlFile.getAbsolutePath());
			parser.validateModel();

			return (ModulesBo) parser.getRootModel();
		} catch (Throwable t) {
			t.printStackTrace();

			throw new RuntimeException(t.getMessage());
		}
	}

}
