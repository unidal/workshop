package com.site.codegen.generator;

import java.io.File;
import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.site.lookup.ComponentTestCase;

@RunWith(BlockJUnit4ClassRunner.class)
public class GeneratorTest extends ComponentTestCase {
	private boolean verbose = true;

	private boolean debug = false;

	@Test
	@Ignore
	public void testGenerateBenchmarkTestFwkModel() throws Exception {
		Generator g = lookup(Generator.class, "dal-model");
		URL manifestXml = getResourceFile("eunit_benchmark_testfwk_manifest.xml").toURI().toURL();
		GenerateContext ctx = new DalGenerateContext(new File("../eunit-testfwk"), "model", manifestXml, "src/main/java");
		long start = System.currentTimeMillis();

		g.generate(ctx);

		if (verbose) {
			long now = System.currentTimeMillis();

			System.out.println(String.format("%s files generated in %s ms.", ctx.getGeneratedFiles(), now - start));
		}
	}

	@Test
	@Ignore
	public void testGenerateCmdTestFwkModel() throws Exception {
		Generator g = lookup(Generator.class, "dal-model");
		final URL manifestXml = getResourceFile("eunit_cmd_testfwk_manifest.xml").toURI().toURL();
		GenerateContext ctx = new DalGenerateContext(new File("../eunit-cmd-testfwk"), "model", manifestXml,
		      "src/main/java");

		long start = System.currentTimeMillis();

		g.generate(ctx);

		if (verbose) {
			long now = System.currentTimeMillis();

			System.out.println(String.format("%s files generated in %s ms.", ctx.getGeneratedFiles(), now - start));
		}
	}

	@Test
	@Ignore
	public void testGenerateCompatibilityTestModel() throws Exception {
		Generator g = lookup(Generator.class, "dal-model");
		URL manifestXml = getResourceFile("ctest_report_model_manifest.xml").toURI().toURL();
		GenerateContext ctx = new DalGenerateContext(new File("."), "model", manifestXml);
		long start = System.currentTimeMillis();

		g.generate(ctx);

		if (verbose) {
			long now = System.currentTimeMillis();

			System.out.println(String.format("%s files generated in %s ms.", ctx.getGeneratedFiles(), now - start));
		}
	}

	@Test
	@Ignore
	public void testGenerateEunitResourceModel() throws Exception {
		Generator g = lookup(Generator.class, "dal-model");
		URL manifestXml = getResourceFile("eunit_resource_manifest.xml").toURI().toURL();
		GenerateContext ctx = new DalGenerateContext(new File("."), "model", manifestXml);
		long start = System.currentTimeMillis();

		g.generate(ctx);

		if (verbose) {
			long now = System.currentTimeMillis();

			System.out.println(String.format("%s files generated in %s ms.", ctx.getGeneratedFiles(), now - start));
		}
	}

	@Test
	@Ignore
	public void testGenerateEunitTestFwkModel() throws Exception {
		Generator g = lookup(Generator.class, "dal-model");
		final URL manifestXml = getResourceFile("eunit_testfwk_manifest.xml").toURI().toURL();
		GenerateContext ctx = new DalGenerateContext(new File("../eunit-testfwk"), "model", manifestXml, "src/main/java");

		long start = System.currentTimeMillis();

		g.generate(ctx);

		if (verbose) {
			long now = System.currentTimeMillis();

			System.out.println(String.format("%s files generated in %s ms.", ctx.getGeneratedFiles(), now - start));
		}
	}

	@Test
	@Ignore
	public void testGenerateJdbc() throws Exception {
		Generator g = lookup(Generator.class, "dal-jdbc");
		final URL manifestXml = getResourceFile("jdbc_manifest.xml").toURI().toURL();
		GenerateContext ctx = new DalGenerateContext(new File("."), "jdbc", manifestXml);
		long start = System.currentTimeMillis();

		g.generate(ctx);

		if (verbose) {
			long now = System.currentTimeMillis();

			System.out.println(String.format("%s files generated in %s ms.", ctx.getGeneratedFiles(), now - start));
		}
	}

	@Test
	@Ignore
	public void testGenerateReportExcelModel() throws Exception {
		Generator g = lookup(Generator.class, "dal-model");
		URL manifestXml = getResourceFile("eunit_excel_manifest.xml").toURI().toURL();
		GenerateContext ctx = new DalGenerateContext(new File("../eunit-testfwk"), "model", manifestXml, "src/main/java");
		long start = System.currentTimeMillis();

		g.generate(ctx);

		if (verbose) {
			long now = System.currentTimeMillis();

			System.out.println(String.format("%s files generated in %s ms.", ctx.getGeneratedFiles(), now - start));
		}
	}

	@Test
	@Ignore
	public void testGenerateResourceModel() throws Exception {
		Generator g = lookup(Generator.class, "dal-model");
		URL manifestXml = getResourceFile("resource_model_manifest.xml").toURI().toURL();
		GenerateContext ctx = new DalGenerateContext(new File("."), "model", manifestXml,
		      "C:/project/webres/WebResRuntime/src/main/java");
		long start = System.currentTimeMillis();

		g.generate(ctx);

		if (verbose) {
			long now = System.currentTimeMillis();

			System.out.println(String.format("%s files generated in %s ms.", ctx.getGeneratedFiles(), now - start));
		}
	}

	@Test
	@Ignore
	public void testGenerateResourceProfile() throws Exception {
		Generator g = lookup(Generator.class, "dal-model");
		URL manifestXml = getResourceFile("resource_profile_manifest.xml").toURI().toURL();
		GenerateContext ctx = new DalGenerateContext(new File("."), "model", manifestXml,
		      "C:/project/webres/WebResRuntime/src/main/java");
		long start = System.currentTimeMillis();

		g.generate(ctx);

		if (verbose) {
			long now = System.currentTimeMillis();

			System.out.println(String.format("%s files generated in %s ms.", ctx.getGeneratedFiles(), now - start));
		}
	}

	@Test
	@Ignore
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
	@Ignore
	public void testGenerateResourceVariation() throws Exception {
		Generator g = lookup(Generator.class, "dal-model");
		URL manifestXml = getResourceFile("resource_variation_manifest.xml").toURI().toURL();
		GenerateContext ctx = new DalGenerateContext(new File("."), "model", manifestXml,
		      "C:/project/webres/WebResRuntime/src/main/java");
		long start = System.currentTimeMillis();

		g.generate(ctx);

		if (verbose) {
			long now = System.currentTimeMillis();

			System.out.println(String.format("%s files generated in %s ms.", ctx.getGeneratedFiles(), now - start));
		}
	}

	@Test
	public void testGenerateWizardPage() throws Exception {
		Generator g = lookup(Generator.class, "wizard-webapp");
		URL manifestXml = getResourceFile("wizard_webapp_manifest.xml").toURI().toURL();
		GenerateContext ctx = new WizardGenerateContext(new File("."), "webapp", manifestXml, "../../../garden/app-garden/src/main/java");
		long start = System.currentTimeMillis();

		g.generate(ctx);

		if (verbose) {
			long now = System.currentTimeMillis();

			System.out.println(String.format("%s files generated in %s ms.", ctx.getGeneratedFiles(), now - start));
		}
	}

	@Test
	@Ignore
	public void testGenerateXml() throws Exception {
		Generator g = lookup(Generator.class, "dal-xml");
		URL manifestXml = getResourceFile("xml_manifest.xml").toURI().toURL();
		GenerateContext ctx = new DalGenerateContext(new File("."), "xml", manifestXml);

		g.generate(ctx);

		if (verbose) {
			System.out.println(ctx.getGeneratedFiles() + " files generated.");
		}
	}

	class DalGenerateContext extends AbstractGenerateContext {
		private URL m_manifestXml;

		public DalGenerateContext(File projectBase, String type, URL manifestXml) {
			super(projectBase, "/META-INF/dal/" + type, "target/generated-test-sources/dal-" + type);

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

	class WizardGenerateContext extends AbstractGenerateContext {
		private URL m_manifestXml;

		public WizardGenerateContext(File projectBase, String type, URL manifestXml) {
			super(projectBase, "/META-INF/wizard/" + type, "target/generated-test-sources/wizard-" + type);

			m_manifestXml = manifestXml;
		}

		public WizardGenerateContext(File projectBase, String type, URL manifestXml, String sourceDir) {
			super(projectBase, "/META-INF/wizard/" + type, sourceDir);

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
