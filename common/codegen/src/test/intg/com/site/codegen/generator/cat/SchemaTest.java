package com.site.codegen.generator.cat;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Test;

import com.dianping.cat.consumer.problem.model.IEntity;

public class SchemaTest {
	@Test
	public void testProblemReport() throws Exception {
		// define the type of schema - we use W3C:
		String schemaLang = "http://www.w3.org/2001/XMLSchema";

		// get validation driver:
		SchemaFactory factory = SchemaFactory.newInstance(schemaLang);

		// create schema by reading it from an XSD file:
		String path = "/" + IEntity.class.getPackage().getName().replace('.', '/');
		Schema schema = factory.newSchema(new StreamSource(getClass().getResourceAsStream(path + "/problem-report.xsd")));
		Validator validator = schema.newValidator();

		// at last perform validation:
		validator.validate(new StreamSource(getClass().getResourceAsStream("problem.xml")));
	}
}
