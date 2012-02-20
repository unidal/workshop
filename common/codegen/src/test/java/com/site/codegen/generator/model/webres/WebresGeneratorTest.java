package com.site.codegen.generator.model.webres;

import java.io.File;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.site.codegen.generator.AbstractGenerateContext;
import com.site.codegen.generator.GenerateContext;
import com.site.codegen.generator.Generator;
import com.site.lookup.ComponentTestCase;

@RunWith(JUnit4.class)
public class WebresGeneratorTest extends ComponentTestCase {
	private boolean verbose = false;

	private boolean debug = false;

	@Test
	public void testGenerateResourceModel() throws Exception {
		Generator g = lookup(Generator.class, "dal-model");
		URL manifestXml = getResourceFile("resource_model_manifest.xml").toURI().toURL();
		GenerateContext ctx = new DalGenerateContext(new File("."), "model", manifestXml);
		long start = System.currentTimeMillis();

		g.generate(ctx);

		if (verbose) {
			long now = System.currentTimeMillis();

			System.out.println(String.format("%s files generated in %s ms.", ctx.getGeneratedFiles(), now - start));
		}
	}

	@Test
	public void testGenerateResourceProfile() throws Exception {
		Generator g = lookup(Generator.class, "dal-model");
		URL manifestXml = getResourceFile("resource_profile_manifest.xml").toURI().toURL();
		GenerateContext ctx = new DalGenerateContext(new File("."), "model", manifestXml);
		long start = System.currentTimeMillis();

		g.generate(ctx);

		if (verbose) {
			long now = System.currentTimeMillis();

			System.out.println(String.format("%s files generated in %s ms.", ctx.getGeneratedFiles(), now - start));
		}
	}

	@Test
	public void testGenerateResourceTestFwkModel() throws Exception {
		Generator g = lookup(Generator.class, "dal-model");
		URL manifestXml = getResourceFile("resource_testfwk_manifest.xml").toURI().toURL();
		GenerateContext ctx = new DalGenerateContext(new File("."), "model", manifestXml);
		long start = System.currentTimeMillis();

		g.generate(ctx);

		if (verbose) {
			long now = System.currentTimeMillis();

			System.out.println(String.format("%s files generated in %s ms.", ctx.getGeneratedFiles(), now - start));
		}
	}

	@Test
	public void testGenerateResourceVariation() throws Exception {
		Generator g = lookup(Generator.class, "dal-model");
		URL manifestXml = getResourceFile("resource_variation_manifest.xml").toURI().toURL();
		GenerateContext ctx = new DalGenerateContext(new File("."), "model", manifestXml);
		long start = System.currentTimeMillis();

		g.generate(ctx);

		if (verbose) {
			long now = System.currentTimeMillis();

			System.out.println(String.format("%s files generated in %s ms.", ctx.getGeneratedFiles(), now - start));
		}
	}

	class DalGenerateContext extends AbstractGenerateContext {
		private URL m_manifestXml;

		public DalGenerateContext(File projectBase, String type, URL manifestXml) {
			super(projectBase, "/META-INF/dal/" + type, "target/generated-model/webres");

			m_manifestXml = manifestXml;
		}

		public DalGenerateContext(File projectBase, String type, URL manifestXml, String sourceDir) {
			super(projectBase, "/META-INF/dal/" + type, sourceDir);

			m_manifestXml = manifestXml;
		}

		public URL getManifestXml() {
			return m_manifestXml;
		}

		public void log(LogLevel logLevel, String message) {
			switch (logLevel) {
			case DEBUG:
				if (debug) {
					System.out.println(message);
				}
				break;
			case INFO:
				if (debug || verbose) {
					System.out.println(message);
				}
				break;
			case ERROR:
				System.out.println(message);
				break;
			}
		}
	}
}