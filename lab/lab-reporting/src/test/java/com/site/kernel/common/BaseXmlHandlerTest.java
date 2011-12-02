package com.site.kernel.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class BaseXmlHandlerTest extends BaseTestCase {
	public void testXmlParserFactoryConfiguration() throws SAXException, IOException {
		TestXmlHandler handler = new TestXmlHandler();
		ByteArrayInputStream bais = new ByteArrayInputStream("<root/>".getBytes());

		handler.parse(bais);
	}

	private static class TestXmlHandler extends BaseXmlHandler {
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			assertEquals(localName, "root");
		}
	}
}
