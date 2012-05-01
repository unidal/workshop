package com.site.codegen.generator.cat;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.dianping.cat.configuration.client.entity.ClientConfig;
import com.dianping.cat.configuration.client.transform.DefaultSaxParser;
import com.dianping.cat.consumer.problem.model.entity.ProblemReport;
import com.dianping.cat.consumer.problem.model.transform.DefaultXmlParser;
import com.site.helper.Files;

public class XmlTest {
   @Test
   public void testDefault() throws Exception {
      DefaultXmlParser parser = new DefaultXmlParser();
      String expected = Files.forIO().readFrom(getClass().getResourceAsStream("problem.xml"), "utf-8");
      ProblemReport report = parser.parse(expected);

      Assert.assertEquals("XML is not well parsed!", expected.replace("\r", ""), report.toString().replace("\r", ""));
   }
   
	@Test
	public void testSaxParser() throws ParserConfigurationException, SAXException, IOException {
		InputStream is = getClass().getResourceAsStream("client.xml");
		String xml = Files.forIO().readFrom(is, "utf-8");
		ClientConfig config = DefaultSaxParser.parse(xml);

		Assert.assertEquals(xml.replace("\r", ""), config.toString().replace("\r", ""));
	}

   @Test
   public void testCompact() throws Exception {
      DefaultXmlParser parser = new DefaultXmlParser();
      String expected = Files.forIO().readFrom(getClass().getResourceAsStream("problem-compact.xml"), "utf-8");
      ProblemReport report = parser.parse(expected);
      String xml = String.format("%1.0s", report);
      
      Assert.assertEquals("XML is not well parsed!", expected.replace("\r", ""), xml.replace("\r", ""));
   }
}
