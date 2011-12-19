package com.site.maven.plugin.wizard;

import org.junit.Test;

public class PropertyProvidersTest {
	@Test
	public void testConsole() {
		String packageName = PropertyProviders.fromConsole().forString("package", "Package Name:", null, null);
		boolean webres = PropertyProviders.fromConsole().forBoolean("webres", "Support WebRes?", true);

		System.out.println(packageName + ":" + webres);
	}
}
