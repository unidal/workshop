package com.site.maven.plugin.wizard;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.site.helper.Files;
import com.site.maven.plugin.wizard.model.entity.Wizard;

public class WebAppMojoTest {
	@Test
	public void test() throws IOException, SAXException {
		WebAppMojo mojo = new WebAppMojo();
		File wizardFile = new File("wizard.xml");
		Wizard wizard = mojo.buildWizard(wizardFile);

		Files.forIO().writeTo(wizardFile.getCanonicalFile(), wizard.toString());
		System.out.println(wizard);
	}
}
