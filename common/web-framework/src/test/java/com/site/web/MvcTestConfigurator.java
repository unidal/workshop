package com.site.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.site.lookup.configuration.Component;
import com.site.web.configuration.AbstractWebComponentsConfigurator;
import com.site.web.test.book.BookModule;

public class MvcTestConfigurator extends AbstractWebComponentsConfigurator {
	public static void main(String[] args) {
		generatePlexusComponentsXmlFile(new MvcTestConfigurator());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		defineModuleRegistry(all, null, BookModule.class);

		return all;
	}

	@Override
	protected File getConfigurationFile() {
		return new File("src/test/resources/" + MvcTest.class.getName().replace('.', '/') + ".xml");
	}
}
