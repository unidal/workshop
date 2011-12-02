package org.unidal.configuration;

import java.util.ArrayList;
import java.util.List;

import org.unidal.expense.biz.ExpenseModule;

import com.site.lookup.configuration.Component;
import com.site.web.configuration.AbstractWebComponentsConfigurator;

public class ComponentsConfigurator extends AbstractWebComponentsConfigurator {
	@Override
	@SuppressWarnings("unchecked")
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		all.addAll(new DatabaseConfigurator().defineComponents());
		
		defineModuleRegistry(all, ExpenseModule.class, ExpenseModule.class);

		return all;
	}

	public static void main(String[] args) {
		generatePlexusComponentsXmlFile(new ComponentsConfigurator());
	}
}
