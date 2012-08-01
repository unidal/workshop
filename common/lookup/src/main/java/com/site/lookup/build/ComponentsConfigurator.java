package com.site.lookup.build;

import java.util.ArrayList;
import java.util.List;

import com.site.initialization.DefaultModuleInitializer;
import com.site.initialization.DefaultModuleManager;
import com.site.initialization.ModuleInitializer;
import com.site.initialization.ModuleManager;
import com.site.lookup.configuration.AbstractResourceConfigurator;
import com.site.lookup.configuration.Component;

public class ComponentsConfigurator extends AbstractResourceConfigurator {
	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		all.add(C(ModuleManager.class, DefaultModuleManager.class));
		all.add(C(ModuleInitializer.class, DefaultModuleInitializer.class) //
		      .req(ModuleManager.class));

		return all;
	}

	public static void main(String[] args) {
		generatePlexusComponentsXmlFile(new ComponentsConfigurator());
	}
}
