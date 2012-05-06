package com.site.codegen.generator.cat.consumer;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Assert;
import org.junit.Test;

import com.dianping.cat.consumer.transaction.model.IEntity;
import com.dianping.cat.consumer.transaction.model.entity.TransactionReport;
import com.dianping.cat.consumer.transaction.model.transform.DefaultJsonBuilder;
import com.dianping.cat.consumer.transaction.model.transform.DefaultSaxParser;
import com.dianping.cat.consumer.transaction.model.transform.DefaultXmlBuilder;
import com.dianping.cat.consumer.transaction.model.transform.DefaultXmlParser;
import com.site.helper.Files;

public class TransactionReportTest {
	@Test
	public void testDomParser() throws Exception {
		String source = Files.forIO().readFrom(getClass().getResourceAsStream("TransactionReport.xml"), "utf-8");
		TransactionReport report = new DefaultXmlParser().parse(source);
		String xml = new DefaultXmlBuilder().buildXml(report);
		String expected = source;

		Assert.assertEquals("XML is not well parsed!", expected.replace("\r", ""), xml.replace("\r", ""));
	}

	@Test
	public void testJson() throws Exception {
		DefaultXmlParser parser = new DefaultXmlParser();
		String source = Files.forIO().readFrom(getClass().getResourceAsStream("TransactionReport.xml"), "utf-8");
		TransactionReport report = parser.parse(source);
		String json = new DefaultJsonBuilder().buildJson(report);
		String expected = Files.forIO().readFrom(getClass().getResourceAsStream("TransactionReport.json"), "utf-8");

		Assert.assertEquals("XML is not well parsed or JSON is not well built!", expected.replace("\r", ""),
		      json.replace("\r", ""));
	}

	@Test
	public void testSaxParser() throws Exception {
		String source = Files.forIO().readFrom(getClass().getResourceAsStream("TransactionReport.xml"), "utf-8");
		TransactionReport report = DefaultSaxParser.parse(source);
		String xml = new DefaultXmlBuilder().buildXml(report);
		String expected = source;

		Assert.assertEquals("XML is not well parsed!", expected.replace("\r", ""), xml.replace("\r", ""));
	}

	@Test
	public void testSchema() throws Exception {
		// define the type of schema - we use W3C:
		String schemaLang = "http://www.w3.org/2001/XMLSchema";

		// get validation driver:
		SchemaFactory factory = SchemaFactory.newInstance(schemaLang);

		// create schema by reading it from an XSD file:
		String path = "/" + IEntity.class.getPackage().getName().replace('.', '/');
		Schema schema = factory.newSchema(new StreamSource(getClass().getResourceAsStream(
		      path + "/transaction-report.xsd")));
		Validator validator = schema.newValidator();

		// at last perform validation:
		validator.validate(new StreamSource(getClass().getResourceAsStream("TransactionReport.xml")));
	}
}
