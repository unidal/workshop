package com.site.codegen.generator.cat;

import junit.framework.Assert;

import org.junit.Test;

import com.dianping.cat.consumer.failure.model.entity.FailureReport;
import com.dianping.cat.consumer.failure.model.transform.DefaultParser;
import com.site.helper.Files;

public class XmlTest {
	@Test
	public void testDefault() throws Exception {
		DefaultParser parser = new DefaultParser();
		String xml = Files.forIO().readFrom(getClass().getResourceAsStream("failure.xml"), "utf-8");
		FailureReport report = parser.parse(xml);

		Assert.assertEquals("XML is not well parsed!", xml, report.toString().replace("\r", ""));
	}
}
