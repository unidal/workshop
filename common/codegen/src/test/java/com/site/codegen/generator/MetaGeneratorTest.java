package com.site.codegen.generator;

import java.io.File;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.site.codegen.meta.ModelMeta;
import com.site.helper.Files;
import com.site.lookup.ComponentTestCase;

@RunWith(BlockJUnit4ClassRunner.class)
public class MetaGeneratorTest extends ComponentTestCase {
	private boolean verbose = true;

	@Test
	// @Ignore
	public void testGenerateReportExcelMetaModel() throws Exception {
		long start = System.currentTimeMillis();

		ModelMeta xmlMeta = lookup(ModelMeta.class);
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		StringWriter writer = new StringWriter(8192);
		Document root = xmlMeta.getCodegen(new InputStreamReader(getClass().getResourceAsStream(
		      "eunit_excel_instruments.xml"), "utf-8"));
		String packageName = getClass().getPackage().getName();
		File file = new File(String.format("%s/src/test/resources/%s%s", ".", packageName.replace('.', '/'),
		      "/eunit_excel_codegen.xml"));

		outputter.output(root, writer);

		String result = writer.toString();
		Files.forIO().writeTo(file, result);

		if (verbose) {
			long now = System.currentTimeMillis();

			System.out.println(String.format("%s files generated in %s ms.", file.getCanonicalFile(), now - start));
		}
	}

	@Test
	@Ignore
	public void testGenerateEunitResourceMetaModel() throws Exception {
		long start = System.currentTimeMillis();

		ModelMeta xmlMeta = lookup(ModelMeta.class);
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		StringWriter writer = new StringWriter(8192);
		Document root = xmlMeta.getCodegen(new InputStreamReader(getClass().getResourceAsStream(
		      "eunit_resource_sample.xml"), "utf-8"));
		String packageName = getClass().getPackage().getName();
		File file = new File(String.format("%s/src/test/resources/%s%s", ".", packageName.replace('.', '/'),
		      "/eunit_resource_codegen.xml"));

		outputter.output(root, writer);

		String result = writer.toString();
		Files.forIO().writeTo(file, result);

		if (verbose) {
			long now = System.currentTimeMillis();

			System.out.println(String.format("%s files generated in %s ms.", file.getCanonicalFile(), now - start));
		}
	}
}
